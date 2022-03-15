package uz.fazliddin;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
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

    HelperMethods helperMethods = new HelperMethods();
    RegistrationService registrationService = new RegistrationService();

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
                    hrService.hrServiceMainMethod(currentUser , update , userActivityForUser , userFoodHr);
                    break;
                case "ADMIN":
                    AdminService adminService = new AdminService();
                    UserFood userFoodAdmin = new UserFood();
                    adminService.adminServiceMainMethod(currentUser , update , userActivityForUser , userFoodAdmin);
                    break;
            }
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
     }
}
