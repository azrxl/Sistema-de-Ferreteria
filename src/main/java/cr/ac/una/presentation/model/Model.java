package cr.ac.una.presentation.model;

import cr.ac.una.logic.Service;
import cr.ac.una.logic.objects.*;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private List<Categoria> categorias;
    private List<Subcategoria> subcategorias;
    private List<Articulo> articulos;
    private List<Presentacion> presentaciones;
    private List<String> unidades;

    public Model() {
        categorias = new ArrayList<>();
        subcategorias = new ArrayList<>();
        articulos = new ArrayList<>();
        presentaciones = new ArrayList<>();
    }

    public void initData() {
        setCategorias(Service.instance().getCategorias());
        setSubcategorias(Service.instance().getSubcategorias());
        setArticulos(Service.instance().getArticulos());
        setPresentaciones(Service.instance().getPresentaciones());
        setUnidades(Service.instance().getUnidades());
    }


    public List<Categoria> getCategorias() {
        return categorias;
    }
    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }
    public List<Subcategoria> getSubcategorias() {
        return subcategorias;
    }
    public void setSubcategorias(List<Subcategoria> subcategorias) {
        this.subcategorias = subcategorias;
    }
    public List<Articulo> getArticulos() {
        return articulos;
    }
    public void setArticulos(List<Articulo> articulos) {
        this.articulos = articulos;
    }
    public List<Presentacion> getPresentaciones() {
        return presentaciones;
    }
    public void setPresentaciones(List<Presentacion> presentaciones) {
        this.presentaciones = presentaciones;
    }
    public List<String> getUnidades() {
        return unidades;
    }
    public void setUnidades(List<String> unidades) {
        this.unidades = unidades;
    }

    public Factura generarFactura(List<CarritoItem> items) {
        double subtotal = 0;
        double descuentoUnidades = 0;
        List<String> articulosUnicos = new ArrayList<>();

        for (CarritoItem item : items) {
            double precioUnitario = item.getPresentacion().getPrecioVenta();
            int cantidad = item.getCantidad();
            double subtotalItem = precioUnitario * cantidad;
            subtotal += subtotalItem;

            // Descuento 10% si se compran más de 10 unidades del mismo artículo
            if (cantidad > 10) {
                descuentoUnidades += subtotalItem * 0.10;
            }
            // Contar artículos únicos
            String idArticulo = item.getPresentacion().getArticuloID();
            if (!articulosUnicos.contains(idArticulo)) {
                articulosUnicos.add(idArticulo);
            }
        }

        double descuentoDiferentes = 0;
        if (articulosUnicos.size() > 10) {
            descuentoDiferentes = subtotal * 0.05;
        }

        double descuentoMonto = 0;
        if (subtotal > 5000) {
            descuentoMonto = subtotal * 0.075;
        }

        double totalDescuento = descuentoUnidades + descuentoDiferentes + descuentoMonto;
        double totalFinal = subtotal - totalDescuento;

        return new Factura(subtotal, descuentoUnidades, descuentoDiferentes, descuentoMonto, totalDescuento, totalFinal);
    }
}
