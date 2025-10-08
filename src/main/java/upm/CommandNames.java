package main.java.upm;

public enum CommandNames {
    PROD_ADD("prod add", "<id> \"<name>\" <category> <price>"),
    PROD_LIST("prod list", ""),
    PROD_UPDATE("prod update", "<id> NAME|CATEGORY|PRICE <value>"),
    PROD_REMOVE("prod remove", "remove <id>"),
    TICKET_NEW("ticket new", ""),
    TICKET_ADD("ticket add", "<prodId> <quantity>"),
    TICKET_REMOVE("ticket remove", "<prodId>"),
    TICKET_PRINT("ticket print", ""),
    HELP("help", ""),
    EXIT("exit", "");


    private final String value;
    private final String parameters;

    CommandNames(String value, String parametters) {
        this.value = value;
        this.parameters = parametters;
    }

    public static CommandNames fromValue(String value) {
        for (CommandNames command : CommandNames.values()) {
            if (command.getValue().equals(value)) {
                return command;
            }
        }
        throw new UnsupportedOperationException("Comando '" + value + "' no existe.");
    }

    public String getValue() {
        return this.value;
    }

    public String getParameters() {
        return parameters;
    }

    public String getHelp() {
        String params = this.getParameters().isEmpty() ? "Sin parámetros" : this.getParameters();
        return this.getValue() + " (" + params + ")";
    }
}
