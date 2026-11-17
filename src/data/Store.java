package data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Store {
    private static String linkPrefix;
    private static int linkLength;
    private static int limitRestriction;
    private static int limitLifetimeMinute;

    private static final Map<UUID, List<LocalStoreData>> localStore = new HashMap<>();

    public static List<LocalStoreData> getUser(UUID uuid) {
        return localStore.get(uuid);
    }

    public static Map<UUID, List<LocalStoreData>> getCopy() {
        return Map.copyOf(localStore);
    }

    public static void post(UUID uuid, LocalStoreData localStoreData) {
        localStore.put(uuid, List.of(localStoreData));
        updateFileStore();
        localStoreData.printNewLinkData(uuid);
    }

    public static void addLink(UUID uuid, LocalStoreData localStoreData) {
        List<LocalStoreData> user = getUser(uuid);
        for (var u : user) {
            if (u.getUrl().equalsIgnoreCase(localStoreData.getUrl())) {
                System.out.printf("У пользователя уже существует короткая ссылка для %s%n", localStoreData.getUrl());
                return;
            }
        }
        user.add(localStoreData);
        updateFileStore();
        localStoreData.printNewLinkData(uuid);
    }

    private static void updateFileStore() {
        var root = new JSONArray();

        localStore.forEach((uuid, arrayLink) -> {
            var user = new JSONObject();
            user.put("uuid", uuid);

            var a = new JSONArray();
            arrayLink.forEach(e -> {
                var link = new JSONObject();
                link.put("url", e.getUrl());
                link.put("shortUrl", e.getShortUrl());
                link.put("limitRestriction", e.getLimitRestriction());
                link.put("limitCurrent", e.getLimitCurrent());
                link.put("creationTime", e.getCreationTime());
                a.put(link);
            });
            user.put("linkData", a);

            root.put(user);
        });

        writeFileStore(root.toString());
    }

    public static boolean shortLinkExist(String shortLintExample) {
        for (var v : localStore.values()) {
            for (var subV : v) {
                if (subV.getShortUrl().equals(shortLintExample))
                    return true;
            }
        }
        return false;
    }

    public static void initLocalStore() {
        var rootArray = new JSONArray(readFileStore());

        for (var e : rootArray) {
            var o = (JSONObject) e;
            String uuidString = o.getString("uuid");

            var storeDataList = new ArrayList<LocalStoreData>();
            JSONArray linkArray = o.getJSONArray("linkData");
            for (var link : linkArray) {
                var linkObject = (JSONObject) link;
                String url = linkObject.getString("url");
                String shortUrl = linkObject.getString("shortUrl");
                int limitRestriction = linkObject.getInt("limitRestriction");
                int limitCurrent = linkObject.getInt("limitCurrent");
                LocalDateTime creationTime = LocalDateTime.parse(linkObject.getString("creationTime"));
                storeDataList.add(new LocalStoreData(url, shortUrl, limitRestriction, limitCurrent, creationTime));
            }

            localStore.put(UUID.fromString(uuidString), storeDataList);
        }
    }

    public static void initParams() {
        try {
            Files.readAllLines(Paths.get("src/config/param.txt"), StandardCharsets.UTF_8).forEach(e -> {
                String[] param = e.split("=");
                if ("link_prefix".equals(param[0]))
                    linkPrefix = param[1];
                else if ("link_length".equals(param[0]))
                    linkLength = Integer.parseInt(param[1]);
                else if ("limit_restriction".equals(param[0]))
                    limitRestriction = Integer.parseInt(param[1]);
                else if ("limit_lifetime_minute".equals(param[0]))
                    limitLifetimeMinute = Integer.parseInt(param[1]);
            });
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    private static String readFileStore() {
        try {
            return Files.readString(Paths.get("src/data/file_store.json"));
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    private static void writeFileStore(String str) {
        try {
            Files.writeString(Paths.get("src/data/file_store.json"), str);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    public static void incrementCurrentLimit(UUID uuid, LocalStoreData link) {
        List<LocalStoreData> user = getUser(uuid);
        for (var l : user) {
            if (l.equals(link)) {
                l.incrementLimitCurrent();
                break;
            }
        }
        updateFileStore();
    }

    public static String getLinkPrefix() {
        return linkPrefix;
    }

    public static int getLinkLength() {
        return linkLength;
    }

    public static int getLimitRestriction() {
        return limitRestriction;
    }

    public static void deleteLinkByInterval() {
        boolean updateFileStoreNeedFlag = false;

        for (var map : localStore.entrySet()) {
            UUID user = map.getKey();
            var it = map.getValue().iterator();

            while (it.hasNext()) {
                LocalStoreData link = it.next();
                long lifeTimeMinute = Duration.between(link.getCreationTime(), LocalDateTime.now()).toMinutes();
                if (lifeTimeMinute > limitLifetimeMinute) {
                    it.remove();
                    System.out.println("У пользователя " + user + " удалена ссылка " + link.getUrl() + " по истечении времени жизни");
                    updateFileStoreNeedFlag = true;
                }
            }
        }

        if (updateFileStoreNeedFlag)
            updateFileStore();
    }

    public static void changeLink(UUID uuid, LocalStoreData link, String newUrl) {
        List<LocalStoreData> user = getUser(uuid);
        for (var l : user) {
            if (l.equals(link)) {
                l.setUrl(newUrl);
                break;
            }
        }
        updateFileStore();
        System.out.println("Ссылка изменена");
    }

    public static void changeRestrictionLimit(UUID uuid, LocalStoreData link, int newLimit) {
        List<LocalStoreData> user = getUser(uuid);
        for (var l : user) {
            if (l.equals(link)) {
                l.setLimitRestriction(newLimit);
                break;
            }
        }
        updateFileStore();
        System.out.println("Значение ограничения переходов изменено");
    }

    public static void deleteLink(UUID uuid, LocalStoreData link) {
        List<LocalStoreData> user = getUser(uuid);
        user.removeIf(l -> l.equals(link));
        updateFileStore();
        System.out.println("Ссылка удалена");
    }
}
