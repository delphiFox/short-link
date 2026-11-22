package process;

import data.LocalStoreData;
import data.Store;

import java.util.UUID;

/**
 * При редактировании данных пользователя удаляет ссылку
 */

public class LinkDeleteOperation extends BaseLinkOperation implements Editable {
    public LinkDeleteOperation(UUID uuid, LocalStoreData link) {
        super(uuid, link);
    }

    @Override
    public void run() {
        Store.deleteLink(this.uuid, this.link);
    }
}
