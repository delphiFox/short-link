package process;

import exception.OperationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StrategyTest {
    @Test
    void start_whenLinkValid_thenShortingLinkOperation() throws OperationException {
        var result = Strategy.start("https://ya.ru");
        assertInstanceOf(ShortingLinkOperation.class, result);

        result = Strategy.start("https://ya.ru  8");
        assertInstanceOf(ShortingLinkOperation.class, result);
    }

    @Test
    void start_whenLimitNotValid_thenException() {
        assertThrows(OperationException.class, () -> Strategy.start("https://ya.ru  НЕ_ЧИСЛО"));
    }

    @Test
    void start_whenLinkNotValid_thenException() {
        assertThrows(OperationException.class, () -> Strategy.start("//ya.ru"));
        assertThrows(OperationException.class, () -> Strategy.start("xxxx://ya.ru"));
        assertThrows(OperationException.class, () -> Strategy.start("https:/ya.ru"));
        assertThrows(OperationException.class, () -> Strategy.start("https://ddd"));
        assertThrows(OperationException.class, () -> Strategy.start("https://###"));
    }

    @Test
    void start_whenUuidValid_thenEditDataOperation() throws OperationException {
        var result = Strategy.start("72efac55-08a4-4b77-a558-ba04e69ad3a9");
        assertInstanceOf(EditDataOperation.class, result);
    }
}