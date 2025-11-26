package upm;

public enum Command {
    //TODO: HE CAMBIADO ID POR CASHID PARA QUE FUNCIONE EN CIERTOS CASOS
    CLIENT_ADD("client add", new String[]{"\"<nombre>\"", "<DNI>", "<email>", "<cashId>"}, 6, 6),
    CLIENT_REMOVE("client remove", new String[]{"<DNI>"}, 3, 3),
    CLIENT_LIST("client list", new String[]{}, 2, 2),

    CASH_ADD("cash add", new String[]{"[<cashId>]", "\"<nombre>\"", "<email>"}, 4, 5),
    CASH_REMOVE("cash remove", new String[]{"<cashId>"}, 3, 3),
    CASH_LIST("cash list", new String[]{}, 2, 2),
    CASH_TICKETS("cash tickets", new String[]{"<cashId>"}, 3, 3),

    TICKET_NEW("ticket new", new String[]{"[<id>]", "<cashId>", "<userId>"}, 4, 5),
    TICKET_ADD("ticket add", new String[]{"<ticketId>", "<cashId>", "<prodId>", "<amount>", "[--p<txt> --p<txt>]"}, 6, 0),
    TICKET_REMOVE("ticket remove", new String[]{"<ticketId>", "<cashId>", "<prodId>"}, 5, 5),
    TICKET_PRINT("ticket print", new String[]{"<ticketId>", "<cashId>"}, 4, 4),
    TICKET_LIST("ticket list", new String[]{}, 2, 2),

    PROD_ADD("prod add", new String[]{"[<id>]", "\"<name>\"", "<category>", "<price>", "[<maxPers>]"}, 5, 7),
    PROD_UPDATE("prod update", new String[]{"<id>", "NAME|CATEGORY|PRICE", "<value>"}, 5, 5),
    PROD_ADDFOOD("prod addFood", new String[]{"[<id>]", "\"<name>\"", "<price>", "<expiration: yyyy-MM-dd>", "<max_people>"}, 6, 7),
    PROD_ADDMEETING("prod addMeeting", new String[]{"[<id>]", "\"<name>\"", "<price>", "<expiration: yyyy-MM-dd>", "<max_people>"}, 6, 7),
    PROD_LIST("prod list", new String[]{}, 2, 2),
    PROD_REMOVE("prod remove", new String[]{"<id>"}, 3, 3),

    HELP("help", new String[]{}, 1, 1),
    ECHO("echo", new String[]{"\"<text>\""}, 2, 0), // 0 = unlimited
    EXIT("exit", new String[]{}, 1, 1);

    public final String commandText;
    private final String[] parameters;
    private final int minLength;
    private final int maxLength;

    Command(String commandText, String[] parameters, int minLength, int maxLength) {
        this.commandText = commandText;
        this.parameters = parameters;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    public String getCommandText() {
        return this.commandText;
    }

    public String getHelp() {
        String params = String.join(" ", this.parameters);
        return this.getCommandText() + (params.isEmpty() ? "": " ") + params;
    }

    public boolean matchesLength(int length) {
        if (maxLength == 0) return length >= minLength;
        return length >= minLength && length <= maxLength;
    }

    private static boolean matchesFormat(String[] commandExpected, String[] commandGiven, int offsetAmount, String[] returnParams){
        String errorCode = "";
        for (int i = 0; i < commandExpected.length; i++) {
            if ((i + offsetAmount) >= commandGiven.length) return true;

            boolean isOptional = commandExpected[i].startsWith("[") && commandExpected[i].endsWith("]");
            boolean matches = true;

            switch (isOptional
                    ? commandExpected[i].substring(1, commandExpected[i].length() - 1)
                    : commandExpected[i]){
                case "<id>"->{
                    if (!commandGiven[i+offsetAmount].matches("\\d+")){
                        errorCode = String.format("El parámetro %d ('%s') debería ser un número%n", i+1, commandGiven[i+offsetAmount]);
                        matches = false;
                    }
                }
                case "<category>"->{
                    try{ ProductCategory.valueOf(commandGiven[i+offsetAmount]); } catch (IllegalArgumentException e) {
                        errorCode = String.format("El parámetro %d ('%s') debería ser una categoría válida%n", i+1, commandGiven[i+offsetAmount]);
                        matches = false;
                    }
                }
                case "<price>" -> {
                    if (!commandGiven[i + offsetAmount].matches("\\d+(\\.\\d+)?")) {
                        errorCode = String.format("El parámetro %d ('%s') debería ser un número%n", i + 1, commandGiven[i + offsetAmount]);
                        matches = false;
                    }
                }
                case "<DNI>", "<userId>" -> {
                    if (!commandGiven[i + offsetAmount].matches("[A-Za-z0-9]{9}")){//"\\d{8}[A-Za-z]")) {
                        errorCode = String.format("El parámetro %d ('%s') debería ser un DNI válido%n", i + 1, commandGiven[i + offsetAmount]);
                        matches = false;
                    }
                }
                case "<cashId>" ->{
                    if (!commandGiven[i + offsetAmount].matches("UW\\d{7}")) {
                        errorCode = String.format("El parámetro %d ('%s') debería ser un código válido%n", i + 1, commandGiven[i + offsetAmount]);
                        matches = false;
                    }
                }
                case "--p<txt> --p<txt>" ->{
                    for (int j = i + offsetAmount; j < commandGiven.length; j++) {
                        if (!commandGiven[j].matches("^--p\\S+$")) {
                            matches = false;
                            break;
                        }else{
                            returnParams[j-offsetAmount] = commandGiven[j].replaceFirst("^--p", "");
                        }
                    }
                }
            }

            if (!matches && isOptional){
                returnParams[i] = null;
                offsetAmount-=1;
            }
            else if (!matches) {
                System.out.print(errorCode);
                return false;
            }
            else{
                if(i != (commandExpected.length-1) || !commandGiven[commandGiven.length-1].startsWith("--p")) returnParams[i] = commandGiven[i + offsetAmount];
            }
        }
        return true;
    }

    public static ValidatedCommand validateCommand(String command){
        String[] splitted = Utils.splitText(command);
        if (splitted.length<1) return null;
        for (Command cmd : Command.values()) {
            if (cmd.commandText.equals(splitted[0]) ||
            (splitted.length>1 && cmd.commandText.equals(splitted[0] + " " + splitted[1]))){

                int wordCount = cmd.commandText.trim().split("\\s+").length;

                if(cmd.matchesLength(splitted.length)){
                    String[] returnParams = new String[Math.max(cmd.maxLength, splitted.length-wordCount)];
                    if (matchesFormat(cmd.parameters, splitted, wordCount, returnParams)) {

                        return new ValidatedCommand(cmd, returnParams);
                    }
                    else return null;

                }else{
                    System.out.printf("Número de parámetros incorrecto (esperados: %d, recibidos: %d)%n", cmd.minLength-wordCount, splitted.length-wordCount);

                    return null;
                }
            }
        }
        System.out.println("Comando desconocido");
        return null;
    }
}
