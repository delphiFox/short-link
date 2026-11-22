package process;

public interface Editable extends Helper {
    int EDIT_OPERATION_INDEX = 0;
    int EDIT_ACTION_INDEX = 1;

    int EDIT_LINK_NAME = 1;
    int EDIT_LINK_RESTRICTION = 2;
    int EDIT_LINK_DELETE = 3;

    void run();

    static boolean messageIsNewLinkName(String message) {
        String[] messageArray = Helper.split(message);

        try {
            int operation = Integer.parseInt(messageArray[EDIT_OPERATION_INDEX].strip());
            return operation == EDIT_LINK_NAME && Helper.isValidLink(messageArray[EDIT_ACTION_INDEX].strip());
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    static boolean messageIsNewLimitCount(String message) {
        String[] messageArray = Helper.split(message);

        try {
            int operation = Integer.parseInt(messageArray[EDIT_OPERATION_INDEX].strip());
            Integer.parseInt(messageArray[EDIT_ACTION_INDEX].strip());
            return operation == EDIT_LINK_RESTRICTION;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    static boolean messageIsDeleteLink(String message) {
        String[] messageArray = Helper.split(message);

        try {
            int operation = Integer.parseInt(messageArray[EDIT_OPERATION_INDEX].strip());
            String action = messageArray[EDIT_ACTION_INDEX].strip();
            return operation == EDIT_LINK_DELETE && "del".equalsIgnoreCase(action);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }
}
