import java.util.ArrayList;
import java.util.Scanner;

public class Duke {
    public static ArrayList<Task> tasks = new ArrayList<>();

    public static Scanner sc = new Scanner(System.in);

    public static void greet() {
        System.out.println("Hello! I'm Duke\nWhat can I do for you?");
    }

    public static void splitCommand(String command) {
        String[] arr;
        if (command.contains("/")) {
            String[] temp = command.split(" ", 2);
            String[] temp2 = temp[1].split("/");
            arr = new String[temp.length + temp2.length -1];
            arr[0] = temp[0];
            System.arraycopy(temp2, 0, arr, temp.length-1, temp2.length);
        } else {
            arr = command.split(" ", 2);
        }
        echo(arr);

    }

    public static void echo(String[] arr) {
        switch (arr[0]) {
            case "bye":
                exit();
                break;
            case "list":
                int counter = 1;
                for (Task element : tasks) {
                    System.out.println(counter + ". " + element.toString());
                    counter++;
                }
                break;
            case "unmark":
                int taskNum = Integer.parseInt(arr[1]);
                Task task = tasks.get(taskNum - 1);
                task.markAsIncomplete();
                break;
            case "mark":
                int numTask = Integer.parseInt(arr[1]);
                Task markedTask = tasks.get(numTask - 1);
                markedTask.markAsDone();
                break;
            case "todo":
                Todo toDo = new Todo(arr[1]);
                tasks.add(toDo);
                System.out.println("Got it. I've added this task:\n" + toDo + "\nNow you have " + tasks.size() + " tasks in the list");
                break;
            case "deadline":
                Deadline deadline = new Deadline(arr[1], arr[2].substring(3));
                tasks.add(deadline);
                System.out.println("Got it. I've added this task:\n" + deadline + "\nNow you have " + tasks.size() + " tasks in the list");
                break;
            case "event":
                Event event = new Event(arr[1], arr[2].substring(5), arr[3].substring(3));
                tasks.add(event);
                System.out.println("Got it. I've added this task:\n" + event + "\nNow you have " + tasks.size() + " tasks in the list");
                break;
            default:
                System.out.println("i dont understand bro");
        }
    }
    public static void exit() {
        System.out.println("Bye. Hope to see you again soon!");
        sc.close();
    }
    public static void main(String[] args) {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        System.out.println("Hello from\n" + logo);
        greet();
        try {
            while (sc.hasNext()) {
                splitCommand(sc.nextLine());
            }
        } catch (IllegalStateException e) {
            System.exit(0);
        }
    }
}
