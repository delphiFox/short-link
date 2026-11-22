package data;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StoreTest {
    @Test
    void shortLinkExist_whenLinkExist_thenTrue() {
        final UUID testUuid = UUID.randomUUID();
        var testLink = new LocalStoreData("https://xxx.com", "demch.ru/dgh3xx", 3, 0, LocalDateTime.now());
        Store.post(testUuid, testLink);

        boolean r = Store.shortLinkExist("demch.ru/dgh3xx");
        assertTrue(r);
    }

    @Test
    void shortLinkExist_whenLinkNotExist_thenFalse() {
        boolean r = Store.shortLinkExist("Ссылки нет");
        assertFalse(r);
    }

    @Test
    void incrementCurrentLimit() {
        final UUID testUuid = UUID.randomUUID();
        var testLink = new LocalStoreData("https://aaa.com", "demch.ru/dgh34h", 3, 0, LocalDateTime.now());
        Store.post(testUuid, testLink);

        var links = Store.getUser(testUuid);
        assertEquals("https://aaa.com", links.getFirst().getUrl());
        assertEquals(0, links.getFirst().getLimitCurrent());

        Store.incrementCurrentLimit(testUuid, testLink);
        links = Store.getUser(testUuid);
        assertEquals("https://aaa.com", links.getFirst().getUrl());
        assertEquals(1, links.getFirst().getLimitCurrent());
    }
}