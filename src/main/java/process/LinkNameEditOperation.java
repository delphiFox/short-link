package process;

import data.LocalStoreData;
import data.Store;

import java.util.UUID;

/**
 * При редактировании данных пользователя изменяет наименование длинной ссылки
 */

public class LinkNameEditOperation extends BaseLinkOperation implements Editable {
    private final String newUrl;

    public LinkNameEditOperation(UUID uuid, LocalStoreData link, String message) {
        super(uuid, link);
        String[] messageArray = Helper.split(message);
        this.newUrl = messageArray[1].strip();
    }

    @Override
    public void run() {
        Store.changeLink(this.uuid, this.link, newUrl);
    }
}
