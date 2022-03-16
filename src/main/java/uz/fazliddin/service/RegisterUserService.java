//package uz.fazliddin.service;
//
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//import uz.fazliddin.LunchBot;
//import uz.fazliddin.model.User;
//import uz.fazliddin.model.UserActivity;
//import uz.fazliddin.util.DB;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author Fazliddin Xamdamov
// * @date 16.03.2022  10:35
// * @project New-Lunch-Bot2
// */
//public class RegisterUserService {
//    AdminService adminService = new AdminService();
//
//
//    public void registerUser(User currentUser, Update update, UserActivity userActivity , User newUser) {
//        switch (userActivity.getRound()) {
//            case 10:
//                getPhoneNumber(currentUser, update, userActivity, newUser);
//                break;
//            case 11:
//                getFullName(currentUser, update, userActivity, newUser);
//                break;
//            case 12:
//                getDepartment(currentUser, update, userActivity, newUser);
//                break;
//            case 13:
//                getPosition(currentUser, update, userActivity, newUser);
//                break;
//        }
//    }
//
//    private void getPosition(User currentUser, Update update, UserActivity userActivity, User newUser) {
//        if (update.hasMessage() && update.getMessage().hasText()) {
//            newUser.setDepartment(update.getMessage().getText());
//            userActivity.setRound(0);
//            currentUser.setRound(50);
//            DB.addUser(newUser);
//            sendMessageUser(currentUser, "Yangi user databasega qo'shildi", false, userActivity , update);
//
//            adminService.adminServiceMainMethod(currentUser , update , userActivity);
//        }
//    }
//
//    private void getDepartment(User currentUser, Update update, UserActivity userActivity, User newUser) {
//        if (update.hasMessage() && update.getMessage().hasText()) {
//            newUser.setFullName(update.getMessage().getText());
//            userActivity.setRound(13);
//            sendMessageUser(currentUser, "Lavozimni tanla:", true , userActivity , update);
//        }
//    }
//
//    private void getPhoneNumber(User currentUser, Update update, UserActivity userActivity, User newUser) {
//        if (update.hasMessage() && update.getMessage().hasText()) {
//            newUser.setPhoneNumber(update.getMessage().getText());
//            userActivity.setRound(11);
//            sendMessageUser(currentUser, "Ism familiyani jo'nat:", false,userActivity,update);
//        }
//    }
//
//    public void getFullName(User currentUser, Update update, UserActivity userActivity, User newUser) {
//        if (update.hasMessage() && update.getMessage().hasText()) {
//            newUser.setFullName(update.getMessage().getText());
//            userActivity.setRound(12);
//            sendMessageUser(currentUser, "Bo'limini tanla:", true, userActivity , update);
//        }
//    }
//
//    public void sendMessageUser(User currentUser, String text, boolean is_replyMarkUp, UserActivity userActivity, Update update) {
//        LunchBot myBot = new LunchBot();
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setText(text);
//        sendMessage.setChatId(currentUser.getChatId());
//        if (is_replyMarkUp) {
//            sendMessage.setReplyMarkup(getReplyKeyBoard(currentUser, userActivity, update));
//        }
//        try {
//            myBot.execute(sendMessage);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private ReplyKeyboard getReplyKeyBoard(User currentUser, UserActivity userActivity, Update update) {
//        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
//        keyboardMarkup.setResizeKeyboard(true);
//        keyboardMarkup.setOneTimeKeyboard(true);
//
//        List<KeyboardRow> rowList = new ArrayList<>();
//        keyboardMarkup.setKeyboard(rowList);
//        KeyboardRow row1 = new KeyboardRow();
//        KeyboardRow row2 = new KeyboardRow();
//        KeyboardRow row3 = new KeyboardRow();
//        KeyboardRow rowN = new KeyboardRow();
//
//        int round = userActivity.getRound();
//
//        switch (round) {
//            case 12:
//                row1.add("Support");
//                row1.add("Marketing");
//                row2.add("ECMA");
//                row2.add("Unicorn");
//                row3.add("Sales");
//                row3.add("Reception");
//                rowN.add("Va Boshqalar");
//                rowList.add(row1);
//                rowList.add(row2);
//                rowList.add(row3);
//                rowList.add(rowN);
//                break;
//            case 13:
//                row1.add("Mentor");
//                row1.add("Assistant");
//                row1.add("Marketing Manager");
//                row2.add("Sales Manager");
//                row2.add("CEO");// todo Lavozimlarni to'ldirish kerak
//                row2.add("ECMA");
//                row3.add("Buxgalter");
//                row3.add("AXO");
//                row3.add("Unicorn Manager");
//                rowN.add("Va Boshqalar");
//                rowList.add(row1);
//                rowList.add(row2);
//                rowList.add(row3);
//                rowList.add(rowN);
//                break;
//        }
//        // bu joyda roundga 5 yoki 5 dan kichik bo'lsa user positioniga qarab menu buttonlar chiqayotgandi.
//        return keyboardMarkup;
//    }
//
//}
