package upm;

public class Client {
    private String name;
    private String DNI;
    private String email;
    private Cashier cashier;

    public Client(String newName, String newDni, String newEmail, Cashier newCashier){
        this.name=newName;
        this.DNI=newDni;
        this.email=newEmail;
        this.cashier=newCashier;
    }
}
