package cr.ac.una.backend.data;

import cr.ac.una.proxy.objects.*;

import java.util.ArrayList;
import java.util.List;

public class Data {
    private List<Categoria> categorias;
    private List<Subcategoria> subcategorias;
    private List<Articulo> articulos;
    private List<Presentacion> presentaciones;
    private List<String> unidades;
    private List<User> usuarios;

    public Data() {
        categorias = new ArrayList<>();
        subcategorias = new ArrayList<>();
        articulos = new ArrayList<>();
        presentaciones = new ArrayList<>();
        unidades = new ArrayList<>();
        usuarios = new ArrayList<>();
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
    public List<User> getUsuarios() {
        return usuarios;
    }
    public void setUsuarios(List<User> usuarios) {
        this.usuarios = usuarios;
    }

}
