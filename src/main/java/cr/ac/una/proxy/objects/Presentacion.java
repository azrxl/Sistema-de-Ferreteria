package cr.ac.una.proxy.objects;

public class Presentacion {
    private String articuloID;
    private final String unidad;
    private String capacidad;
    private double precioCompra;
    private double precioVenta;
    private int cantidad;

    public Presentacion(String unidad, String capacity, String articuloID, double precioCompra, double precioVenta, int cantidad) {
        this.articuloID = articuloID;
        this.unidad = unidad;
        this.capacidad = capacity;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.cantidad = cantidad;
    }
    public String getArticuloID() {
        return articuloID;
    }
    public void setArticuloID(String articuloID) {
        this.articuloID = articuloID;
    }
    public String getUnidad() {
        return unidad;
    }
    public String getCapacidad() {
        return capacidad;
    }
    public void setCapacidad(String capacidad) {
        this.capacidad = capacidad;
    }
    public double getPrecioCompra() {
        return precioCompra;
    }
    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }
    public double getPrecioVenta() {
        return precioVenta;
    }
    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }
    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + capacidad + unidad;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Presentacion) {
            return articuloID.equals(((Presentacion) obj).articuloID) && unidad.equals(((Presentacion) obj).unidad) && capacidad.equals(((Presentacion) obj).capacidad);
        }
        return false;
    }
}
