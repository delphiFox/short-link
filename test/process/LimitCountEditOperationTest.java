package process;

import data.LocalStoreData;
import data.Store;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LimitCountEditOperationTest {
    @Test
    void limitCountEdit() {
        final UUID testUuid = UUID.randomUUID();
        var testLink = new LocalStoreData("https://aaa.com", "demch.ru/dgh34h", 3, 0, LocalDateTime.now());
        Store.post(testUuid, testLink);

        var t = new LimitCountEditOperation(testUuid, testLink, "2 7");
        t.run();

        List<LocalStoreData> links = Store.getUser(testUuid);
        assertEquals(7, links.getFirst().getLimitRestriction());
    }
}