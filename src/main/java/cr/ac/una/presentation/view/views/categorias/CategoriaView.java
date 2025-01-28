package cr.ac.una.presentation.view.views.categorias;

import cr.ac.una.presentation.view.components.AbstractEntityView;
import cr.ac.una.presentation.view.components.AbstractTableModel;
import cr.ac.una.logic.objects.Categoria;

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

class CategoriaTableModel extends AbstractTableModel<Categoria> {
    public CategoriaTableModel(List<Categoria> categorias) {
        super(new String[]{"ID", "Nombre", "Descripción"}, categorias);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Categoria categoria = getItemAt(rowIndex);
        return switch (columnIndex) {
            case 0 -> categoria.getId(); // Columna ID
            case 1 -> categoria.getNombre(); // Columna Nombre
            case 2 -> categoria.getDescripcion(); // Columna Descripción
            default -> null;
        };
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Categoria categoria = getItemAt(rowIndex);
        switch (columnIndex) {
            case 1 -> categoria.setNombre((String) aValue); // Actualizar Nombre
            case 2 -> categoria.setDescripcion((String) aValue); // Actualizar Descripción
        }
        fireTableCellUpdated(rowIndex, columnIndex); // Notificar cambios
    }
}
