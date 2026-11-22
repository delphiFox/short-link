package process;

import data.LocalStoreData;
import data.Store;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LinkNameEditOperationTest {
    @Test
    void linkNameEdit() {
        final UUID testUuid = UUID.fromString("290d3483-3b84-49a5-ace5-4b6843f66a05");

        var testLink = new LocalStoreData("https://aaa.com", "demch.ru/dgh34h", 3, 0, LocalDateTime.now());
        Store.post(testUuid, testLink);

        var t = new LinkNameEditOperation(testUuid, testLink, "1 https://bbb.com");
        t.run();

        List<LocalStoreData> links = Store.getUser(testUuid);
        assertEquals("https://bbb.com", links.getFirst().getUrl());
    }
}