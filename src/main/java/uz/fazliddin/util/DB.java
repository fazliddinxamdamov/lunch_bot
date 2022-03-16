package uz.fazliddin.util;

import org.telegram.telegrambots.meta.api.objects.Update;
import uz.fazliddin.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Fazliddin Xamdamov
 * @date 01.03.2022  21:48
 * @project New-Lunch-Bot2
 */
public class DB {

    public static Map<String , UserActivity> userActivityMap = new HashMap<>();

    public static User getUserFromList(Update update){
        if(update.hasMessage()){
            for (User user : allUser()) {
                if (user.getChatId().equals(update.getMessage().getChatId().toString())){
                    return user;
                }
            }
        } else if (update.hasCallbackQuery()){
            for (User user : allUser()) {
                if (user.getChatId().equals(update.getCallbackQuery().getMessage().getChatId().toString())){
                    return user;
                }
            }
        }

        User newUser = new User();
        newUser.setFullName(update.getMessage().getFrom().getFirstName());
        newUser.setChatId(update.getMessage().getChatId().toString());

        DataBase.userActivityMap.put(newUser.getChatId(), new UserActivity());

        allUser().add(newUser);
        return newUser;
    }



    // USER

    public static List<User> allUser() {
        Connection connection = DbConnect.getConnection();
        PreparedStatement preparedStatement = null;
        List<User> userPeople = new ArrayList<>();
        try {
            assert connection != null;
            preparedStatement = connection.prepareStatement("select * from users");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                userPeople.add(new User(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getBoolean(6),
                        resultSet.getString(7),
                        resultSet.getString(8),
                        resultSet.getInt(9)
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userPeople;
    }

    public static void addUser(User user) {
        Connection connection = DbConnect.getConnection();
        try {
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement("insert into users (full_name, chat_id, phone_number, user_status, is_register , department , position , round )" + " values (?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, user.getFullName());
            preparedStatement.setString(2, user.getChatId());
            preparedStatement.setString(3, user.getPhoneNumber());
            preparedStatement.setString(4, user.getUserStatus());
            preparedStatement.setBoolean(5, user.isRegister());
            preparedStatement.setString(6, user.getDepartment());
            preparedStatement.setString(7, user.getPosition());
            preparedStatement.setInt(8, user.getRound());

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static User getUserFromId(Integer id) {
        Connection connection = DbConnect.getConnection();
        PreparedStatement preparedStatement = null;
        User user = new User();
        try {
            assert connection != null;
            preparedStatement = connection.prepareStatement("select * from users where id = " + id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                user.setId(resultSet.getInt(1));
                user.setFullName(resultSet.getString(2));
                user.setChatId(resultSet.getString(3));
                user.setPhoneNumber(resultSet.getString(4));
                user.setUserStatus(resultSet.getString(5));
                user.setRegister(resultSet.getBoolean(6));
                user.setDepartment(resultSet.getString(7));
                user.setPosition(resultSet.getString(8));
                user.setRound(resultSet.getInt(9));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static User getUserFromChatId(Integer charId) {
        Connection connection = DbConnect.getConnection();
        PreparedStatement preparedStatement = null;
        User user = new User();
        try {
            assert connection != null;
            preparedStatement = connection.prepareStatement("select * from users where chat_id = " + charId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                user.setId(resultSet.getInt(1));
                user.setFullName(resultSet.getString(2));
                user.setChatId(resultSet.getString(3));
                user.setPhoneNumber(resultSet.getString(4));
                user.setUserStatus(resultSet.getString(5));
                user.setRegister(resultSet.getBoolean(6));
                user.setDepartment(resultSet.getString(7));
                user.setPosition(resultSet.getString(8));
                user.setRound(resultSet.getInt(9));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static User getUserFromPosition(String positionName) {
        Connection connection = DbConnect.getConnection();
        PreparedStatement preparedStatement = null;
        User user = new User();
        try {
            assert connection != null;
            preparedStatement = connection.prepareStatement("select * from users where position = " + "'"+ positionName+"'");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                user.setId(resultSet.getInt(1));
                user.setFullName(resultSet.getString(2));
                user.setChatId(resultSet.getString(3));
                user.setPhoneNumber(resultSet.getString(4));
                user.setUserStatus(resultSet.getString(5));
                user.setRegister(resultSet.getBoolean(6));
                user.setDepartment(resultSet.getString(7));
                user.setPosition(resultSet.getString(8));
                user.setRound(resultSet.getInt(9));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }


    // FOOD

    public static List<Food> footList() {
        Connection connection = DbConnect.getConnection();
        PreparedStatement preparedStatement = null;
        List<Food> foods = new ArrayList<>();
        try {
            assert connection != null;
            LocalDate localDate = LocalDate.now();
            preparedStatement = connection.prepareStatement("select * from foods f where cast(f.kuni as date) = " + " '" + localDate + "'");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                foods.add(new Food(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getObject(3, LocalDateTime.class)
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foods;
    }

    public static void addFoodToToday(Food food){
        Connection connection = DbConnect.getConnection();
        try {
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement("insert into foods( full_name, kuni) values (?,?)");
            preparedStatement.setString(1, food.getName());
            preparedStatement.setObject(2, food.getTimestamp());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Food getFood(Integer id) {
        Connection connection = DbConnect.getConnection();
        PreparedStatement preparedStatement = null;
        Food food = new Food();
        try {
            assert connection != null;
            preparedStatement = connection.prepareStatement("select * from foods where id = " + id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                food.setId(resultSet.getInt(1));
                food.setName(resultSet.getString(2));
                food.setTimestamp(resultSet.getObject(3, LocalDateTime.class));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return food;
    }

    public static List<GeneralFood> generalFoodList(){
        Connection connection = DbConnect.getConnection();
        PreparedStatement preparedStatement = null;
        List<GeneralFood> foods = new ArrayList<>();
        try {
            assert connection != null;
            preparedStatement = connection.prepareStatement("select * from general_foods");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                foods.add(new GeneralFood(
                        resultSet.getInt(1),
                        resultSet.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foods;
    }


    // FOOD USER
    public static void addFoodToUser(UserFood userFood) {
        Connection connection = DbConnect.getConnection();
        try {
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement("insert into user_food( user_full_name,user_position, food_name, kuni) values (?,?,?,?)");
            preparedStatement.setString(1, userFood.getUserFullName());
            preparedStatement.setString(2, userFood.getUserPosition());
            preparedStatement.setString(3, userFood.getFoodName());
            preparedStatement.setObject(4, userFood.getKuni());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<UserFood> userFoodList() {
        Connection connection = DbConnect.getConnection();
        PreparedStatement preparedStatement = null;
        List<UserFood> userFoods = new ArrayList<>();
        try {
            assert connection != null;
            LocalDate localDate = LocalDate.now();
            preparedStatement = connection.prepareStatement("select * from user_food f where cast(f.kuni as date) = " + " '" + localDate + "'");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                userFoods.add(new UserFood(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getObject(5, LocalDateTime.class)
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userFoods;
    }

    public static boolean isAddFoodUser(String userFullName) {
        boolean res = true;
        LocalDate now = LocalDate.now();
        for (UserFood userFood : userFoodList()) {
            if ((userFullName.equals(userFood.getUserFullName())) && userFood.getKuni().getMonthValue() == now.getMonthValue() && userFood.getKuni().getDayOfMonth() == now.getDayOfMonth()) {
                res = false;
                break;
            }
        }
        return res;
    }


    // Hozirgi vaqtgacha bo'lgan odamlar zakas qilgan ovqatlari , tuziga qarab soni

    public static Map<String , Integer> foodQuantity(List<UserFood> userFoodsList){
        Map<String , Integer> map = new TreeMap<>();

        for (UserFood userFood : userFoodsList) {
            Integer older = map.get(userFood.getFoodName());
            if (older == null) older = 0;
            map.put(userFood.getFoodName() , older + 1);
        }
        return map;
    }







}
