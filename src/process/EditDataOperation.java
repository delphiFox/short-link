package process;

import data.LocalStoreData;
import data.Store;
import exception.OperationException;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

import static java.util.Objects.isNull;
import static process.Strategy.*;

/**
 * Класс, содержащий логику редактирования данных пользователя
 */

public class EditDataOperation implements RootOperational {
    private final UUID uuid;

    public EditDataOperation(String message) {
        this.uuid = UUID.fromString(message.strip());
    }

    @Override
    public void runAction() throws OperationException {
        List<LocalStoreData> userLinkData = Store.getUser(this.uuid);
        if (isNull(userLinkData))
            throw new OperationException("Пользователь %s ссылки не создавал".formatted(this.uuid));

        System.out.printf("Ссылки пользователя %s:%n", uuid);
        userLinkData.forEach(u -> System.out.println("- " + u.getUrl() + " | " + u.getShortUrl() + " | Ограничение переходов: " + u.getLimitRestriction()));

        System.out.print("Введите ссылку для добавления, короткую ссылку для перехода по ней или кодированное значение короткой ссылки для её редактирования (символы после '/'): ");

        try (var sc = new Scanner(System.in)) {
            if (sc.hasNext()) {
                String value = sc.nextLine();
                if (linkForAdded(value)) {
                    boolean validateSuccess = RootOperational.messageIsLink(value);
                    if (!validateSuccess)
                        throw new OperationException("Не удалось обработать ссылку для добавления");
                    RootOperational createShortLink = new ShortingLinkOperation(value, this.uuid);
                    createShortLink.runAction();
                } else if (linkForClick(value)) {
                    Optional<LocalStoreData> link = userLinkData.stream().filter(v -> v.getShortUrl().equals(value)).findFirst();
                    link.ifPresentOrElse(l -> {
                        try {
                            if (l.getLimitRestriction() > l.getLimitCurrent()) {
                                Desktop.getDesktop().browse(new URI(l.getUrl()));
                                Store.incrementCurrentLimit(this.uuid, l);
                            } else {
                                System.out.printf("Для ссылки %s достигнут предельный лимит переходов", l.getShortUrl());
                            }
                        } catch (URISyntaxException | IOException e) {
                            System.err.println("Ошибка обращения к браузеру: " + e);
                        }
                    }, () -> System.out.println("По короткой ссылке исходная ссылка не найдена или была удалена по истечении срока давности"));
                } else if (linkForEdit()) {
                    Optional<LocalStoreData> link = userLinkData.stream().filter(v -> {
                        int index = v.getShortUrl().indexOf("/");
                        return v.getShortUrl().substring(index + 1).equals(value);
                    }).findFirst();
                    link.ifPresentOrElse(l -> {
                        l.printEditData();
                        if (sc.hasNext()) {
                            String editValue = sc.nextLine();
                            try {
                                Strategy.runEdit(this.uuid, l, editValue).run();
                            } catch (OperationException e) {
                                System.err.println("Ошибка при редактировании ссылки: " + e.getMessage());
                            }
                        }
                    }, () -> System.out.println("По введенному кодированному значению ссылка не найдена - редактирование невозможно"));
                }
            }
        }
    }
}
