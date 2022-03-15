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

        int round = getUserRound(currentUser);

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
                row3.add("Sales");
                row3.add("Reception");
                rowN.add("Va Boshqalar");
                rowList.add(row1);
                rowList.add(row2);
                rowList.add(row3);
                rowList.add(rowN);
                break;
            case 4:
                row1.add("Mentor");
                row1.add("Assistant");
                row1.add("Marketing Manager");
                row2.add("Sales Manager");
                row2.add("CEO");// todo Lavozimlarni to'ldirish kerak
                row2.add("ECMA");
                row3.add("Buxgalter");
                row3.add("AXO");
                row3.add("Unicorn Manager");
                rowN.add("Va Boshqalar");
                rowList.add(row1);
                rowList.add(row2);
                rowList.add(row3);
                rowList.add(rowN);
                break;
        }
        // bu joyda roundga 5 yoki 5 dan kichik bo'lsa user positioniga qarab menu buttonlar chiqayotgandi.
        return keyboardMarkup;
    }

    public int getUserRound(User currentUser){
        if (currentUser.isRegister()){
            return currentUser.getRound();
        }else {
            UserActivity userActivity = DataBase.userActivityMap.get(currentUser.getChatId());
            return userActivity.getRound();
        }
    }
}
