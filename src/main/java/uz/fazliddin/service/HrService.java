package uz.fazliddin.service;

import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.fazliddin.LunchBot;
import uz.fazliddin.model.Food;
import uz.fazliddin.model.User;
import uz.fazliddin.model.UserActivity;
import uz.fazliddin.model.UserFood;
import uz.fazliddin.util.DB;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Fazliddin Xamdamov
 * @date 02.03.2022  15:56
 * @project New-Lunch-Bot2
 */
public class HrService {

    public void hrServiceMainMethod(User currentUser, Update update, UserActivity userActivity ,  UserFood userFood) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId().toString());
            if (message.hasText()) {
                String text = message.getText();
                if (text.equals("/start")) {
                    sendMessageUser(currentUser, "Asosiy menu", true, userActivity);
                } else if (text.equals("Orqaga")) {
                    sendMessageUser(currentUser, "Orqaga yurdingiz 🔙", true, userActivity);
                    userActivity.setRound(userActivity.getRound() - 1);
                } else if (text.equals("Bugungi ovqatlar")) {
                    if (DB.footList().isEmpty()) {
                        userActivity.setRound(0);
                        sendMessageUser(currentUser, "Bugungi ovqatlar ro'yxati bo'sh ekan ❌", true, userActivity);
                    } else {
                        userActivity.setRound(1);
                        sendMessageUser(currentUser, "Bugungi ovqatlar ro'yhati 🍲\nTanlab ustiga bosing 👇", true, userActivity);
                    }
                } else if (text.equals("Settings")) {
                    userActivity.setRound(3);
                    sendMessageUser(currentUser, "Profil sozlamalari ⚙️", true, userActivity);
                } else if (text.equals("Bosh menu")) {
                    userActivity.setRound(0);
                    sendMessageUser(currentUser, "Bosh menuga qaytdingiz 👋", true, userActivity);
                }else if (text.equals("Buyurtma berilgan ovqatlarning soni")) {
                    if (!DB.userFoodList().isEmpty()){
                        userActivity.setRound(7);
                        sendMessageUser(currentUser, "Ovqatlar soni", false, userActivity);
                        // soni todo qilish kerak hr bu ovqatdan qanhaligini bilishi kerak.
                        Map<String, Integer> map = DB.foodQuantity(DB.userFoodList());
                        StringBuilder builder = new StringBuilder();
                        map.forEach((ovqat, soni) -> builder.append(ovqat).append(" : ").append(soni).append("\n"));
                        sendMessageUser(currentUser, builder.toString(), true, userActivity);
                    }else {
                        userActivity.setRound(0);
                        sendMessageUser(currentUser, "Bugungi ovqatlar ro'yxati hozircha bo'sh ekan ❌", true, userActivity);
                    }
                }else {
                    for (Food food : DB.footList()) {
                        if (text.equals(food.getName())) {
                            if (DB.isAddFoodUser(currentUser.getFullName())){
                                LocalDateTime localDateTime = LocalDateTime.now();
                                LocalDate localDate = LocalDate.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth());
                                LocalTime localTime = LocalTime.of(10, 0, 0);
                                if (localDateTime.getDayOfMonth() == localDate.getDayOfMonth() && localDateTime.getHour() < localTime.getHour()) {
                                    userFood.setFoodName(food.getName());
                                    userFood.setUserFullName(currentUser.getFullName());
                                    userFood.setKuni(LocalDateTime.now());
                                    userFood.setUserPosition(currentUser.getPosition());
                                    DB.addFoodToUser(userFood);
                                    sendMessageUser(currentUser, "Ovqat belgilandi : " + food.getName() + " : " + localDateTime.getDayOfMonth()+":"+localDateTime.getMonthValue()+":"+localDateTime.getYear()
                                            +"  "+localDateTime.getHour()+":"+ localDateTime.getMinute()+" ✅", true, userActivity);

                                    userActivity.setRound(5);
                                } else {
                                    sendMessageUser(currentUser, "Ovqat tanlashga ulgurmadingiz 😞", true, userActivity);
                                    userActivity.setRound(5);
                                }
                            } else {
                                sendMessageUser(currentUser, "Tanlab bo'lgansiz ❌", true, userActivity);
                                userActivity.setRound(5);
                            }
                        }
                    }
                }
            }
        }
    }

    private ReplyKeyboard getReplyKeyBoard(User currentUser, UserActivity userActivity) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> rowList = new ArrayList<>();
        keyboardMarkup.setKeyboard(rowList);
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        KeyboardRow rowN = new KeyboardRow();

        switch (userActivity.getRound()) {
            case 0:
                row1.add("Bugungi ovqatlar");
                row2.add("Settings");
//                rowN.add("Bugungi ro'yxatni olish");
                row3.add("Buyurtma berilgan ovqatlarning soni");
                rowList.add(row1);
                rowList.add(row2);
                rowList.add(row3);
                break;
            case 1:
                List<Food> foods = DB.footList();
                int cycle = 1;
                int k = 0;
                for (int i = 0; i < cycle; i++) {
                    for (int j = 0; j <= foods.size(); j++) {
                        if (k <= 3) {
                            assert foods.get(k) != null;
                            row1.add(new KeyboardButton(foods.get(k).getName()));
                            if (k == foods.size() - 1) {
                                break;
                            }
                            k++;
                        } else {
                            row2.add(new KeyboardButton(foods.get(k).getName()));
                            if (k == foods.size() - 1) {
                                break;
                            }
                            k++;
                        }
                    }
                    rowN.add("Orqaga");
                    rowList.add(row1);
                    rowList.add(row2);
                    rowList.add(rowN);
                }
                break;

            case 3:
                row1.add("Hozircha sozlamalar paneli ishga tushgani yo'q! :( ");
                rowN.add("Orqaga");
                rowList.add(row1);
                rowList.add(rowN);
                break;
            case 4:
            case 5:
            case 6:
//                row1.add("Bosh menu");
                rowN.add("Orqaga");
//                rowList.add(row1);
                rowList.add(rowN);
                break;
        }
        return keyboardMarkup;
    }

    public void sendMessageUser(User currentUser, String text, boolean is_replyMarkUp, UserActivity userActivity) {
        LunchBot myBot = new LunchBot();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(currentUser.getChatId());
        if (is_replyMarkUp) {
            sendMessage.setReplyMarkup(getReplyKeyBoard(currentUser, userActivity));
        }
        try {
            myBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendDocumentUser(User currentUser) {
        LunchBot myBot = new LunchBot();
        SendDocument sendDocument = new SendDocument();
        sendDocument.setDocument(new InputFile(new File("src/main/resources/royxat.xlsx")));
        sendDocument.setChatId(currentUser.getChatId());
        try {
            myBot.execute(sendDocument);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
