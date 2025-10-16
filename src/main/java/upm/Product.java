package upm;

public class Product {

    private int id;
    private String name;
    private Category category;
    private double price;

    public Product(int idProd, String productName, Category category, double price){
        this.id = idProd;
        this.name = productName;
        this.category = category;
        this.price = price;
    }

    public int getIdProducto() {
        return id;
    }

    public String getNombreProducto() {
        return name;
    }

    public Category getCat() {
        return category;
    }

    public double getPrecio() {
        return price;
    }

    public void setNombreProducto(String nombreProducto) {
        this.name = nombreProducto;
    }

    public void setCat(Category cat) {
        this.category = cat;
    }

    public void setPrecio(int precio) {
        this.price = precio;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product other = (Product) obj;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
