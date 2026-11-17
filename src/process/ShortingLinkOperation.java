package process;

import crypt.Encryptor;
import data.LocalStoreData;
import data.Store;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.Objects.isNull;

public class ShortingLinkOperation implements RootOperational {
    private final String url;
    private final int limit;
    private UUID uuid;

    public ShortingLinkOperation(String message) {
        String[] messageArray = message.split(" ", 2);
        this.url = messageArray[0].strip();
        if (messageArray.length == 2)
            this.limit = Integer.parseInt(messageArray[1].strip());
        else
            this.limit = Store.getLimitRestriction();
    }

    public ShortingLinkOperation(String message, UUID uuid) {
        this(message);
        this.uuid = uuid;
    }

    @Override
    public void runAction() {
        UUID uuid = isNull(this.uuid) ? UUID.randomUUID() : this.uuid;
        String shortUrl = Encryptor.encrypt(uuid + url);
        var userData = new LocalStoreData(url, shortUrl, limit, 0, LocalDateTime.now());
        if (isNull(this.uuid))
            Store.post(uuid, userData);
        else
            Store.addLink(uuid, userData);
    }
}
