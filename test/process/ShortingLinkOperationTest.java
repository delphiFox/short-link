package process;

import data.LocalStoreData;
import data.Store;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ShortingLinkOperationTest {
    @Test
    void encrypt_randomUuid() {
        new ShortingLinkOperation("https://mail.ru 3").runAction();

        Map<UUID, List<LocalStoreData>> storeCopy = Store.getCopy();

        assertFalse(storeCopy.isEmpty());

        for (var uuid : storeCopy.keySet()) {
            var links = storeCopy.get(uuid);
            for (var link : links) {
                if (link.getUrl().contains("mail")) {
                    assertEquals("https://mail.ru", link.getUrl());
                    assertEquals(3, link.getLimitRestriction());
                    assertEquals(0, link.getLimitCurrent());
                    return;
                }
            }
        }
    }
}