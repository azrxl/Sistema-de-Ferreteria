package cr.ac.una.proxy;

public class Articulo extends ObjetoBase {
    private String subcategoriaID;
    private String marca;
    public Articulo(String id, String nombre, String marca, String descripcion, String subcategoriaID) {
        super(id, nombre, descripcion);
        this.subcategoriaID = subcategoriaID;
        this.marca = marca;
    }
    public String getSubcategoriaID() {
        return subcategoriaID;
    }
    public void setSubcategoriaID(String subcategoryID) {
        this.subcategoriaID = subcategoryID;
    }
    public String getMarca() {
        return marca;
    }
    public void setMarca(String marca) {
        this.marca = marca;
    }
    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + "[" + id + "] (" + marca + ") " + nombre + ": " + descripcion;
    }
}

