package uz.fazliddin.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.fazliddin.LunchBot;
import uz.fazliddin.model.User;
import uz.fazliddin.model.UserActivity;
import uz.fazliddin.util.DataBase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fazliddin Xamdamov
 * @date 01.03.2022  16:13
 * @project New-Lunch-Bot2
 */

public class HelperMethods {

    public void sendMessage(User currentUser, String text, boolean is_replyMarkUp) {
        LunchBot myBot = new LunchBot();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(currentUser.getChatId());
        if (is_replyMarkUp) {
            sendMessage.setReplyMarkup(getReplyKeyBoard(currentUser));
        }
        sendMessage.setText(text);

        try {
            myBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private ReplyKeyboard getReplyKeyBoard(User currentUser) {

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> rowList = new ArrayList<>();
        keyboardMarkup.setKeyboard(rowList);
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        KeyboardRow rowN = new KeyboardRow();

        UserActivity userActivity = DataBase.userActivityMap.get(currentUser.getChatId());
        int round = userActivity.getRound();

        switch (round) {

            case 0:
                KeyboardButton button = new KeyboardButton();
                button.setText("Raqamni jo'natish"); // share contact
                button.setRequestContact(true);
                row1.add(button);
                rowList.add(row1);
                break;
            case 3:
                row1.add("Support");
                row1.add("Marketing");
                row2.add("ECMA");
                row2.add("Unicorn");
                rowList.add(row1);
                rowList.add(row2);
                break;
            case 4:
                row1.add("Mentor");
                row1.add("Asistent");
                row1.add("Manager");
                row1.add("HR");
                row2.add("CEO");// todo Lavozimlarni to'ldirish kerak
                row2.add("ECMA");
                row2.add("Buxgalter");
                row2.add("Va boshqalar");
                rowList.add(row1);
                rowList.add(row2);
                break;
            case 5:
                switch (currentUser.getUserStatus()) {
                    case "USER":
                        row1.add("Vaqt oralig'ini tanlash");
                        row1.add("bugungi ovqatlar");
                        row2.add("buyurtmamni bekor qilish");
                        row2.add("settings");
                        rowList.add(row1);
                        rowList.add(row2);
                        break;
                    case "ADMIN":
                        row1.add("Vaqt oralig'ini tanlash");
                        row1.add("bugungi ovqatlar");
                        row2.add("buyurtmamni bekor qilish");
                        row2.add("settings");
                        rowN.add("bugunli ro'yxatni korish");
                        rowN.add("ro'yxatni HR ga jo'nstish");
                        rowList.add(row1);
                        rowList.add(row2);
                        rowList.add(rowN);
                        break;
                    case "HR":
                        row1.add("bugunli ro'yxatni korish");
                        row1.add("ro'yxatni qubul qilish");
                        row2.add("ro'yxatni exelga chiqarish");
                        row2.add("settings");
                        break;
                }
                break;
        }
        return keyboardMarkup;
    }

}
