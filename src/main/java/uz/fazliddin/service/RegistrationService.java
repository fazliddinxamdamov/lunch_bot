package uz.fazliddin.service;

import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.fazliddin.model.User;
import uz.fazliddin.model.UserActivity;
import uz.fazliddin.util.DB;
import uz.fazliddin.util.DataBase;

import java.util.SplittableRandom;

/**
 * @author Fazliddin Xamdamov
 * @date 01.03.2022  16:19
 * @project New-Lunch-Bot2
 */
public class RegistrationService {

    HelperMethods helperMethods = new HelperMethods();

    public void startRegistration(User currentUser, Update update, UserActivity userActivity) {

        switch (userActivity.getRound()) {
            case 0:
                getPhoneNumber(currentUser , update);
                break;
            case 1:
                getFullName(currentUser , update);
                break;
            case 2:
                getDepartment(currentUser , update , userActivity);
                break;
            case 3:
                getPosition(currentUser , update , userActivity);
                break;
            case 4:
                finishRegistration(currentUser , update , userActivity);
                if (currentUser.getUserStatus().equals("USER")) currentUser.setRound(10);
                if (currentUser.getUserStatus().equals("HR")) currentUser.setRound(30);
                if (currentUser.getUserStatus().equals("ADMIN")) currentUser.setRound(60);
                DB.addUser(currentUser);
                DataBase.userActivityMap.put(currentUser.getChatId(), new UserActivity(5));
                break;
        }


    }

    private void finishRegistration(User currentUser, Update update, UserActivity userActivity) {
        if (update.hasMessage() && update.getMessage().hasText()){
            currentUser.setPosition(update.getMessage().getText());
            currentUser.setRegister(true);   // registratsiya tugadi , isRegiter = true;
            userActivity.setRound(5);
            helperMethods.sendMessage(currentUser, "Asosiy menu:" , true);
            userActivity.setRound(6);
        }
    }

    private void getPosition(User currentUser, Update update, UserActivity userActivity) {
        if (update.hasMessage() && update.getMessage().hasText()){
            currentUser.setDepartment(update.getMessage().getText());
            userActivity.setRound(4);
            helperMethods.sendMessage(currentUser, "Lavozimingizni tanlang:" , true);
        }
    }

    private void getDepartment(User currentUser, Update update, UserActivity userActivity) {
        if (update.hasMessage() && update.getMessage().hasText()){
            currentUser.setFullName(update.getMessage().getText());
            userActivity.setRound(3);
            helperMethods.sendMessage(currentUser, "Bo'limingizni tanlang:" , true);
        }
    }


    public void getPhoneNumber(User currentUser, Update update){
        UserActivity userActivity = DataBase.userActivityMap.get(currentUser.getChatId());
        if (currentUser.getPhoneNumber() == null){

            if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().equals("/start")){
                helperMethods.sendMessage(currentUser, "Assalomu Alayakum Botga Hush Kelibsiz! \n" +
                        "Bot Xizmatlaridan foydalanish uchun iltimos telefon raqamingizni jo'nating!" , true);
            }
            userActivity.setRound(1);
        } else {

            if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().equals("/start")){
                userActivity.setRound(5);
                helperMethods.sendMessage(currentUser, "Asosiy Menu" , true);
                userActivity.setRound(6);
            }
        }
    }

    //round 1
    public void getFullName(User currentUser, Update update) {
        UserActivity userActivity = DataBase.userActivityMap.get(currentUser.getChatId());
        if (update.getMessage().hasContact()){
            Contact contact = update.getMessage().getContact();
            currentUser.setPhoneNumber(contact.getPhoneNumber());
        }

        helperMethods.sendMessage(currentUser, "To'liq ismi sharifingizni quyidagi ko'rinishda kiriting\n" +
                "\n" +
                "Ism Familya" , true);
        userActivity.setRound(2);
    }


}
