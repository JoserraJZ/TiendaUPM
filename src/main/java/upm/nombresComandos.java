package main.java.upm;

public enum nombresComandos {
    CREATE_USER("create-user", "<mobile>,<name>,<address>", "Se crea un usuario"),
    LIST_USERS("list-users", "", "Muestra todos los usuarios"),
    HELP("help", "", "Muestra la ayuda"),
    EXIT("exit", "", "Termina la ejecución");

    private final String value;
    private final String parameters;
    private final String help;

    nombresComandos(String value, String parametters, String help) {
        this.value = value;
        this.parameters = parametters;
        this.help = help;
    }

    public static nombresComandos fromValue(String value) {
        for (nombresComandos command : nombresComandos.values()) {
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
        return this.getValue() + " (" + params + ")   " + this.help;
    }
}
