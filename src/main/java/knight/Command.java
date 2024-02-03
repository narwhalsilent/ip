package knight;

public enum Command {
    BYE,
    LIST,
    MARK,
    UNMARK,
    DELETE,
    TODO,
    EVENT,
    DEADLINE,
    SAVE,
    FIND;

    boolean isTaskInitialisation() {
        return this == TODO || this == EVENT || this == DEADLINE || this == MARK;
    }
}
