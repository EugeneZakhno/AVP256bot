package io.github.eugenezakhno.telegrambotspringboot.service;


import io.github.eugenezakhno.telegrambotspringboot.model.telegram.TelegramChat;
import io.github.eugenezakhno.telegrambotspringboot.model.telegram.TelegramMessage;
import io.github.eugenezakhno.telegrambotspringboot.model.telegram.TelegramUpdate;
import io.github.eugenezakhno.telegrambotspringboot.model.telegram.TelegramUser;
import io.github.eugenezakhno.telegrambotspringboot.repository.telegram.TelegramChatRepository;
import io.github.eugenezakhno.telegrambotspringboot.repository.telegram.TelegramMessageRepository;
import io.github.eugenezakhno.telegrambotspringboot.repository.telegram.TelegramUpdateRepository;
import io.github.eugenezakhno.telegrambotspringboot.repository.telegram.TelegramUserRepository;
import io.github.eugenezakhno.telegrambotspringboot.transformer.Transformer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TelegramUpdateService {
    Transformer<Update, TelegramUpdate> updateToTelegramUpdateTransformer;
    Transformer<Message, TelegramMessage> messageToTelegramMessageTransformer;
    Transformer<User, TelegramUser> userToTelegramUserTransformer;
    Transformer<Chat, TelegramChat> chatToTelegramChatTransformer;
    TelegramUpdateRepository telegramUpdateRepository;
    TelegramMessageRepository telegramMessageRepository;
    TelegramUserRepository telegramUserRepository;
    TelegramChatRepository telegramChatRepository;

    public TelegramUpdate save(Update update) {
        TelegramUser telegramUser = telegramUserRepository.findById(update.getMessage().getFrom().getId())
                .orElseGet(() ->
                        telegramUserRepository.save(
                                userToTelegramUserTransformer.transform(update.getMessage().getFrom())
                        )
                );

        TelegramChat telegramChat = telegramChatRepository.findById(update.getMessage().getChat().getId())
                .orElseGet(() ->
                        {
                            TelegramChat transformedChat = chatToTelegramChatTransformer.transform(update.getMessage().getChat());
                            transformedChat.setUser(telegramUser);
                            return telegramChatRepository.save(transformedChat);
                        }
                );

        TelegramMessage telegramMessage = messageToTelegramMessageTransformer.transform(update.getMessage());
        telegramMessage.setFrom(telegramUser);
        telegramMessage.setChat(telegramChat);
        TelegramMessage savedTelegramMessage = telegramMessageRepository.save(telegramMessage);

        TelegramUpdate telegramUpdate = updateToTelegramUpdateTransformer.transform(update);
        telegramUpdate.setMessage(savedTelegramMessage);
        return telegramUpdateRepository.save(telegramUpdate);
    }
}
