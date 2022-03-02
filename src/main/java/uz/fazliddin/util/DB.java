package uz.fazliddin.util;

import uz.fazliddin.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Fazliddin Xamdamov
 * @date 01.03.2022  21:48
 * @project New-Lunch-Bot2
 */
public class DB {

    public static List<User> allUser(){
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

    public static void addUser(User user){
        Connection connection = DbConnect.getConnection();
        try {
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement("insert into users (full_name, chat_id, phone_number, user_status, is_register , department , position , round )" + " values (?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, user.getFullName());
            preparedStatement.setString(2, user.getChatId());
            preparedStatement.setString(3, user.getPhoneNumber());
            preparedStatement.setString(4,user.getUserStatus());
            preparedStatement.setBoolean(5, user.isRegister());
            preparedStatement.setString(6, user.getDepartment());
            preparedStatement.setString(7, user.getPosition());
            preparedStatement.setInt(8,user.getRound());

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
