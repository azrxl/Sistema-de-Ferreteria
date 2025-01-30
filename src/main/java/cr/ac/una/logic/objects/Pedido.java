package cr.ac.una.logic.objects;

import java.util.List;

public class Pedido {
    private List<Articulo> articulos;
    public Pedido(List<Articulo> articulos) {
        this.articulos = articulos;
    }

    public void setArticulos(List<Articulo> articulos) {
        this.articulos = articulos;
    }

    public List<Articulo> getArticulos() {
        return articulos;
    }
}
