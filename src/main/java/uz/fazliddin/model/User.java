package uz.fazliddin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Fazliddin Xamdamov
 * @date 01.03.2022  16:04
 * @project New-Lunch-Bot2
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    private Integer id;
    private String fullName;
    private String chatId;
    private String phoneNumber;
    private String  userStatus = "USER";
    private boolean isRegister = false;
    private String department; // bo'limi
    private String position;  // lavozimi
    private Integer round = 0;
}
