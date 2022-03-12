package uz.fazliddin;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * @author Fazliddin Xamdamov
 * @date 01.03.2022  17:50
 * @project New-Lunch-Bot2
 */
public class Main {
    public static void main(String[] args) {
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(new LunchBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}