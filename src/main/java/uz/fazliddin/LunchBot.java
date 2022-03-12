package uz.fazliddin;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberMember;
import uz.fazliddin.model.Food;
import uz.fazliddin.model.User;
import uz.fazliddin.model.UserActivity;
import uz.fazliddin.model.UserFood;
import uz.fazliddin.service.*;
import uz.fazliddin.util.DataBase;

import static uz.fazliddin.util.Constants.TOKEN;
import static uz.fazliddin.util.Constants.USERNAME;

/**
 * @author Fazliddin Xamdamov
 * @date 01.03.2022  16:04
 * @project New-Lunch-Bot2
 */
public class LunchBot extends TelegramLongPollingBot {

    //    RoundMethods roundMethods = new RoundMethods();
    HelperMethods helperMethods = new HelperMethods();
    RegistrationService registrationService = new RegistrationService();
//    UserService userService = new UserService();
//    HrService hrService = new HrService();
//    AdminService adminService = new AdminService();

    @Override
    public String getBotUsername() {
        return USERNAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        User currentUser = DataBase.getUserFromList(update);
        UserActivity userActivity = DataBase.userActivityMap.get(currentUser.getChatId());
        UserActivity userActivityForUser = new UserActivity();// boshlanishi 0 dan boshlanadi
//        userActivityForUser.setChoice("ovqat");

        if (currentUser.isRegister()) {
            switch (currentUser.getUserStatus()) {
                case "USER":
                    UserService userService = new UserService();
                    UserFood userFood = new UserFood();
                    userService.userServiceMainMethod(currentUser, update, userActivityForUser , userFood );
                    break;
                case "HR":
                    //  todo hr serviceni qilish kerak
                    HrService hrService = new HrService();
                    UserFood userFoodHr = new UserFood();
                    hrService.hrServiceMainMethod(currentUser , update , userActivity , userFoodHr);
                    break;
                case "ADMIN":
                    AdminService adminService = new AdminService();
                    UserFood userFoodAdmin = new UserFood();
                    adminService.adminServiceMainMethod(currentUser , update , userActivityForUser , userFoodAdmin);
                    break;
            }
//            helperMethods.sendMessage(currentUser, "Asosiy Menu", true);
        } else {
            if (currentUser.getPhoneNumber() != null && update.getMessage().hasText() && update.getMessage().getText().equals("/start")) {
                userActivity.setRound(5);
                helperMethods.sendMessage(currentUser, "Asosiy Menu", true);
                userActivity.setRound(6);
            }

            if (userActivity.getRound() < 5) {
                registrationService.startRegistration(currentUser, update, userActivity);
            }
        }
        // user bo'lsa
//        if (currentUser.isRegister()) {
//            if (currentUser.getUserStatus().equals("USER")) {
//                userActivity.setRound(0);
//                userService.userServiceMainMethod(currentUser, update, userActivity);
//            } else if (currentUser.getUserStatus().equals("ADMIN")) {
//                UserActivity userActivityAdmin = new UserActivity();
//                userActivityAdmin.setRound(0);
//                adminService.adminServiceMainMethod(currentUser,update,userActivityAdmin);
//            } else if (currentUser.getUserStatus().equals("HR")){
//                UserActivity userActivityAdmin = new UserActivity();
//                userActivityAdmin.setRound(0);
//                hrService.adminServiceMainMethod(currentUser,update,userActivityAdmin);
//            }
//        }
     }
}
