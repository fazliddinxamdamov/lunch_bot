package uz.fazliddin;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.fazliddin.model.User;
import uz.fazliddin.model.UserActivity;
import uz.fazliddin.model.UserFood;
import uz.fazliddin.service.AdminService;
import uz.fazliddin.service.HelperMethods;
import uz.fazliddin.service.HrService;
import uz.fazliddin.service.UserService;
import uz.fazliddin.util.DB;
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
//    RegistrationService registrationService = new RegistrationService();

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
        User currentUser = DB.getUserFromList(update);
        UserActivity userActivity = DataBase.userActivityMap.get(currentUser.getChatId());
        UserActivity userActivityForUser = new UserActivity();
        UserActivity userActivityForAdmin = new UserActivity();

        if (currentUser.isRegister()) {
            switch (currentUser.getUserStatus()) {
                case "USER":
                    UserService userService = new UserService();
                    UserFood userFood = new UserFood();
                    userService.userServiceMainMethod(currentUser, update, userActivityForUser , userFood );
                    break;
                case "HR":
                    HrService hrService = new HrService();
                    UserFood userFoodHr = new UserFood();
                    hrService.hrServiceMainMethod(currentUser , update , userActivityForUser , userFoodHr);
                    break;
                case "ADMIN":
                    AdminService adminService = new AdminService();
                    adminService.adminServiceMainMethod(currentUser , update , userActivityForAdmin);
                    break;
            }
        } else {
            if (currentUser.getPhoneNumber() != null && update.getMessage().hasText() && update.getMessage().getText().equals("/start")) {
                userActivity.setRound(5);
                helperMethods.sendMessage(currentUser, "Asosiy Menu", true);
                userActivity.setRound(6);
            }

            if (userActivity.getRound() < 5) {
                helperMethods.sendMessage(currentUser, "Botdan foydlananishingiz uchun omborga avvaldan qo'shilgan bo'lishingiz kerak.", false);
                // bu joyda restratsiya qismini yopib qo'ydim , ECMA dagila kerakmas diyishgandi..
//                registrationService.startRegistration(currentUser, update, userActivity);
            }
        }
     }
}
