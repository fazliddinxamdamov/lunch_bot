package uz.fazliddin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Fazliddin Xamdamov
 * @date 01.03.2022  16:10
 * @project New-Lunch-Bot2
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserActivity {
    private Integer round = 0;
    public void setRound(Integer round) {
        this.round = round;
    }
}
