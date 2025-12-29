package upm;

public class TicketCompany extends Ticket{

    public TicketCompany(String id) {
        super(id);
    }

    //No mostrar precio de Servicio al imprimir, solo al final
    //15% de descuento a Productos por cada Servicio
    //Ticket combinado no puede imprimirse si no hay por lo menos uno de cada
    //Dos listas, una de servicios y una de productos independientes y mostrar una y luego otra o como sea requerido

}
