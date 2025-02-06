package cr.ac.una.proxy.objects;

public class CarritoItem {
    private final Presentacion presentacion;
    private int cantidad;

    public CarritoItem(Presentacion presentacion, int cantidad) {
        this.presentacion = presentacion;
        this.cantidad = cantidad;
    }

    public Presentacion getPresentacion() {
        return presentacion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
