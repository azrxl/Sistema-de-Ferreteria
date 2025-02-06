package cr.ac.una.backend.logic;

import cr.ac.una.backend.data.Data;
import cr.ac.una.backend.data.XMLHandler;
import cr.ac.una.proxy.IService;
import cr.ac.una.proxy.objects.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Service implements IService {
    private static Service theInstance;
    private final Data data;
    private final XMLHandler xmlHandler;

    private Service() {
            data = new Data();
            xmlHandler = new XMLHandler();
    }

    public static Service instance() {
        if (theInstance == null) {
            theInstance = new Service();
        }
        return theInstance;
    }

    //--------------------------------------------------------------------------

    public void cargar() throws Exception {
        try {
            data.setCategorias(xmlHandler.cargarCategorias());
            data.setSubcategorias(xmlHandler.cargarSubcategorias());
            data.setArticulos(xmlHandler.cargarArticulos());
            data.setPresentaciones(xmlHandler.cargarPresentaciones());
            data.setUnidades(xmlHandler.cargarUnidades());
            data.setUsuarios(xmlHandler.cargarUsuarios());
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new Exception("Error al cargar los datos");
        }
    }

    public void guardar() throws Exception {
        try {
            xmlHandler.guardar(data);
        } catch (TransformerException | ParserConfigurationException e) {
            throw new Exception("Error al guardar los datos");
        }
    }

    //--------------------------------------------------------------------------


    public List<Categoria> getCategorias() {
        return data.getCategorias();
    }
    public List<Subcategoria> getSubcategorias() {
        return data.getSubcategorias();
    }
    public List<Articulo> getArticulos() {
        return data.getArticulos();
    }
    public List<Presentacion> getPresentaciones() {
        return data.getPresentaciones();
    }
    public List<String> getUnidades() {
        return data.getUnidades();
    }
    public List<User> getUsuarios() { return data.getUsuarios(); }

    public void cleanData() {
        data.setCategorias(new ArrayList<>());
        data.setSubcategorias(new ArrayList<>());
        data.setArticulos(new ArrayList<>());
        data.setPresentaciones(new ArrayList<>());
    }

    //--------------------------------------------------------------------------

    private <T> T read(List<T> list, Predicate<T> condition) {
        return list.stream().filter(condition).findFirst().orElse(null);
    }

    private <T> void create(List<T> list, T item, Predicate<T> existsCondition, String entityName) throws Exception {
        if (exists(list, existsCondition)) {
            throw new Exception("Un elemento ya existe con el mismo codigo que " + entityName);
        }
        list.add(item);
    }

    private <T> void update(List<T> list, T item, Predicate<T> matchCondition, String entityName) throws Exception {
        if (!exists(list, matchCondition)) {
            throw new Exception(entityName + " no encontrado.");
        }
        for (int i = 0; i < list.size(); i++) {
            if (matchCondition.test(list.get(i))) {
                list.set(i, item);
                return;
            }
        }
    }

    private <T> void delete(List<T> list, Predicate<T> matchCondition, String entityName) throws Exception {
        if (!exists(list, matchCondition)) {
            throw new Exception(entityName + " no encontrado.");
        }
        list.removeIf(matchCondition);
    }

    private <T> boolean exists(List<T> list, Predicate<T> condition) {
        return list.stream().anyMatch(condition);
    }

    //--------------------------------------------------------------------------
    public Categoria getCategoriaById(String categoriaId) {
        return read(data.getCategorias(), c -> c.getId().equals(categoriaId));
    }

    public Subcategoria getSubcategoriaById(String subcategoriaId) {
        return read(data.getSubcategorias(), s -> s.getId().equals(subcategoriaId));
    }

    public Articulo getArticuloById(String articuloId) {
        return read(data.getArticulos(), a -> a.getId().equals(articuloId));
    }

    public Presentacion getPresentacionByParam(String unidad, String capacidad) {
        return data.getPresentaciones().stream()
                .filter(p -> p.getUnidad().equals(unidad) && p.getCapacidad().equals(capacidad))
                .findFirst()
                .orElse(null);
    }

    //--------------------------------------------------------------------------

    public void createCategoria(Categoria categoria) throws Exception {
        create(data.getCategorias(), categoria, c -> c.getId().equals(categoria.getId()) || c.getNombre().equals(categoria.getNombre()), "Categoría");
    }

    public void createSubcategoria(Subcategoria subcategoria, String categoriaId) throws Exception {
        Categoria categoria = getCategoriaById(categoriaId);
        if (categoria == null) {
            throw new Exception("La categoría con ID " + categoriaId + " no existe");
        }
        subcategoria.setCategoriaID(categoriaId);
        boolean exists = data.getSubcategorias().stream()
                .anyMatch(s -> s.getNombre().equals(subcategoria.getNombre()));
        if (exists) {
            throw new Exception("La subcategoría con nombre " + subcategoria.getNombre() + " ya existe.");
        }
        create(data.getSubcategorias(), subcategoria, s -> s.getId().equals(subcategoria.getId()) || s.getNombre().equals(subcategoria.getNombre()), "Subcategoría");
    }

    public void createArticulo(Articulo articulo, String subcategoriaId) throws Exception {
        Subcategoria subcategoria = getSubcategoriaById(subcategoriaId);
        if (subcategoria == null) {
            throw new Exception("La subcategoría con ID " + subcategoriaId + " no existe");
        }
        boolean existsInSubcategoria = data.getArticulos().stream()
                .anyMatch(a -> a.getSubcategoriaID().equals(subcategoriaId) && a.getNombre().equals(articulo.getNombre()));
        if (existsInSubcategoria) {
            throw new Exception("El artículo con nombre " + articulo.getNombre() + " ya existe en la subcategoría " + subcategoriaId);
        }
        articulo.setSubcategoriaID(subcategoriaId);
        create(data.getArticulos(), articulo, a -> a.getId().equals(articulo.getId()), "Artículo");
    }

    public void createPresentacion(Presentacion presentacion, String articuloId) throws Exception {
        Articulo articulo = getArticuloById(articuloId);
        if (articulo == null) {
            throw new Exception("El artículo con ID " + articuloId + " no existe");
        }
        List<String> unidadesValidas = data.getUnidades();
        if (!unidadesValidas.contains(presentacion.getUnidad())) {
            throw new Exception("La unidad " + presentacion.getUnidad() + " no es válida. Las unidades válidas son: " + unidadesValidas);
        }
        boolean exists = data.getPresentaciones().stream().anyMatch(p -> p.equals(presentacion));
        if (exists) {
            throw new Exception("La presentación con unidad " + presentacion.getUnidad() + " y cantidad " + presentacion.getCapacidad() + " ya existe para el artículo " + articuloId);
        }
        presentacion.setArticuloID(articuloId);
        create(data.getPresentaciones(), presentacion, p -> p.equals(presentacion), "Presentación");
    }

    //--------------------------------------------------------------------------

    public void updateCategoria(Categoria categoria) throws Exception {
        // Verificar si el nuevo nombre ya existe en otra categoría
        boolean nombreExiste = data.getCategorias().stream()
                .anyMatch(c -> c.getNombre().equalsIgnoreCase(categoria.getNombre()) && !c.getId().equals(categoria.getId()));

        if (nombreExiste) {
            throw new Exception("Ya existe una categoría con el nombre '" + categoria.getNombre() + "'.");
        }

        // Proceder con la actualización
        update(data.getCategorias(), categoria, c -> c.getId().equals(categoria.getId()), "Categoría");
    }

    public void updateSubcategoria(Subcategoria subcategoria) throws Exception {
        boolean nombreExiste = data.getSubcategorias().stream()
                .anyMatch(s -> s.getNombre().equalsIgnoreCase(subcategoria.getNombre()) && !s.getId().equals(subcategoria.getId()));

        if (nombreExiste) {
            throw new Exception("Ya existe una subcategoria con el nombre '" + subcategoria.getNombre() + "'.");
        }
        update(data.getSubcategorias(), subcategoria, s -> s.getId().equals(subcategoria.getId()), "Subcategoría");
    }

    public void updateArticulo(Articulo articulo) throws Exception {
        boolean nombreExiste = data.getArticulos().stream()
                .anyMatch(a -> a.getNombre().equalsIgnoreCase(articulo.getNombre()) && !a.getId().equals(articulo.getId()) && a.getSubcategoriaID().equals(articulo.getSubcategoriaID()));

        if (nombreExiste) {
            throw new Exception("Ya existe un articulo con el nombre '" + articulo.getNombre() + "'.");
        }
        update(data.getArticulos(), articulo, a -> a.getId().equals(articulo.getId()), "Artículo");
    }

    //TODO: Modificar validacion para repetidos
    public void updatePresentacion(Presentacion presentacion) throws Exception {
        boolean existe = data.getPresentaciones().stream()
                .anyMatch(p -> p != presentacion && // Excluye la presentación actual por referencia
                        p.getArticuloID().equals(presentacion.getArticuloID()) &&
                        p.getUnidad().equals(presentacion.getUnidad()) &&
                        p.getCapacidad().equals(presentacion.getCapacidad()));
        if (existe) {
            throw new Exception("Ya existe una presentación con la misma capacidad y unidad para este artículo.");
        }
        update(data.getPresentaciones(), presentacion, p -> p.equals(presentacion), "Presentación");
    }

    //--------------------------------------------------------------------------

    public void deleteCategoria(String categoriaId) throws Exception {
        // Verificar si tiene subcategorías asociadas
        boolean hasSubcategories = data.getSubcategorias().stream()
                .anyMatch(s -> s.getCategoriaID().equals(categoriaId));
        if (hasSubcategories) {
            throw new Exception("No se puede eliminar la categoría porque tiene subcategorías asociadas");
        }
        delete(data.getCategorias(), c -> c.getId().equals(categoriaId), "Categoría");
    }

    public void deleteSubcategoria(String subcategoriaId) throws Exception {
        // Verificar si tiene artículos asociados
        boolean hasArticles = data.getArticulos().stream()
                .anyMatch(a -> a.getSubcategoriaID().equals(subcategoriaId));
        if (hasArticles) {
            throw new Exception("No se puede eliminar la subcategoría porque tiene artículos asociados");
        }
        delete(data.getSubcategorias(), s -> s.getId().equals(subcategoriaId), "Subcategoría");
    }

    public void deleteArticulo(String articuloId) throws Exception {
        // Verificar si tiene presentaciones asociadas
        boolean hasPresentations = data.getPresentaciones().stream()
                .anyMatch(p -> p.getArticuloID().equals(articuloId));
        if (hasPresentations) {
            throw new Exception("No se puede eliminar el artículo porque tiene presentaciones asociadas");
        }
        delete(data.getArticulos(), a -> a.getId().equals(articuloId), "Artículo");
    }

    public void deletePresentacion(Presentacion presentacion) throws Exception {
        delete(data.getPresentaciones(), p -> p.equals(presentacion), "Presentación");
    }

    //-----------------------------------------------------------------------

    private double getSubtotal(Pedido pedido) {
        return pedido.getItems().stream()
                .mapToDouble(item -> item.getPresentacion().getPrecioVenta() * item.getCantidad())
                .sum();
    }

    private double getDescuentoUnidades(Pedido pedido) {
        return pedido.getItems().stream()
                .filter(item -> item.getCantidad() > 10)
                .mapToDouble(item -> item.getPresentacion().getPrecioVenta() * item.getCantidad() * 0.10)
                .sum();
    }

    private double getDescuentoDiferentes(Pedido pedido, double subtotal) {
        long articulosDiferentes = pedido.getItems().stream()
                .map(item -> item.getPresentacion().getArticuloID())
                .distinct()
                .count();

        return (articulosDiferentes > 10) ? subtotal * 0.05 : 0;
    }

    private double getDescuentoMonto(double subtotal) {
        return (subtotal > 5000) ? subtotal * 0.075 : 0;
    }

    public Factura generarFactura(Pedido pedido) {
        double subtotal = getSubtotal(pedido);
        double descuentoUnidades = getDescuentoUnidades(pedido);
        double descuentoDiferentes = getDescuentoDiferentes(pedido, subtotal);
        double descuentoMonto = getDescuentoMonto(subtotal);
        double totalDescuento = descuentoUnidades + descuentoDiferentes + descuentoMonto;
        double totalFinal = subtotal - totalDescuento;

        return new Factura(subtotal, descuentoUnidades, descuentoDiferentes, descuentoMonto, totalDescuento, totalFinal);
    }

    @Override
    public User login(User user) throws Exception {
        return null;
    }

    @Override
    public void logout(User loggedUser) throws Exception {

    }

    @Override
    public void confirmarPedido(List<CarritoItem> items) {

    }
}