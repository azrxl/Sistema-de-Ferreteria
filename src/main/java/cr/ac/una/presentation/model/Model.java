package cr.ac.una.presentation.model;

import cr.ac.una.proxy.Articulo;
import cr.ac.una.proxy.Categoria;
import cr.ac.una.proxy.Presentacion;
import cr.ac.una.proxy.Subcategoria;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private List<Categoria> categorias;
    private List<Subcategoria> subcategorias;
    private List<Articulo> articulos;
    private List<Presentacion> presentaciones;

    public Model() {
        categorias = new ArrayList<>();
        subcategorias = new ArrayList<>();
        articulos = new ArrayList<>();
        presentaciones = new ArrayList<>();
    }

    public void init(List<Categoria> categorias, List<Subcategoria> subcategorias, List<Articulo> articulos, List<Presentacion> presentaciones) {
        setCategorias(categorias);
        setSubcategorias(subcategorias);
        setArticulos(articulos);
        setPresentaciones(presentaciones);
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

}
