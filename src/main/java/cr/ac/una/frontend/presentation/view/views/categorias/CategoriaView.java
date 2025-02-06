package cr.ac.una.frontend.presentation.view.views.categorias;

import cr.ac.una.frontend.presentation.controller.Controller;
import cr.ac.una.frontend.presentation.view.components.AbstractEntityView;
import cr.ac.una.frontend.presentation.view.components.AbstractTableModel;
import cr.ac.una.proxy.objects.Categoria;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaView extends AbstractEntityView<Categoria> {
    public CategoriaView() {
        super(new CategoriaTableModel(new ArrayList<>(), null)); // Inicialmente pasa null para el controlador
    }

    @Override
    public void setController(Controller controller) {
        super.setController(controller);
        ((CategoriaTableModel) table.getModel()).controller = controller; // Conecta el controlador al modelo
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

    static class CategoriaTableModel extends AbstractTableModel<Categoria> {
        private Controller controller; // Referencia al controlador

        public CategoriaTableModel(List<Categoria> categorias, Controller controller) {
            super(new String[]{"ID", "Nombre", "Descripción"}, categorias);
            this.controller = controller; // Inicializar el controlador
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
            String nombrePredeterminado = categoria.getNombre();
            String nuevoValor = aValue.toString();

            switch (columnIndex) {
                case 1: // Editar Nombre
                    try {
                        categoria.setNombre(nuevoValor); // Actualizar temporalmente
                        this.controller.actualizarCategoria(categoria); // Intentar actualizar en el servicio
                    } catch (Exception e) {
                        categoria.setNombre(nombrePredeterminado);
                        fireTableDataChanged(); // Restaurar el valor original en la tabla
                        JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case 2: categoria.setDescripcion(nuevoValor);
            }

            fireTableCellUpdated(rowIndex, columnIndex); // Reflejar cambios en la tabla
        }
    }


}

