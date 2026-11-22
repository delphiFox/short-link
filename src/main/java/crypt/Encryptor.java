package crypt;

import data.Store;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Вычисляем SHA-256 от входной строки
 * Кодируем дайджест в символы заданного алфавита, получаем первые X символов
 * Если полученный код совпал с существующим (коллизия), добавляем #+i и кодируем снова
 */

public class Encryptor {
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String encrypt(String stringEncoding) {
        for (int i = 0; i <= 20; i++) {
            if (i > 0)
                stringEncoding = stringEncoding.concat("#") + i;
            byte[] sha256FromUrl = hash(stringEncoding);
            String code = Store.getLinkPrefix() + encode(sha256FromUrl).substring(0, Store.getLinkLength());

            if (!Store.shortLinkExist(code))
                return code;
        }

        throw new IllegalStateException("Не удалось сгенерировать уникальный код для ссылки: %s".formatted(stringEncoding));
    }

    private static byte[] hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(input.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Не верно указан алгоритм", e);
        }
    }

    private static String encode(byte[] bytes) {
        var value = new BigInteger(1, bytes);
        var base = BigInteger.valueOf(ALPHABET.length());

        var sb = new StringBuilder();
        while (value.signum() > 0) {
            BigInteger[] divideAndRemainder = value.divideAndRemainder(base);
            sb.append(ALPHABET.charAt(divideAndRemainder[1].intValue()));
            value = divideAndRemainder[0];
        }

        return sb.reverse().toString();
    }
}
