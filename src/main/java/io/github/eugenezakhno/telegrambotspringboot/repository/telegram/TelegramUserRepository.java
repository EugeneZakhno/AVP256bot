package io.github.eugenezakhno.telegrambotspringboot.repository.telegram;


import io.github.eugenezakhno.telegrambotspringboot.model.telegram.TelegramUser;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "telegram_users", path = "users")
public interface TelegramUserRepository extends PagingAndSortingRepository<TelegramUser, Integer> {
}
