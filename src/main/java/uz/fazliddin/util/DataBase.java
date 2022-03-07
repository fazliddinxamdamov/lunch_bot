package uz.fazliddin.util;

import org.telegram.telegrambots.meta.api.objects.Update;
import uz.fazliddin.model.Food;
import uz.fazliddin.model.User;
import uz.fazliddin.model.UserActivity;

import java.time.LocalDateTime;
import java.util.*;


public class DataBase {
    public static List<Food> foodListFromDB = DB.footList();

    public static List<User> userList = DB.allUser();
    public static Map<String , UserActivity> userActivityMap = new HashMap<>();
    public static User getUserFromList(Update update){
        if(update.hasMessage()){
            for (User user : userList) {
                if (user.getChatId().equals(update.getMessage().getChatId().toString())){
                    return user;
                }
            }
        } else if (update.hasCallbackQuery()){
            for (User user : userList) {
                if (user.getChatId().equals(update.getCallbackQuery().getMessage().getChatId().toString())){
                    return user;
                }
            }
        }

        User newUser = new User();
        newUser.setFullName(update.getMessage().getFrom().getFirstName());
        newUser.setChatId(update.getMessage().getChatId().toString());

        DataBase.userActivityMap.put(newUser.getChatId(), new UserActivity());

        userList.add(newUser);
        return newUser;
    }

}
