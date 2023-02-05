package duke;

import duke.command.Command;
import duke.exception.DukeException;
import duke.storage.Storage;
import duke.task.Parser;
import duke.task.TaskList;
import duke.ui.Ui;

/**
 * Class for the Duke object.
 *
 * @author Gao Mengqi
 * @version CS2103T AY22/23 Semester 2
 */
public class Duke {
    private TaskList tasks;
    private Ui ui;
    private Storage storage;
    private boolean isExit = false;

    public Duke() {

    }
    /**
     * Constructor for Duke object.
     *
     * @param filePath
     */
    public Duke(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (DukeException e) {
            tasks = new TaskList();
            ui.showLoadingError();
        }
    }

    /**
     * Start the Duke program.
     */
    public void run() {
        System.out.println(ui.showWelcome());
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine(); // show the divider line ("_______")
                Command c = Parser.parse(fullCommand);
                System.out.println(c.execute(tasks, ui, storage));
                isExit = c.isExit();
                ui.showLine();
            } catch (NullPointerException e) {
                continue;
            }
        }
    }

    /**
     * Initialise the Duke object.
     *
     * @param args
     * @throws DukeException
     */
    public static void main(String[] args) {
        new Duke("ip/data/tasks.txt").run();
    }


    /**
     * You should have your own function to generate a response to user input.
     * Replace this stub with your completed method.
     */
    public String getResponse(String input) {
        if (!isExit) {
            Command c = Parser.parse(input);
            assert c != null : "command should not be null";
            isExit = c.isExit();
            return c.execute(tasks, ui, storage);
        } else {
            return ui.exit();
        }
    }



}
