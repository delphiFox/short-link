package process;

import data.LocalStoreData;
import data.Store;
import exception.OperationException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EditDataOperationTest {
    @Test
    void runAction_whenUuidNotFound_whenException() {
        var test = new EditDataOperation("290d3483-3b84-49a5-ace5-4b6843f66a03");
        assertThrows(OperationException.class, test::runAction);
    }

    @Test
    void runAction_linkForAdded_whenLinkDuplicated_thenNoAdded() throws OperationException {
        var testLink = new LocalStoreData("http://demchishin.com", "demch.ru/dgh34h", 3, 0, LocalDateTime.now());
        Store.post(UUID.fromString("290d3483-3b84-49a5-ace5-4b6843f66a04"), testLink);
        List<LocalStoreData> links = Store.getUser(UUID.fromString("290d3483-3b84-49a5-ace5-4b6843f66a04"));
        assertTrue(links.contains(testLink));

        var testIn = new ByteArrayInputStream("http://demchishin.com".getBytes());
        System.setIn(testIn);
        new EditDataOperation("290d3483-3b84-49a5-ace5-4b6843f66a04").runAction();
        links = Store.getUser(UUID.fromString("290d3483-3b84-49a5-ace5-4b6843f66a04"));
        assertEquals(1, links.size());
    }

    @Test
    void runAction_linkForAdded_whenLinkNotCheck_whenException() throws OperationException {
        var testLink = new LocalStoreData("http://demchishin.com", "demch.ru/dgh34h", 3, 0, LocalDateTime.now());
        Store.post(UUID.fromString("290d3483-3b84-49a5-ace5-4b6843f66a05"), testLink);

        var testIn = new ByteArrayInputStream("http://demchishin".getBytes());
        System.setIn(testIn);
        assertThrows(OperationException.class, () -> new EditDataOperation("290d3483-3b84-49a5-ace5-4b6843f66a05").runAction());
    }
}