package upm;

@SuppressWarnings("ClassCanBeRecord")
public class ValidatedCommand {
    public final Command command;
    public final String[] parameters;

    public ValidatedCommand(Command command, String[] parameters) {
        this.command = command;
        this.parameters = parameters;
    }
}
