package duke.storage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import duke.exception.DukeException;
import duke.task.Deadline;
import duke.task.Event;
import duke.task.Task;
import duke.task.Todo;

/**
 * Storage class to store the tasks in text file.
 *
 * @author Gao Mengqi
 * @version CS2103T AY22/23 Semester 2
 */
public class Storage {
    private static final String TODO = "T";
    private static final String DEADLINE = "D";
    private static final String EVENT = "E";

    private String filePath;

    /**
     * Constructor for Storage.
     *
     * @param filePath
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public void markTaskStatus(String status, Task task) {
        if (status.equals("0")) {
            task.markAsIncomplete();
        } else if (status.equals("1")) {
            task.markAsDone();
        }
    }

    public Deadline formatDeadlineStr(String str) {
        String updatedStr = str.replace("(", "").replace(")", "").trim();
        String[] paraForDeadline = updatedStr.split("by: ", 2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy hhmm a");
        LocalDateTime tempDueDate = LocalDateTime.parse(paraForDeadline[1], formatter);

        DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HHmm");
        String finalDueDate = tempDueDate.format(newFormatter);

        LocalDateTime dueDate = LocalDateTime.parse(finalDueDate, newFormatter);
        return new Deadline(paraForDeadline[0], dueDate);
    }

    public Event formatEventStr(String str) {
        String updatedStr = str.replace("(", "").replace(")", "").trim();
        String[] getParas = updatedStr.split("from: ", 2);
        String getDesc = getParas[0];
        String[] getDueDateInfo = getParas[1].split("to: ", 2);
        return new Event(getDesc, getDueDateInfo[0], getDueDateInfo[1]);
    }

    /**
     * Convert the task information in the txt file to Task objects.
     *
     * @param str
     * @return Task objects.
     */

    private Task convertStrToTask(String str) {
        Task task = null;
        final String TASK_TYPE = str.substring(0, 1);
        final String STATUS_OF_TASK = str.substring(4, 5);
        switch (TASK_TYPE) {
        case TODO:
            task = new Todo(str.substring(8));
            break;
        case DEADLINE:
            task = formatDeadlineStr(str.substring(8));
            break;
        case EVENT:
            task = formatEventStr(str.substring(8));
            break;
        default:
            break;
        }
        markTaskStatus(STATUS_OF_TASK, task);
        return task;
    }

    /**
     * Load the existing task list to Duke.
     *
     * @return TaskList.
     * @throws DukeException
     */

    public ArrayList<Task> load() throws DukeException {
        ArrayList<Task> taskList = new ArrayList<>();
        try {
            BufferedReader bufReader = new BufferedReader(new FileReader(filePath));
            String currLine = bufReader.readLine();
            while (currLine != null) {
                Task addTask = convertStrToTask(currLine);
                taskList.add(addTask);
                currLine = bufReader.readLine();
            }
            bufReader.close();
        } catch (FileNotFoundException e) {
            throw new DukeException(e.getMessage());
        } catch (IOException e) {
            throw new DukeException(e.getMessage());
        } finally {
            return taskList;
        }
    }

    /**
     * Update the text file whenever a new task is added by the user.
     *
     * @param task
     * @throws IOException
     */

    public void update(Task task) throws IOException {
        Path dataDir = Paths.get("ip/data");
        Path dataFile = Paths.get("ip/data/tasks.txt");

        // directory does not exists
        if (!Files.isDirectory(dataDir)) {
            Files.createDirectory(dataDir);
            Files.createFile(dataFile);
        } else {
            // if directory exist, check if duke.txt exists
            if (!Files.exists(dataFile)) {
                Files.createFile(dataFile);
            }
        }

        assert Files.exists(dataFile) : "tasks.txt file should be created";
        FileWriter writer = new FileWriter("ip/data/tasks.txt", true);

        String finalTasks = "";
        String taskInfo = task.toString().replace("[ ]", " | 0 |").replace("[X]", "| 1 |");
        taskInfo = taskInfo.replace("[", "").replace("]", "");
        finalTasks = finalTasks + taskInfo + "\n";
        writer.write(finalTasks);
        writer.close();
    }

    /**
     * Sync the tasks.txt file to delete the specific task as well.
     * @param taskNum
     */
    public void delete(int taskNum) {
        try {
            Path tempFile = Paths.get("ip/data/tempTasks.txt");
            Files.createFile(tempFile);
            assert Files.exists(tempFile) : "tempTasks.txt file should be created";
            BufferedReader bufReader = new BufferedReader(new FileReader(filePath));
            FileWriter writerToTempFile = new FileWriter("ip/data/tempTasks.txt", true);

            int currLine = 1;
            String line = bufReader.readLine();

            while (line != null) {
                if (currLine == taskNum) {
                    line = bufReader.readLine();
                    currLine++;
                    continue;
                } else {
                    writerToTempFile.write(line + "\n");
                    line = bufReader.readLine();
                    currLine++;
                }
            }
            bufReader.close();
            writerToTempFile.close();
            Files.delete(Paths.get("ip/data/tasks.txt"));
            Files.move(tempFile, tempFile.resolveSibling(
                    "tasks.txt"));
        } catch (IOException e) {
            System.out.println("operation failed");
        }
    }
}
