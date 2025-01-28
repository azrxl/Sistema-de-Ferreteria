package cr.ac.una.presentation.views.concrete_views;

import cr.ac.una.presentation.views.components.abstract_view.AbstractEntityView;
import cr.ac.una.presentation.views.components.tables.CategoriaTableModel;
import cr.ac.una.proxy.Categoria;

import java.util.ArrayList;
import java.util.List;

public class CategoriaView extends AbstractEntityView<Categoria> {
    public CategoriaView() {
        super(new CategoriaTableModel(new ArrayList<>()));
    }

    @Override
    protected Categoria obtenerEntidadDesdeFormulario() throws Exception {
        String id = codigoField.getText();
        String nombre = nombreField.getText();
        String descripcion = descripcionField.getText();

        if (id.isEmpty() || nombre.isEmpty() || descripcion.isEmpty()) {
            throw new Exception("Todos los campos son requeridos.");
        }

        return new Categoria(id, nombre, descripcion);
    }

    @Override
    protected void agregarEntidad(Categoria categoria) throws Exception {
        controller.agregarCategoria(categoria);
    }

    @Override
    protected List<Categoria> buscarEntidades(String id, String nombre) throws Exception {
        return controller.buscarCategorias(id, nombre);
    }

    @Override
    protected void eliminarEntidad(Categoria categoria) throws Exception {
        controller.eliminarCategoria(categoria);
    }

    @Override
    protected List<Categoria> obtenerEntidades() throws Exception {
        return controller.getCategorias();
    }

    @Override
    protected void limpiarFormulario() {
        codigoField.setText("");
        nombreField.setText("");
        descripcionField.setText("");
    }

    @Override
    protected void onElementoSeleccionado(Categoria entidad) {
        controller.initSubcategoriaView(entidad);
    }
}
