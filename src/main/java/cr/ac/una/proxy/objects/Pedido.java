package cr.ac.una.proxy.objects;

import java.util.List;

public class Pedido {
    private List<CarritoItem> items;

    public Pedido(List<CarritoItem> items) {
        this.items = items;
    }

    public List<CarritoItem> getItems() {
        return items;
    }

    public void setItems(List<CarritoItem> items) {
        this.items = items;
    }
}
