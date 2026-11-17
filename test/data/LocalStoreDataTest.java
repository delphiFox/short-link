package data;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class LocalStoreDataTest {
    @Test
    void printEditData() {
        var t = new LocalStoreData("url", "shortUrl", 3, 1, LocalDateTime.now());
        assertDoesNotThrow(t::printEditData);
    }

    @Test
    void equalsNull() {
        var t = new LocalStoreData("url", "shortUrl", 3, 1, LocalDateTime.now());
        assertNotEquals(null, t);
    }

    @Test
    void equalsOtherClassNull() {
        var t = new LocalStoreData("url", "shortUrl", 3, 1, LocalDateTime.now());
        assertNotEquals("", t);
    }

    @Test
    void equalsOk() {
        var t = new LocalStoreData("url", "shortUrl", 3, 1, LocalDateTime.now());
        var t2 = new LocalStoreData("url", "shortUrl", 3, 1, LocalDateTime.now());
        assertEquals(t2, t);
    }

    @Test
    void hashCodeTest() {
        var t = new LocalStoreData("url", "shortUrl", 3, 1, LocalDateTime.now());
        assertDoesNotThrow(t::hashCode);
    }
}