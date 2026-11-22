package process;

import exception.OperationException;

import java.util.UUID;

public interface RootOperational extends Helper {
    int URL_INDEX = 0;
    int LIMIT_INDEX = 1;

    void runAction() throws OperationException;

    static boolean messageIsLink(String message) {
        String[] messageArray = Helper.split(message);

        try {
            boolean uriValid = Helper.isValidLink(messageArray[URL_INDEX].strip());

            if (messageArray.length == 2)
                Integer.parseInt(messageArray[LIMIT_INDEX].strip());

            return uriValid;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    static boolean messageIsUuid(String message) {
        try {
            UUID.fromString(message.strip());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
