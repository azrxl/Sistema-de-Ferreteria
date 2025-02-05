package cr.ac.una.presentation.controller;

import cr.ac.una.logic.Service;
import cr.ac.una.logic.objects.*;
import cr.ac.una.presentation.model.Model;
import cr.ac.una.presentation.view.views.articulos.ArticuloView;
import cr.ac.una.presentation.view.views.categorias.CategoriaView;
import cr.ac.una.presentation.view.views.presentaciones.PresentacionView;
import cr.ac.una.presentation.view.views.subcategorias.SubcategoriaView;

import javax.swing.*;
import java.util.List;

public class Controller {
    private final Model model;
    private final CategoriaView categoriaView;
    private final SubcategoriaView subcategoriaView;
    private final ArticuloView articuloView;
    private final PresentacionView presentacionView;
    private final JTabbedPane tabbedPane; // Agregamos una referencia al JTabbedPane

    public Controller(Model model, CategoriaView categoriaView, SubcategoriaView subcategoriaView, ArticuloView articuloView, JTabbedPane tabbedPane) {
        this.model = model;
        this.categoriaView = categoriaView;
        this.subcategoriaView = subcategoriaView;
        this.articuloView = articuloView;
        this.presentacionView = articuloView.getPresentacionView();
        this.tabbedPane = tabbedPane; // Inicializamos el JTabbedPane
    }

    public void disableSubcategoriasTab() {
        int index = tabbedPane.indexOfTab("Subcategorias");
        if (index != -1) {
            tabbedPane.setEnabledAt(index, false); // Deshabilita la pestaña
        }
    }

    public void enableSubcategoriasTab() {
        int index = tabbedPane.indexOfTab("Subcategorias");
        if (index != -1) {
            tabbedPane.setEnabledAt(index, true); // Habilita la pestaña
        }
    }

    public void enableArticulosTab() {
        int index = tabbedPane.indexOfTab("Articulos");
        if (index != -1) {
            tabbedPane.setEnabledAt(index, true);
        }
    }

    public void disableArticulosTab() {
        int index = tabbedPane.indexOfTab("Articulos");
        if (index != -1) {
            tabbedPane.setEnabledAt(index, false);
        }
    }

    public void initApp() {
        categoriaView.setController(this);
        subcategoriaView.setController(this);
        articuloView.setController(this);
        presentacionView.setController(this);

        // Inicializar datos en el modelo
        model.initData();

        // Cargar las categorías iniciales en la vista de categorías
        categoriaView.cargarEntidades(model.getCategorias());
        disableSubcategoriasTab();
        disableArticulosTab();

        //Cargar las unidades válidas en las presentaciones
        presentacionView.cargarUnidades(model.getUnidades());
    }

    // Métodos para manejar categorías
    public List<Categoria> getCategorias() {
        return model.getCategorias();
    }


    public List<String> getUnidades() {
        return model.getUnidades();
    }

    public Categoria getCategoriaById(String categoriaID) {
        return model.getCategorias().stream().filter(c -> c.getId().equals(categoriaID)).findFirst().orElse(null);
    }

    public void agregarCategoria(Categoria categoria) throws Exception {
        Service.instance().createCategoria(categoria);
    }

    public void actualizarCategoria(Categoria categoria) throws Exception {
        Service.instance().updateCategoria(categoria);
    }

    public void eliminarCategoria(Categoria categoria) throws Exception {
        Service.instance().deleteCategoria(categoria.getId());
    }

    public List<Categoria> buscarCategorias(String idBusqueda, String nombreBusqueda) {
        return model.getCategorias().stream()
                .filter(c -> c.getId().toLowerCase().contains(idBusqueda.toLowerCase()) &&
                        c.getNombre().toLowerCase().contains(nombreBusqueda.toLowerCase()))
                .toList();
    }

