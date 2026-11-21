package process;

import data.LocalStoreData;
import exception.OperationException;

import java.util.UUID;

import static process.Editable.*;
import static process.RootOperational.*;

/**
 * Реализация паттерна Стратегия для процессов
 * создания и редактирования ссылки
 */

public class Strategy {
    public static RootOperational start(String message) throws OperationException {
        if (messageIsLink(message))
            return new ShortingLinkOperation(message);
        else if (messageIsUuid(message))
            return new EditDataOperation(message);

        throw new OperationException("Не удалось обработать сообщение");
    }

    public static boolean linkForAdded(String message) {
        return message.toLowerCase().contains("http");
    }

    public static boolean linkForClick(String message) {
        return message.contains("/");
    }

    public static boolean linkForEdit() {
        return true;
    }

    public static Editable runEdit(UUID uuid, LocalStoreData link, String message) throws OperationException {
        if (messageIsNewLinkName(message))
            return new LinkNameEditOperation(uuid, link, message);
        else if (messageIsNewLimitCount(message))
            return new LimitCountEditOperation(uuid, link, message);
        else if (messageIsDeleteLink(message))
            return new LinkDeleteOperation(uuid, link);

        throw new OperationException("Не удалось обработать сообщение");
    }
}
