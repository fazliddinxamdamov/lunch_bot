package uz.fazliddin;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.fazliddin.model.User;
import uz.fazliddin.model.UserActivity;
import uz.fazliddin.service.HelperMethods;
import uz.fazliddin.service.RegistrationService;
import uz.fazliddin.util.DataBase;

import static uz.fazliddin.ignore.Constants.*;
/**
 * @author Fazliddin Xamdamov
 * @date 01.03.2022  16:04
 * @project New-Lunch-Bot2
 */
public class LunchBot extends TelegramLongPollingBot {

//    RoundMethods roundMethods = new RoundMethods();
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

        if (currentUser.getPhoneNumber() != null && update.getMessage().hasText() && update.getMessage().getText().equals("/start")){
            userActivity.setRound(5);
            helperMethods.sendMessage(currentUser, "Asosiy Menu" , true);
            userActivity.setRound(6);
        }

        if (userActivity.getRound() < 5){
            registrationService.startRegistration(currentUser , update , userActivity);
        }

//        switch (userActivity.getRound()){
//            case 0:
//                roundMethods.registerProcess(currentUser, update);
//                break;
//            case 1:
//                roundMethods.confirmContact(currentUser, update);
//                break;
//            case 2:
//                roundMethods.selectFromMainMenu(currentUser, update);
//                break;
//            case 3:
//                roundMethods.selectingCategory(currentUser, update);
//                break;
//            case 4:
//                roundMethods.selectingBook(currentUser, update);
//                break;
//            case 5:
//                roundMethods.deciding(currentUser, update); //qaror qabul qilyabdi, sotib olaymi yoki yuqmi
//                break;
//            case 6:
//                roundMethods.selectingPayType(currentUser, update);
//                break;
//            case 7:
//                roundMethods.deciding2(currentUser, update); //qaror qabul qilyabdi, sotib olaymi yoki yuqmi pul to'lash jarayoni!
//                break;
//            case 8:
//                roundMethods.confirmLocation(currentUser, update); //qaror qabul qilyabdi, sotib olaymi yoki yuqmi pul to'lash jarayoni!
//                break;
//        }


    }
}
