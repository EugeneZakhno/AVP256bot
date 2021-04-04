package io.github.eugenezakhno.telegrambotspringboot.hendler;


import io.github.eugenezakhno.telegrambotspringboot.model.telegram.TelegramUpdate;

public interface TelegramMessageHandler {
    void handle(TelegramUpdate telegramUpdate);
}
