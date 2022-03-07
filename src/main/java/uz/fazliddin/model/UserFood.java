package uz.fazliddin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author Fazliddin Xamdamov
 * @date 03.03.2022  17:56
 * @project New-Lunch-Bot2
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserFood {
    private Integer id;
    private Integer userId;
    private Integer foodId;
    private LocalDateTime kuni;
//    private String lunchTime;    // har doim ovqatlanadigan vaqti.
}
