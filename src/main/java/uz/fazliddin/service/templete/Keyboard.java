package uz.fazliddin.service.templete;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fazliddin Xamdamov
 * @date 04.03.2022  19:06
 * @project New-Lunch-Bot2
 */
public class Keyboard {
    public ReplyKeyboardMarkup makeReplyKeyboard(String[] buttons, boolean oneRow) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        int k = 0;
        int cycle;
        if (oneRow) {
            cycle = buttons.length;
        } else {
            cycle = (buttons.length % 2 == 0) ? buttons.length / 2 : buttons.length / 2 + 1;
        }
        for (int i = 0; i < cycle; i++) {
            KeyboardRow row = new KeyboardRow();
            if (oneRow){
                row.add(new KeyboardButton(buttons[k]));
                k++;
            }else {
                for (int j = 0; j < 2; j++) {
                    row.add(new KeyboardButton(buttons[k]));
                    if (k == buttons.length - 1) {
                        break;
                    }
                    k++;
                }
            }
            keyboardRows.add(row);
        }
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        return replyKeyboardMarkup;
    }
}
