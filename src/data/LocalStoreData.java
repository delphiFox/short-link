package data;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class LocalStoreData {
    private String url;
    private final String shortUrl;
    private int limitRestriction;
    private int limitCurrent;
    private final LocalDateTime creationTime;

    public LocalStoreData(String url, String shortUrl, int limitRestriction, int limitCurrent, LocalDateTime creationTime) {
        this.url = url;
        this.shortUrl = shortUrl;
        this.limitRestriction = limitRestriction;
        this.limitCurrent = limitCurrent;
        this.creationTime = creationTime;
    }

    public String getUrl() {
        return url;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public int getLimitRestriction() {
        return limitRestriction;
    }

    public int getLimitCurrent() {
        return limitCurrent;
    }

    public void incrementLimitCurrent() {
        this.limitCurrent++;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setLimitRestriction(int limit) {
        this.limitRestriction = limit;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void printNewLinkData(UUID uuid) {
        System.out.println("Создана/добавлена новая ссылка: " + uuid + " | " + url + " | " + shortUrl + " | макс. количество переходов: " + limitRestriction);
    }

    public void printEditData() {
        System.out.println("Редактирование: " + url + " | " + shortUrl + " | макс. количество переходов: " + limitRestriction);
        System.out.println("Введите [1] и новое наименование длинной ссылки (пример: 1 наименование)");
        System.out.println("Введите [2] и новое значение количества переходов (пример: 2 число)");
        System.out.println("Введите [3] и подтверждение удаления ссылки (пример: 3 del)");
        System.out.print("Введите значение: ");
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        LocalStoreData that = (LocalStoreData) o;
        return Objects.equals(url, that.url) && Objects.equals(shortUrl, that.shortUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, shortUrl);
    }
}
