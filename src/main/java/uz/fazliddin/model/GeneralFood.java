package uz.fazliddin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class GeneralFood {
    private Integer id;
    private String name;
}
