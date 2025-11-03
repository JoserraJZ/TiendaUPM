package main.java.upm;

public class Cliente {
    private String name;
    private String DNI;
    private String email;
    private Cajero cashier;
    public Cliente(String newName, String newDni, String newEmail, Cajero newCashier){
        this.name=newName;
        this.DNI=newDni;
        this.email=newEmail;
        this.cashier=newCashier;
    }
}
