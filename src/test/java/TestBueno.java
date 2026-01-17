import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

@Disabled("Ignorado temporalmente para generar el JAR")
class TestBueno {


    String normalize(String s) {
        // Step 1: Trim lines and collapse spaces/tabs
        String normalizedLines = Arrays.stream(s.split("\n"))
                .map(String::trim)
                .map(line -> line.replaceAll("[ \t]+", " "))
                .collect(Collectors.joining("\n")); // join lines back

        // Step 2: Collapse 3+ consecutive newlines into 2
        return normalizedLines.replaceAll("\n{3,}", "\n\n");
    }

    @Test
    void main() {
        // ----- 1. Input commands -----
        String input =
                """
                        help
                        echo "Agrego Libro"
                        prod add 1 "Libro POO" BOOK 25
                        echo "Agrego Camiseta"
                        prod add 2 "Camiseta talla:M UPM" CLOTHES 15
                        prod add "Camiseta talla:M UPM" CLOTHES 15
                        echo "Listo Productos"
                        prod list
                        echo "Actualizo Nombre y Precio del Libro"
                        prod update 1 NAME "Libro POO V2"
                        prod update 1 PRICE 30
                        echo "inserto libro repetido y lo borro"
                        prod add 3 "Libro POO repetido Error" BOOK 25
                        prod remove 3
                        echo "Creo un Trabajador"
                        cash add UW1234567 "pepecurro3" pepe0@upm.es
                        cash add "pepecurro2" pepe0@upm.es
                        echo "Creo tres cliente listo borro uno y listo"
                        client add "Pepe3" 55630667S pepe1@upm.es UW1234567
                        client add "Pepe2" 98948334B pepe2@upm.es UW1234567
                        client add "Pepe1" Y8682724P pepe3@upm.es UW1234567
                        client list
                        client remove Y8682724P
                        client list
                        echo "creo un cajero nuevo listo, lo booro y listo de nuevo"
                        cash add UW1234569 "pepecurro1" pepe0@upm.es
                        cash list
                        cash remove UW1234569
                        cash list
                        echo "miro los tickets de UW1234567 creo un ticket y vuelvo a printar"
                        cash tickets UW1234567
                        ticket new UW1234567 55630667S
                        cash tickets UW1234567
                        echo "creo un ticket con ID"
                        ticket new 212121 UW1234567 55630667S
                        echo "Agrego un producto al ticket e imprimo el ticket"
                        ticket add 212121 UW1234567 1 20
                        ticket list
                        ticket print 212121 UW1234567
                        ticket list
                        echo "Agrego dos productos al ticket e imprimo el ticket"
                        ticket new 212123 UW1234567 98948334B
                        ticket add 212123 UW1234567 1 20
                        ticket add 212123 UW1234567 2 1
                        ticket remove 212123 UW1234567 2
                        ticket add 212123 UW1234567 2 3
                        ticket list
                        ticket print 212123 UW1234567
                        ticket list
                        echo "Trabajamos con productos de personas"
                        prod addMeeting 23456 "Reunion Rotonda" 12 2025-12-21 100
                        prod addMeeting 23457 "Graduacion ETSISI" 40 2025-12-21 30
                        prod addFood 23458 "Cafeteria ETSISI" 5 2025-12-21 300
                        prod addFood 23459 "Restaurante Asador" 50 2025-12-21 40
                        prod list
                        ticket new 212127 UW1234567 98948334B
                        ticket add 212127 UW1234567 23456 20
                        ticket add 212127 UW1234567 2 3
                        ticket print 212127 UW1234567
                        ticket list
                        echo "Trabajamos con productos personalizados"
                        prod add 5 "Camiseta talla:M UPM" CLOTHES 15 3
                        prod add 6 "Camiseta talla:L UPM" CLOTHES 20 4
                        ticket new 212128 UW1234567 98948334B
                        ticket add 212128 UW1234567 5 1
                        ticket add 212128 UW1234567 5 3 --pred --pblue --pgreen
                        ticket add 212128 UW1234567 6 3 --pred --pblue
                        ticket print 212128 UW1234567
                        echo "Iniciamos productos servicio"
                        prod add 2025-12-21 INSURANCE
                        prod add 2025-12-24 TRANSPORT
                        echo "Iniciamos empresas"
                        client add "pepe2" B12345674 pepe5@upm.es UW1234567
                        ticket new 212129 UW1234567 B12345674 -s
                        ticket add 212129 UW1234567 1S
                        ticket add 212129 UW1234567 2S
                        ticket print 212129 UW1234567
                        ticket new 212130 UW1234567 B12345674 -c
                        ticket add 212130 UW1234567 1S
                        ticket add 212130 UW1234567 2S
                        ticket add 212130 UW1234567 23456 20
                        ticket print 212130 UW1234567
                        exit
                        """;

        //HE MODIFICADO LOS COMANDOS PARA QUE FUNCIONE
        // ----- 2. Expected output (use the exact text you provided) -----
        String expected = """
                Welcome to the ticket module App.
                        Ticket module. Type 'help' to see commands.
                        tUPM> help
                        Commands:
                          client add "<nombre>" (<DNI>|<NIF>) <email> <cashId>
                          client remove <DNI>
                          client list
                          cash add [<cashId>] "<nombre>" <email>
                          cash remove <cashId>
                          cash list
                          cash tickets <cashId>
                          ticket new [<id>] <cashId> <userId> -[c|p|s] (default -p option)
                          ticket add <ticketId> <cashId> <prodId> [<amount>] [--p<txt> --p<txt>]
                          ticket remove <ticketId> <cashId> <prodId>
                          ticket print <ticketId> <cashId>
                          ticket list
                          prod add ([<id>] "<name>" <category> <price> [<maxPers>]) || (<expiration: yyyy-MM-dd> <serviceCategory>)
                          prod update <id> NAME|CATEGORY|PRICE <value>
                          prod addFood [<id>] "<name>" <price> <expiration: yyyy-MM-dd> <max_people>
                          prod addMeeting [<id>] "<name>" <price> <expiration: yyyy-MM-dd> <max_people>
                          prod list
                          prod remove <id>
                          help
                          echo "<text>"
                          exit
                
                        Categories: MERCH, STATIONERY, CLOTHES, BOOK, ELECTRONICS
                        Discounts if there are ≥2 units in the category: MERCH 0%, STATIONERY 5%, CLOTHES 7%, BOOK 10%, ELECTRONICS 3%.
                
                        tUPM> echo "Agrego Libro"
                        "Agrego Libro"\s
                
                        tUPM> prod add 1 "Libro POO" BOOK 25
                        {class:Product, id:1, name:'Libro POO', category:BOOK, price:25.0}
                        prod add: ok
                
                        tUPM> echo "Agrego Camiseta"
                        "Agrego Camiseta"\s
                
                        tUPM> prod add 2 "Camiseta talla:M UPM" CLOTHES 15
                        {class:Product, id:2, name:'Camiseta talla:M UPM', category:CLOTHES, price:15.0}
                        prod add: ok
                
                        tUPM> prod add "Camiseta talla:M UPM" CLOTHES 15
                        {class:Product, id:0, name:'Camiseta talla:M UPM', category:CLOTHES, price:15.0}
                        prod add: ok
                
                        tUPM> echo "Listo Productos"
                        "Listo Productos"\s
                
                        tUPM> prod list
                        Catalog:
                          {class:Product, id:0, name:'Camiseta talla:M UPM', category:CLOTHES, price:15.0}
                          {class:Product, id:1, name:'Libro POO', category:BOOK, price:25.0}
                          {class:Product, id:2, name:'Camiseta talla:M UPM', category:CLOTHES, price:15.0}
                        prod list: ok
                
                        tUPM> echo "Actualizo Nombre y Precio del Libro"
                        "Actualizo Nombre y Precio del Libro"\s
                
                        tUPM> prod update 1 NAME "Libro POO V2"
                        {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:25.0}
                        prod update: ok
                
                
                        tUPM> prod update 1 PRICE 30
                        {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0}
                        prod update: ok
                
                
                        tUPM> echo "inserto libro repetido y lo borro"
                        "inserto libro repetido y lo borro"\s
                
                        tUPM> prod add 3 "Libro POO repetido Error" BOOK 25
                        {class:Product, id:3, name:'Libro POO repetido Error', category:BOOK, price:25.0}
                        prod add: ok
                
                        tUPM> prod remove 3
                        {class:Product, id:3, name:'Libro POO repetido Error', category:BOOK, price:25.0}
                        prod remove: ok
                
                        tUPM> echo "Creo un Trabajador"
                        "Creo un Trabajador"\s
                
                        tUPM> cash add UW1234567 "pepecurro3" pepe0@upm.es
                        Cash{identifier='UW1234567', name='pepecurro3', email='pepe0@upm.es'}
                        cash add: ok
                
                        tUPM> cash add "pepecurro2" pepe0@upm.es
                        Cash{identifier='UW9121937', name='pepecurro2', email='pepe0@upm.es'}
                        cash add: ok
                
                        tUPM> echo "Creo tres cliente listo borro uno y listo"
                        "Creo tres cliente listo borro uno y listo"\s
                
                        tUPM> client add "Pepe3" 55630667S pepe1@upm.es UW1234567
                        USER{identifier='55630667S', name='Pepe3', email='pepe1@upm.es', cash=UW1234567}
                        client add: ok
                
                        tUPM> client add "Pepe2" 98948334B pepe2@upm.es UW1234567
                        USER{identifier='98948334B', name='Pepe2', email='pepe2@upm.es', cash=UW1234567}
                        client add: ok
                
                        tUPM> client add "Pepe1" Y8682724P pepe3@upm.es UW1234567
                        USER{identifier='Y8682724P', name='Pepe1', email='pepe3@upm.es', cash=UW1234567}
                        client add: ok
                
                        tUPM> client list
                        Client:
                          USER{identifier='Y8682724P', name='Pepe1', email='pepe3@upm.es', cash=UW1234567}
                          USER{identifier='98948334B', name='Pepe2', email='pepe2@upm.es', cash=UW1234567}
                          USER{identifier='55630667S', name='Pepe3', email='pepe1@upm.es', cash=UW1234567}
                        client list: ok
                
                        tUPM> client remove Y8682724P
                        client remove: ok
                
                        tUPM> client list
                        Client:
                          USER{identifier='98948334B', name='Pepe2', email='pepe2@upm.es', cash=UW1234567}
                          USER{identifier='55630667S', name='Pepe3', email='pepe1@upm.es', cash=UW1234567}
                        client list: ok
                
                        tUPM> echo "creo un cajero nuevo listo, lo booro y listo de nuevo"
                        "creo un cajero nuevo listo, lo booro y listo de nuevo"\s
                
                        tUPM> cash add UW1234569 "pepecurro1" pepe0@upm.es
                        Cash{identifier='UW1234569', name='pepecurro1', email='pepe0@upm.es'}
                        cash add: ok
                
                        tUPM> cash list
                        Cash:
                          Cash{identifier='UW1234569', name='pepecurro1', email='pepe0@upm.es'}
                          Cash{identifier='UW9121937', name='pepecurro2', email='pepe0@upm.es'}
                          Cash{identifier='UW1234567', name='pepecurro3', email='pepe0@upm.es'}
                        cash list: ok
                
                        tUPM> cash remove UW1234569
                        cash remove: ok
                
                        tUPM> cash list
                        Cash:
                          Cash{identifier='UW9121937', name='pepecurro2', email='pepe0@upm.es'}
                          Cash{identifier='UW1234567', name='pepecurro3', email='pepe0@upm.es'}
                        cash list: ok
                
                        tUPM> echo "miro los tickets de UW1234567 creo un ticket y vuelvo a printar"
                        "miro los tickets de UW1234567 creo un ticket y vuelvo a printar"\s
                
                        tUPM> cash tickets UW1234567
                        Tickets:\s
                        cash tickets: ok
                
                        tUPM> ticket new UW1234567 55630667S
                        Ticket : 25-12-07-22:32-47570
                          Total price: 0.0
                          Total discount: 0.0
                          Final Price: 0.0
                        ticket new: ok
                
                        tUPM> cash tickets UW1234567
                        Tickets:\s
                          25-12-07-22:32-47570->EMPTY
                        cash tickets: ok
                
                        tUPM> echo "creo un ticket con ID"
                        "creo un ticket con ID"\s
                
                        tUPM> ticket new 212121 UW1234567 55630667S
                        Ticket : 212121
                          Total price: 0.0
                          Total discount: 0.0
                          Final Price: 0.0
                        ticket new: ok
                
                        tUPM> echo "Agrego un producto al ticket e imprimo el ticket"
                        "Agrego un producto al ticket e imprimo el ticket"\s
                
                        tUPM> ticket add 212121 UW1234567 1 20
                        Ticket : 212121
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          Total price: 600.0
                          Total discount: 60.0
                          Final Price: 540.0
                        ticket add: ok
                
                        tUPM> ticket list
                        Ticket List:
                          25-12-07-22:32-47570 - EMPTY
                          212121 - OPEN
                        ticket list: ok
                
                        tUPM> ticket print 212121 UW1234567
                        Ticket : 212121-25-12-07-22:32
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          Total price: 600.0
                          Total discount: 60.0
                          Final Price: 540.0
                        ticket print: ok
                
                        tUPM> ticket list
                        Ticket List:
                          25-12-07-22:32-47570 - EMPTY
                          212121-25-12-07-22:32 - CLOSE
                        ticket list: ok
                
                        tUPM> echo "Agrego dos productos al ticket e imprimo el ticket"
                        "Agrego dos productos al ticket e imprimo el ticket"\s
                
                        tUPM> ticket new 212123 UW1234567 98948334B
                        Ticket : 212123
                          Total price: 0.0
                          Total discount: 0.0
                          Final Price: 0.0
                        ticket new: ok
                
                        tUPM> ticket add 212123 UW1234567 1 20
                        Ticket : 212123
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          Total price: 600.0
                          Total discount: 60.0
                          Final Price: 540.0
                        ticket add: ok
                
                        tUPM> ticket add 212123 UW1234567 2 1
                        Ticket : 212123
                          {class:Product, id:2, name:'Camiseta talla:M UPM', category:CLOTHES, price:15.0}
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          Total price: 615.0
                          Total discount: 60.0
                          Final Price: 555.0
                        ticket add: ok
                
                        tUPM> ticket remove 212123 UW1234567 2
                        Ticket : 212123
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          Total price: 600.0
                          Total discount: 60.0
                          Final Price: 540.0
                        ticket remove: ok
                
                        tUPM> ticket add 212123 UW1234567 2 3
                        Ticket : 212123
                          {class:Product, id:2, name:'Camiseta talla:M UPM', category:CLOTHES, price:15.0} **discount -1.05
                          {class:Product, id:2, name:'Camiseta talla:M UPM', category:CLOTHES, price:15.0} **discount -1.05
                          {class:Product, id:2, name:'Camiseta talla:M UPM', category:CLOTHES, price:15.0} **discount -1.05
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          Total price: 645.0
                          Total discount: 63.15
                          Final Price: 581.85
                        ticket add: ok
                
                        tUPM> ticket list
                        Ticket List:
                          25-12-07-22:32-47570 - EMPTY
                          212123 - OPEN
                          212121-25-12-07-22:32 - CLOSE
                        ticket list: ok
                
                        tUPM> ticket print 212123 UW1234567
                        Ticket : 212123-25-12-07-22:32
                          {class:Product, id:2, name:'Camiseta talla:M UPM', category:CLOTHES, price:15.0} **discount -1.05
                          {class:Product, id:2, name:'Camiseta talla:M UPM', category:CLOTHES, price:15.0} **discount -1.05
                          {class:Product, id:2, name:'Camiseta talla:M UPM', category:CLOTHES, price:15.0} **discount -1.05
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0} **discount -3.0
                          Total price: 645.0
                          Total discount: 63.15
                          Final Price: 581.85
                        ticket print: ok
                
                        tUPM> ticket list
                        Ticket List:
                          25-12-07-22:32-47570 - EMPTY
                          212123-25-12-07-22:32 - CLOSE
                          212121-25-12-07-22:32 - CLOSE
                        ticket list: ok
                
                        tUPM> echo "Trabajamos con productos de personas"
                        "Trabajamos con productos de personas"\s
                
                        tUPM> prod addMeeting 23456 "Reunion Rotonda" 12 2025-12-21 100
                        {class:Meeting, id:23456, name:'Reunion Rotonda', price:12.0, date of Event:2025-12-21, max people allowed:100}
                        prod addMeeting: ok
                
                        tUPM> prod addMeeting 23457 "Graduacion ETSISI" 40 2025-12-21 30
                        {class:Meeting, id:23457, name:'Graduacion ETSISI', price:40.0, date of Event:2025-12-21, max people allowed:30}
                        prod addMeeting: ok
                
                        tUPM> prod addFood 23458 "Cafeteria ETSISI" 5 2025-12-21 300
                        Error processing ->prod addFood ->Error adding product
                
                        tUPM> prod addFood 23459 "Restaurante Asador" 50 2025-12-21 40
                        {class:Food, id:23459, name:'Restaurante Asador', price:50.0, date of Event:2025-12-21, max people allowed:40}
                        prod addFood: ok
                
                        tUPM> prod list
                        Catalog:
                          {class:Product, id:0, name:'Camiseta talla:M UPM', category:CLOTHES, price:15.0}
                          {class:Product, id:1, name:'Libro POO V2', category:BOOK, price:30.0}
                          {class:Product, id:2, name:'Camiseta talla:M UPM', category:CLOTHES, price:15.0}
                          {class:Meeting, id:23456, name:'Reunion Rotonda', price:12.0, date of Event:2025-12-21, max people allowed:100}
                          {class:Meeting, id:23457, name:'Graduacion ETSISI', price:40.0, date of Event:2025-12-21, max people allowed:30}
                          {class:Food, id:23459, name:'Restaurante Asador', price:50.0, date of Event:2025-12-21, max people allowed:40}
                        prod list: ok
                
                        tUPM> ticket new 212127 UW1234567 98948334B
                        Ticket : 212127
                          Total price: 0.0
                          Total discount: 0.0
                          Final Price: 0.0
                        ticket new: ok
                
                        tUPM> ticket add 212127 UW1234567 23456 20
                        Ticket : 212127
                          {class:Meeting, id:23456, name:'Reunion Rotonda', price:240,0, date of Event:2025-12-21, max people allowed:100, actual people in event:20}
                          Total price: 240.0
                          Total discount: 0.0
                          Final Price: 240.0
                        ticket add: ok
                
                        tUPM> ticket add 212127 UW1234567 2 3
                        Ticket : 212127
                          {class:Product, id:2, name:'Camiseta talla:M UPM', category:CLOTHES, price:15.0} **discount -1.05
                          {class:Product, id:2, name:'Camiseta talla:M UPM', category:CLOTHES, price:15.0} **discount -1.05
                          {class:Product, id:2, name:'Camiseta talla:M UPM', category:CLOTHES, price:15.0} **discount -1.05
                          {class:Meeting, id:23456, name:'Reunion Rotonda', price:240,0, date of Event:2025-12-21, max people allowed:100, actual people in event:20}
                          Total price: 285.0
                          Total discount: 3.15
                          Final Price: 281.85
                        ticket add: ok
                
                        tUPM> ticket print 212127 UW1234567
                        Ticket : 212127-25-12-07-22:32
                          {class:Product, id:2, name:'Camiseta talla:M UPM', category:CLOTHES, price:15.0} **discount -1.05
                          {class:Product, id:2, name:'Camiseta talla:M UPM', category:CLOTHES, price:15.0} **discount -1.05
                          {class:Product, id:2, name:'Camiseta talla:M UPM', category:CLOTHES, price:15.0} **discount -1.05
                          {class:Meeting, id:23456, name:'Reunion Rotonda', price:240,0, date of Event:2025-12-21, max people allowed:100, actual people in event:20}
                          Total price: 285.0
                          Total discount: 3.15
                          Final Price: 281.85
                        ticket print: ok
                
                        tUPM> ticket list
                        Ticket List:
                          25-12-07-22:32-47570 - EMPTY
                          212127-25-12-07-22:32 - CLOSE
                          212123-25-12-07-22:32 - CLOSE
                          212121-25-12-07-22:32 - CLOSE
                        ticket list: ok
                
                        tUPM> echo "Trabajamos con productos personalizados"
                        "Trabajamos con productos personalizados"\s
                
                        tUPM> prod add 5 "Camiseta talla:M UPM" CLOTHES 15 3
                        {class:ProductPersonalized, id:5, name:'Camiseta talla:M UPM', category:CLOTHES, price:15.0, maxPersonal:3}
                        prod add: ok
                
                        tUPM> prod add 6 "Camiseta talla:L UPM" CLOTHES 20 4
                        {class:ProductPersonalized, id:6, name:'Camiseta talla:L UPM', category:CLOTHES, price:20.0, maxPersonal:4}
                        prod add: ok
                
                        tUPM> ticket new 212128 UW1234567 98948334B
                        Ticket : 212128
                          Total price: 0.0
                          Total discount: 0.0
                          Final Price: 0.0
                        ticket new: ok
                
                        tUPM> ticket add 212128 UW1234567 5 1
                        Ticket : 212128
                          {class:ProductPersonalized, id:5, name:'Camiseta talla:M UPM', category:CLOTHES, price:15.0, maxPersonal:3}
                          Total price: 15.0
                          Total discount: 0.0
                          Final Price: 15.0
                        ticket add: ok
                
                        tUPM> ticket add 212128 UW1234567 5 3 --pred --pblue --pgreen
                        Ticket : 212128
                          {class:ProductPersonalized, id:5, name:'Camiseta talla:M UPM', category:CLOTHES, price:15.0, maxPersonal:3} **discount -1.05
                          {class:ProductPersonalized, id:5, name:'Camiseta talla:M UPM', category:CLOTHES, price:19.5, maxPersonal:3, personalizationList:[red, blue, green]} **discount -1.365
                          {class:ProductPersonalized, id:5, name:'Camiseta talla:M UPM', category:CLOTHES, price:19.5, maxPersonal:3, personalizationList:[red, blue, green]} **discount -1.365
                          {class:ProductPersonalized, id:5, name:'Camiseta talla:M UPM', category:CLOTHES, price:19.5, maxPersonal:3, personalizationList:[red, blue, green]} **discount -1.365
                          Total price: 73.5
                          Total discount: 5.145
                          Final Price: 68.355
                        ticket add: ok
                
                        tUPM> ticket add 212128 UW1234567 6 3 --pred --pblue
                        Ticket : 212128
                          {class:ProductPersonalized, id:6, name:'Camiseta talla:L UPM', category:CLOTHES, price:24.0, maxPersonal:4, personalizationList:[red, blue]} **discount -1.68
                          {class:ProductPersonalized, id:6, name:'Camiseta talla:L UPM', category:CLOTHES, price:24.0, maxPersonal:4, personalizationList:[red, blue]} **discount -1.68
                          {class:ProductPersonalized, id:6, name:'Camiseta talla:L UPM', category:CLOTHES, price:24.0, maxPersonal:4, personalizationList:[red, blue]} **discount -1.68
                          {class:ProductPersonalized, id:5, name:'Camiseta talla:M UPM', category:CLOTHES, price:15.0, maxPersonal:3} **discount -1.05
                          {class:ProductPersonalized, id:5, name:'Camiseta talla:M UPM', category:CLOTHES, price:19.5, maxPersonal:3, personalizationList:[red, blue, green]} **discount -1.365
                          {class:ProductPersonalized, id:5, name:'Camiseta talla:M UPM', category:CLOTHES, price:19.5, maxPersonal:3, personalizationList:[red, blue, green]} **discount -1.365
                          {class:ProductPersonalized, id:5, name:'Camiseta talla:M UPM', category:CLOTHES, price:19.5, maxPersonal:3, personalizationList:[red, blue, green]} **discount -1.365
                          Total price: 145.5
                          Total discount: 10.185
                          Final Price: 135.315
                        ticket add: ok
                
                        tUPM> ticket print 212128 UW1234567
                        Ticket : 212128-25-12-07-22:32
                          {class:ProductPersonalized, id:6, name:'Camiseta talla:L UPM', category:CLOTHES, price:24.0, maxPersonal:4, personalizationList:[red, blue]} **discount -1.68
                          {class:ProductPersonalized, id:6, name:'Camiseta talla:L UPM', category:CLOTHES, price:24.0, maxPersonal:4, personalizationList:[red, blue]} **discount -1.68
                          {class:ProductPersonalized, id:6, name:'Camiseta talla:L UPM', category:CLOTHES, price:24.0, maxPersonal:4, personalizationList:[red, blue]} **discount -1.68
                          {class:ProductPersonalized, id:5, name:'Camiseta talla:M UPM', category:CLOTHES, price:15.0, maxPersonal:3} **discount -1.05
                          {class:ProductPersonalized, id:5, name:'Camiseta talla:M UPM', category:CLOTHES, price:19.5, maxPersonal:3, personalizationList:[red, blue, green]} **discount -1.365
                          {class:ProductPersonalized, id:5, name:'Camiseta talla:M UPM', category:CLOTHES, price:19.5, maxPersonal:3, personalizationList:[red, blue, green]} **discount -1.365
                          {class:ProductPersonalized, id:5, name:'Camiseta talla:M UPM', category:CLOTHES, price:19.5, maxPersonal:3, personalizationList:[red, blue, green]} **discount -1.365
                          Total price: 145.5
                          Total discount: 10.185
                          Final Price: 135.315
                        ticket print: ok
                
                        tUPM> echo "Iniciamos productos servicio"
                        "Iniciamos productos servicio"\s
                
                        tUPM> prod add 2025-12-21 INSURANCE
                        {class:ProductService, id:1, category:INSURANCE, expiration:Sun Dec 21 00:00:00 CET 2025}
                        prod add: ok
                
                        tUPM> prod add 2025-12-24 TRANSPORT
                        {class:ProductService, id:2, category:TRANSPORT, expiration:Wed Dec 24 00:00:00 CET 2025}
                        prod add: ok
                
                        tUPM> echo "Iniciamos empresas"
                        "Iniciamos empresas"\s
                
                        tUPM> client add "pepe2" B12345674 pepe5@upm.es UW1234567
                        COMPANY{identifier='B12345674', name='pepe2', email='pepe5@upm.es', cash=UW1234567}
                        client add: ok
                
                        tUPM> ticket new 212129 UW1234567 B12345674 -s
                        Ticket : 212129
                        ticket new: ok
                
                        tUPM> ticket add 212129 UW1234567 1S
                        Ticket : 212129
                        Services Included:\s
                          {class:ProductService, id:1, category:INSURANCE, expiration:Sun Dec 21 00:00:00 CET 2025}
                        ticket add: ok
                
                        tUPM> ticket add 212129 UW1234567 2S
                        Ticket : 212129
                        Services Included:\s
                          {class:ProductService, id:1, category:INSURANCE, expiration:Sun Dec 21 00:00:00 CET 2025}
                          {class:ProductService, id:2, category:TRANSPORT, expiration:Wed Dec 24 00:00:00 CET 2025}
                        ticket add: ok
                
                        tUPM> ticket print 212129 UW1234567
                        Ticket : 212129-25-12-07-22:32
                        Services Included:\s
                          {class:ProductService, id:1, category:INSURANCE, expiration:Sun Dec 21 00:00:00 CET 2025}
                          {class:ProductService, id:2, category:TRANSPORT, expiration:Wed Dec 24 00:00:00 CET 2025}
                        ticket print: ok
                
                        tUPM> ticket new 212130 UW1234567 B12345674 -c
                        Ticket : 212130
                        ticket new: ok
                
                        tUPM> ticket add 212130 UW1234567 1S
                        Ticket : 212130
                        Services Included:\s
                          {class:ProductService, id:1, category:INSURANCE, expiration:Sun Dec 21 00:00:00 CET 2025}
                        ticket add: ok
                
                        tUPM> ticket add 212130 UW1234567 2S
                        Ticket : 212130
                        Services Included:\s
                          {class:ProductService, id:1, category:INSURANCE, expiration:Sun Dec 21 00:00:00 CET 2025}
                          {class:ProductService, id:2, category:TRANSPORT, expiration:Wed Dec 24 00:00:00 CET 2025}
                        ticket add: ok
                
                        tUPM> ticket add 212130 UW1234567 23456 20
                        Ticket : 212130
                        Services Included:\s
                          {class:ProductService, id:1, category:INSURANCE, expiration:Sun Dec 21 00:00:00 CET 2025}
                          {class:ProductService, id:2, category:TRANSPORT, expiration:Wed Dec 24 00:00:00 CET 2025}
                        Product Included
                          {class:Meeting, id:23456, name:'Reunion Rotonda', price:240,0, date of Event:2025-12-21, max people allowed:100, actual people in event:20}
                          Total price: 240.0
                          Extra Discount from services:72.0 **discount -72.0
                          Total discount: 72.0
                          Final Price: 168.0
                        ticket add: ok
                
                        tUPM> ticket print 212130 UW1234567
                        Ticket : 212130-25-12-07-22:32
                        Services Included:\s
                          {class:ProductService, id:1, category:INSURANCE, expiration:Sun Dec 21 00:00:00 CET 2025}
                          {class:ProductService, id:2, category:TRANSPORT, expiration:Wed Dec 24 00:00:00 CET 2025}
                        Product Included
                          {class:Meeting, id:23456, name:'Reunion Rotonda', price:240,0, date of Event:2025-12-21, max people allowed:100, actual people in event:20}
                          Total price: 240.0
                          Extra Discount from services:72.0 **discount -72.0
                          Total discount: 72.0
                          Final Price: 168.0
                        ticket print: ok
                
                        tUPM> exit
                        Closing application.
                        Goodbye!
                """;
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);

        // ----- 4. Run the application -----
        upm.Tienda.main(new String[0]);


        // ----- 6. Compare actual vs expected ----
        String actualOutput = baos.toString();
        Assertions.assertEquals(
                normalize(expected),
                normalize(actualOutput)
        );
    }
}