    // Métodos para manejar subcategorías
    public List<Subcategoria> getSubcategoriasPorCategoria(Categoria categoria) {
        return model.getSubcategorias().stream()
                .filter(subcategoria -> subcategoria.getCategoriaID().equals(categoria.getId()))
                .toList();
    }

    public void agregarSubcategoria(Subcategoria subcategoria) throws Exception {
        Service.instance().createSubcategoria(subcategoria, subcategoria.getCategoriaID());
    }

    public void actualizarSubcategoria(Subcategoria subcategoria) throws Exception {
        Service.instance().updateSubcategoria(subcategoria);
    }

    public void eliminarSubcategoria(Subcategoria subcategoria) throws Exception {
        Service.instance().deleteSubcategoria(subcategoria.getId());
    }

    public List<Subcategoria> buscarSubcategorias(Categoria categoria, String id, String nombre) {
        return model.getSubcategorias().stream()
                .filter(subcategoria -> subcategoria.getCategoriaID().equals(categoria.getId()))
                .filter(subcategoria -> subcategoria.getId().toLowerCase().contains(id.toLowerCase()) &&
                        subcategoria.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .toList();
    }

    public void initSubcategoriaView(Categoria entidad) {
        subcategoriaView.setCategoriaSeleccionada(entidad);

        if (entidad != null) {
            enableSubcategoriasTab();
            tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Subcategorias"));
        } else {
            disableSubcategoriasTab();
        }
    }

    public List<Articulo> getArticulosPorSubcategoria(Subcategoria subcategoria) {
        return model.getArticulos().stream()
                .filter(articulo -> articulo.getSubcategoriaID().equals(subcategoria.getId()))
                .toList();
    }

    public void agregarArticulo(Articulo articulo) throws Exception {
        Service.instance().createArticulo(articulo, articulo.getSubcategoriaID());
    }

    public void actualizarArticulo(Articulo articulo) throws Exception {
        Service.instance().updateArticulo(articulo);
    }

    public void eliminarArticulo(Articulo articulo) throws Exception {
        Service.instance().deleteArticulo(articulo.getId());
    }

    public List<Articulo> buscarArticulos(Subcategoria subcategoria, String id, String nombre) {
        return model.getArticulos().stream()
                .filter(articulo -> articulo.getSubcategoriaID().equals(subcategoria.getId()))
                .filter(articulo -> articulo.getId().toLowerCase().contains(id.toLowerCase()) &&
                        articulo.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .toList();
    }


    public void initArticuloView(Subcategoria entidad) {
        articuloView.setSubcategoriaSeleccionada(entidad);

        if (entidad != null) {
            enableSubcategoriasTab();
            tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Articulos"));
        } else {
            disableArticulosTab();
        }
    }

    public List<Presentacion> getPresentacionesPorArticulo(Articulo articulo) {
        return model.getPresentaciones().stream().filter(p -> p.getArticuloID().equals(articulo.getId())).toList();
    }

    public void agregarPresentacion(Presentacion presentacion) throws Exception {
        Service.instance().createPresentacion(presentacion, presentacion.getArticuloID());
    }

    public void actualizarPresentacion(Presentacion presentacion) throws Exception {
        Service.instance().updatePresentacion(presentacion);
    }

    public void eliminarPresentacion(Presentacion presentacion) throws Exception {
        Service.instance().deletePresentacion(presentacion);
    }

    public void initPresentacionView(Articulo entidad) {
        presentacionView.setSubcategoriaSeleccionada(entidad);
    }

    public Factura calcularFactura(List<CarritoItem> items) {
        return model.generarFactura(items);  // Se delega el cálculo al modelo
    }

    public void confirmarPedido(List<CarritoItem> items) throws Exception {
        for (CarritoItem item : items) {
            Presentacion p = item.getPresentacion();
            int nuevaExistencia = p.getCantidad() - item.getCantidad();
            p.setCantidad(nuevaExistencia);
            actualizarPresentacion(p);
        }
    }
}
