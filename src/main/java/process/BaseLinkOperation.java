package process;

import data.LocalStoreData;

import java.util.UUID;

public class BaseLinkOperation {
    final UUID uuid;
    final LocalStoreData link;

    public BaseLinkOperation(UUID uuid, LocalStoreData link) {
        this.uuid = uuid;
        this.link = link;
    }
}
