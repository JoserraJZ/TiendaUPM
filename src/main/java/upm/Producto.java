package main.java.upm;

public class Producto {

    private int idProducto;
    private String nombreProducto;
    private categoria cat;
    private double precio;



    public static void main(String[] args) {


    }

    public Producto(int idProd, String nombreProd, categoria c, double pre){
        this.idProducto=idProd;
        this.nombreProducto=nombreProd;
        this.cat=c;
        this.precio=pre;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public categoria getCat() {
        return cat;
    }

    public double getPrecio() {
        return precio;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public void setCat(categoria cat) {
        this.cat = cat;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
