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
import uz.fazliddin.model.*;
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
public class AdminService {
    public void adminServiceMainMethod(User currentUser, Update update, UserActivity userActivity) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId().toString());
            if (currentUser.getRound() < 55) {
                if (message.hasText()) {
                    String text = message.getText();
                    if (text.equals("/start")) {
                        userActivity.setRound(0);
                        sendMessageUser(currentUser, "Asosiy menu", true, userActivity, update);
                    } else if (text.equals("Orqaga")) {
                        sendMessageUser(currentUser, "Orqaga yurdingiz 🔙", true, userActivity, update);
                        userActivity.setRound(userActivity.getRound() - 1);
                    } else if (text.equals("Bugungi ovqatlar")) {
                        if (DB.footList().isEmpty()) {
                            userActivity.setRound(0);
                            sendMessageUser(currentUser, "Bugungi ovqatlar ro'yxati bo'sh ekan ❌", true, userActivity, update);
                        } else {
                            userActivity.setRound(1);
                            currentUser.setRound(50);
                            sendMessageUser(currentUser, "Bugungi ovqatlar ro'yhati 🍲\nTanlab ustiga bosing 👇", true, userActivity, update);
                        }
                    } else if (text.equals("Settings")) {
                        userActivity.setRound(3);
                        sendMessageUser(currentUser, "Profil sozlamalari ⚙️", true, userActivity, update);
                    } else if (text.equals("Ovqat qo'shish")) {
                        userActivity.setRound(7);
                        currentUser.setRound(51);
                        sendMessageUser(currentUser, "Bugungi menuga ovqat qo'shish uchun ovqat ustiga bosing ➕", true, userActivity, update);
                    } else if (text.equals("Bosh menu")) {
                        userActivity.setRound(0);
                        sendMessageUser(currentUser, "Bosh menuga qaytdingiz 👋", true, userActivity, update);
                    } else if (text.equals("Ro'yxatni HR ga jo'natish")) {
                        userActivity.setRound(6);
                        FileService fileService = new FileService();
                        fileService.keepToExel(DB.userFoodList());
                        User hr = DB.getUserFromPosition("HR");
                        sendDocumentUser(hr);
                        sendMessageUser(currentUser, "Bugungi ro'yxatni HR ga yuborildi 📤.", true, userActivity, update);
                    } else if (text.equals("Hamma userlarni ko'rish")) {
                        StringBuilder builder = new StringBuilder();
                        int a = 0;
                        for (User user : DB.allUser()) {
                            builder.append(++a).append(". ").append(user.getFullName()).append(" ").append(user.getDepartment()).append(" ").append(user.getPosition()).append(" ").append(user.getPhoneNumber()).append("\n");
                        }
                        sendMessageUser(currentUser, builder.toString(), true, userActivity, update);
                    } else if (text.equals("Buyurtma berilgan ovqatlarning soni")) {
                        if (!DB.userFoodList().isEmpty()) {
                            userActivity.setRound(7);
                            sendMessageUser(currentUser, "Ovqatlar soni", false, userActivity, update);
                            Map<String, Integer> map = DB.foodQuantity(DB.userFoodList());
                            StringBuilder builder = new StringBuilder();
                            map.forEach((ovqat, soni) -> builder.append(ovqat).append(" : ").append(soni).append("\n"));
                            sendMessageUser(currentUser, builder.toString(), true, userActivity, update);
                        } else {
                            userActivity.setRound(0);
                            sendMessageUser(currentUser, "Bugungi ovqatlar ro'yxati hozircha bo'sh ekan ❌", true, userActivity, update);
                        }
                    } else {
                        if (currentUser.getRound() == 50) {
                            for (Food food : DB.footList()) {
                                if (text.equals(food.getName())) {
                                    LocalDateTime localDateTime = LocalDateTime.now();
                                    LocalDate localDate = LocalDate.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth());
                                    LocalTime localTime = LocalTime.of(11, 0, 0);
                                    if (localDateTime.getDayOfMonth() == localDate.getDayOfMonth() && localDateTime.getHour() < localTime.getHour()) {
                                        UserFood userFood = new UserFood();
                                        userFood.setFoodName(food.getName());
                                        userFood.setUserFullName(currentUser.getFullName());
                                        userFood.setKuni(LocalDateTime.now());
                                        userFood.setUserPosition(currentUser.getPosition());
                                        DB.addFoodToUser(userFood);
                                        sendMessageUser(currentUser, "Ovqat belgilandi : " + food.getName() + " : " + localDateTime.getDayOfMonth() + ":" + localDateTime.getMonthValue() + ":" + localDateTime.getYear()
                                                + "  " + localDateTime.getHour() + ":" + localDateTime.getMinute() + " ✅", true, userActivity, update);

                                        userActivity.setRound(5);
                                    } else {
                                        sendMessageUser(currentUser, "Ovqat tanlashga ulgurmadingiz 😞", true, userActivity, update);
                                        userActivity.setRound(5);
                                    }
                                }
                                // bu joyda admin soat 11 gacha ovqat buyurtma bera oladi va ko'plab ovqatlar buyurtma berishi mumkin
                            }
                        } else if (currentUser.getRound() == 51) {
                            for (GeneralFood food : DB.generalFoodList()) {
                                if (text.equals(food.getName())) {
                                    Food food1 = new Food();
                                    food1.setName(food.getName());
                                    food1.setTimestamp(LocalDateTime.now());
                                    DB.addFoodToToday(food1);
                                    sendMessageUser(currentUser, "Ovqat qo'shildi : ✅", false, userActivity, update);
                                    userActivity.setRound(7);
                                    break;
                                }
                            }
                            // bu joyda admin soat 11 gacha ovqat buyurtma bera oladi va ko'plab ovqatlar buyurtma berishi mumkin
                        }
                    }
                }
            }
        }
    }

    private ReplyKeyboard getReplyKeyBoard(User currentUser, UserActivity userActivity, Update update) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> rowList = new ArrayList<>();
        keyboardMarkup.setKeyboard(rowList);
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        KeyboardRow row4 = new KeyboardRow();
        KeyboardRow row5 = new KeyboardRow();
        KeyboardRow rowN = new KeyboardRow();

        if (userActivity.getRound() < 10) {
            switch (userActivity.getRound()) {
                case 0:
                    row1.add("Bugungi ovqatlar");
                    row1.add("Settings");
                    row2.add("Ro'yxatni HR ga jo'natish");
                    row2.add("Ovqat qo'shish");
                    row3.add("Buyurtma berilgan ovqatlarning soni");
                    row3.add("User o'chirish");
                    row4.add("Hamma userlarni ko'rish");

                    rowList.add(row1);
                    rowList.add(row2);
                    rowList.add(row3);
                    rowList.add(row4);
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
                    rowN.add("Orqaga");
                    rowList.add(rowN);
                    break;
                case 7:
                    if (currentUser.getRound() > 50) {
                        List<GeneralFood> generalFoods = DB.generalFoodList();
                        int cycle2 = 1;
                        int k2 = 0;
                        for (int i = 0; i < cycle2; i++) {
                            for (int j = 0; j <= generalFoods.size(); j++) {
                                if (k2 <= 3) {
                                    assert generalFoods.get(k2) != null;
                                    row1.add(new KeyboardButton(generalFoods.get(k2).getName()));
                                    if (k2 == generalFoods.size() - 1) {
                                        break;
                                    }
                                    k2++;
                                } else if (k2 <= 7) {
                                    row2.add(new KeyboardButton(generalFoods.get(k2).getName()));
                                    if (k2 == generalFoods.size() - 1) {
                                        break;
                                    }
                                    k2++;
                                } else if (k2 <= 11) {
                                    row3.add(new KeyboardButton(generalFoods.get(k2).getName()));
                                    if (k2 == generalFoods.size() - 1) {
                                        break;
                                    }
                                    k2++;
                                } else if (k2 <= 15) {
                                    row4.add(new KeyboardButton(generalFoods.get(k2).getName()));
                                    if (k2 == generalFoods.size() - 1) {
                                        break;
                                    }
                                    k2++;
                                } else if (k2 <= 19) {
                                    row5.add(new KeyboardButton(generalFoods.get(k2).getName()));
                                    if (k2 == generalFoods.size() - 1) {
                                        break;
                                    }
                                    k2++;
                                }
                            }
                            rowN.add("Orqaga");
                            rowList.add(row1);
                            rowList.add(row2);
                            rowList.add(row3);
                            rowList.add(row4);
                            rowList.add(row5);
                            rowList.add(rowN);
                        }
                        break;
                    }
                    break;
            }
        }
        return keyboardMarkup;
    }

    public void sendMessageUser(User currentUser, String text, boolean is_replyMarkUp, UserActivity userActivity, Update update) {
        LunchBot myBot = new LunchBot();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(currentUser.getChatId());
        if (is_replyMarkUp) {
            sendMessage.setReplyMarkup(getReplyKeyBoard(currentUser, userActivity, update));
        }
        try {
            myBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    static void sendDocumentUser(User currentUser) {
        LunchBot myBot = new LunchBot();
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(currentUser.getChatId());
        sendDocument.setCaption("Thanks for using this bot");
        sendDocument.setDocument(new InputFile(new File("src/main/resources/royxat.xlsx")));
        try {
            myBot.execute(sendDocument);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
