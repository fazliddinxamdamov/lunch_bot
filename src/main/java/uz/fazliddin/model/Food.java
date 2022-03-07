package uz.fazliddin.model;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author Fazliddin Xamdamov
 * @date 01.03.2022  20:50
 * @project New-Lunch-Bot2
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Food {
    private Integer id;
    private String name;
    private LocalDateTime timestamp = LocalDateTime.now();
}
