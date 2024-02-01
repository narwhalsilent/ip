import java.util.ArrayList;
import java.util.Scanner;

public class Knight {
    private static ArrayList<Task> tasks = new ArrayList<>();
    private static final String logo = "    ┓┏┓  •  ┓  \n"
            + "    ┃┫ ┏┓┓┏┓┣┓╋\n"
            + "    ┛┗┛┛┗┗┗┫┛┗┗\n"
            + "           ┛\n";

    private enum Command {
        BYE,
        LIST,
        MARK,
        UNMARK,
        DELETE,
        TODO,
        EVENT,
        DEADLINE,
        SAVE;
    }

    private static void speak(String s) {
        System.out.println("    " + s);
    }

    private static Command parseCommand(String s) {
        if (s.equals("bye")) {
            return Command.BYE;
        } else if (s.equals("save")) {
            return Command.SAVE;
        } else if (s.equals("list")) {
            return Command.LIST;
        } else if (s.matches("mark [1-9]\\d*")) {
            return Command.MARK;
        } else if (s.matches("unmark [1-9]\\d*")) {
            return Command.UNMARK;
        } else if (s.matches("delete [1-9]\\d*")) {
            return Command.DELETE;
        } else if (s.matches("todo \\S.*")) {
            return Command.TODO;
        } else if (s.matches("deadline \\S.* /by \\S.*")) {
            return Command.DEADLINE;
        } else if (s.matches("event \\S.* /from \\S.* /to \\S.*")) {
            return Command.EVENT;
        } else {
            throw new NonstandardCommandException();
        }
    }

    public static void main(String[] args) {
        speak("Greetings, Your Excellency! Thy humble\n"
                + logo
                + "    doth stand before thee. How may I serve thee on this day?");

        Scanner scan = new Scanner(System.in);
        String message;
        Command command;

        while (true) {
            message = scan.nextLine();

            try {
                command = parseCommand(message);
            } catch (NonstandardCommandException e) {
                if (message.startsWith("bye")) {
                    speak("Thou canst bid me farewell simply with:\n    bye");
                } else if (message.startsWith("list")) {
                    speak("Though canst view thy list simply with:\n    list");
                } else if (message.startsWith("mark")) {
                    speak("Take heed, for thou shouldst reference the task thou wishest to alter by its index:");
                    speak("mark [index]");
                } else if (message.startsWith("unmark")) {
                    speak("Take heed, for thou shouldst reference the task thou wishest to alter by its index:");
                    speak("unmark [index]");
                } else if (message.startsWith("delete")) {
                    speak("Take heed, for thou shouldst reference the task thou wishest to alter by its index:");
                    speak("delete [index]");
                } else if (message.startsWith("todo")) {
                    speak("Thou shouldst forge a todo task such as so:");
                    speak("todo [description]");
                } else if (message.startsWith("deadline")) {
                    speak("Thou shouldst forge a deadline task such as so:");
                    speak("deadline [description] /by [time]");
                } else if (message.startsWith("event")) {
                    speak("Thou shouldst forge an event task such as so:");
                    speak("event [description] /from [start time] /to [end time]");
                } else {
                    speak("I beg thine pardon, but I am clueless of the meaning of your utterance.");
                }
                continue;
            }

            if (command == Command.BYE) {
                break;
            } else if (command == Command.LIST) {
                if (tasks.isEmpty()) {
                    speak("Your Excellency, thy list remaineth free of tasks at this present moment.");
                } else {
                    speak("Behold, the duties thou hast assigned:");
                    int i = 1;
                    for (Task t : tasks) {
                        speak(i + "." + t);
                        i++;
                    }
                }
            } else if (command == Command.SAVE) {
                FileIO.writeToFile(tasks);
                speak("Thy list hath been saved to thy scrolls of history.");
            } else if (command == Command.MARK) {
                int index = Integer.parseInt(message.substring(5));
                Task task;

                try {
                    task = tasks.get(index - 1);
                } catch (IndexOutOfBoundsException e) {
                    speak("I regret to inform thee, Your Excellency, that thou lackest a task bearing this index in thy list.");
                    continue;
                }

                task.mark();
                speak("Well met! This task hath been marked as fulfilled:\n    " + task);
            } else if (command == Command.UNMARK) {
                int index = Integer.parseInt(message.substring(7));
                Task task;

                try {
                    task = tasks.get(index - 1);
                } catch (IndexOutOfBoundsException e) {
                    speak("I regret to inform thee, Your Excellency, that thou lackest a task bearing this index in thy list.");
                    continue;
                }

                task.unmark();
                speak("Verily, I have marked this task as not yet done:\n    " + task);
            } else if (command == Command.DELETE) {
                int index = Integer.parseInt(message.substring(7));
                Task task;
                try {
                    task = tasks.get(index - 1);
                } catch (IndexOutOfBoundsException e) {
                    speak("I regret to inform thee, Your Excellency, that thou lackest a task bearing this index in thy list.");
                    continue;
                }

                tasks.remove(index - 1);
                speak("Indeed, this task hath been wiped from thy list:\n    " + task);
            } else if (command == Command.TODO) {
                Task task = new ToDo(message.substring(5));
                tasks.add(task);
                speak("Understood. This task hath been added to thy list:\n    " + task);
            } else if (command == Command.DEADLINE) {
                String[] params = message.split(" /");
                Task task = new Deadline(params[0].substring(9), params[1].substring(3));
                tasks.add(task);
                speak("Understood. This task hath been added to thy list:\n    " + task);
            } else if (command == Command.EVENT) {
                String[] params = message.split(" /");
                Task task = new Event(params[0].substring(6), params[1].substring(5), params[2].substring(3));
                tasks.add(task);
                speak("Understood. This task hath been added to thy list:\n    " + task);
            }
        }

        speak("Farewell, Your Excellency! May we cross paths once more in the near future.");
    }
}
