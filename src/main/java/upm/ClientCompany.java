package upm;

public class ClientCompany extends Client{

    public ClientCompany(String name, String NIF, String email, Cashier cashier) {
        super(name, NIF, email, cashier);
    }

    //Ver como aceptar Servicios o Productos+Servicios
    //No mostrar precio de Servicios
    //Identificar por NIF

}
