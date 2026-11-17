package process;

import data.LocalStoreData;
import data.Store;

import java.util.UUID;

public class LimitCountEditOperation extends BaseLinkOperation implements Editable {
    private final int newLimit;

    public LimitCountEditOperation(UUID uuid, LocalStoreData link, String message) {
        super(uuid, link);
        String[] messageArray = Helper.split(message);
        this.newLimit = Integer.parseInt(messageArray[1].strip());
    }

    @Override
    public void run() {
        Store.changeRestrictionLimit(this.uuid, this.link, newLimit);
    }
}
