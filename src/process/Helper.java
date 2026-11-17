package process;

import java.net.URI;
import java.net.URISyntaxException;

import static java.util.Objects.isNull;

public interface Helper {
    static String[] split(String message) {
        return message.split(" ", 2);
    }

    static boolean isValidLink(String link) {
        try {
            var uri = new URI(link);

            if (isNull(uri.getScheme()) || !uri.getScheme().matches("^(http|https)$"))
                return false;

            if (isNull(uri.getAuthority()))
                return false;

            if (link.indexOf(".") <= 0)
                return false;

            return uri.isAbsolute();
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
