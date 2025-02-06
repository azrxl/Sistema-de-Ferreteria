package cr.ac.una.frontend.presentation.model;

import cr.ac.una.proxy.objects.*;

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

    public void initData(List<Categoria> categorias, List<Subcategoria> subcategorias, List<Articulo> articulos, List<Presentacion> presentaciones, List<String> unidades) {
        setCategorias(categorias);
        setSubcategorias(subcategorias);
        setArticulos(articulos);
        setPresentaciones(presentaciones);
        setUnidades(unidades);
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

}
