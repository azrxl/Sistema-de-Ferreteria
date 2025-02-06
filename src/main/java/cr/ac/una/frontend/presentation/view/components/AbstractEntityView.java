package cr.ac.una.frontend.presentation.view.components;

import cr.ac.una.frontend.presentation.view.views.BaseView;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public abstract class AbstractEntityView<T> extends BaseView {
    protected AbstractTableModel<T> tableModel;

    public AbstractEntityView(AbstractTableModel<T> tableModel) {
        this.tableModel = tableModel;
        table.setModel(tableModel);

        // Configurar botones comunes
        guardarButton.addActionListener(e -> guardar());
        buscarButton.addActionListener(e -> buscar());
        eliminarButton.addActionListener(e -> eliminar());
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Verifica que sea un doble clic
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) { // Verifica que se haya seleccionado una fila
                        T entidad = tableModel.getItemAt(selectedRow);
                        onElementoSeleccionado(entidad);
                    }
                }
            }
        });
    }

    protected abstract void onElementoSeleccionado(T entidad);

    public void cargarEntidades(List<T> entidades) {
        tableModel.setItems(entidades);
    }

    protected void guardar() {
        try {
            T entidad = obtenerEntidadDesdeFormulario();
            agregarEntidad(entidad);
            cargarEntidades(obtenerEntidades());
            limpiarFormulario();
            JOptionPane.showMessageDialog(mainPanel, "Guardado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainPanel, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    protected void buscar() {
        try {
            String idBusqueda = busquedaIDField.getText().toLowerCase().trim();
            String nombreBusqueda = busquedaNombreField.getText().toLowerCase().trim();
            List<T> resultados = buscarEntidades(idBusqueda, nombreBusqueda);
            if (resultados.isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "No se encontraron coincidencias.", "Información", JOptionPane.INFORMATION_MESSAGE);
            } else {
                cargarEntidades(resultados);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainPanel, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    protected void eliminar() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainPanel, "Debe seleccionar un elemento para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            T entidadSeleccionada = tableModel.getItemAt(selectedRow);
            eliminarEntidad(entidadSeleccionada);
            cargarEntidades(obtenerEntidades());
            JOptionPane.showMessageDialog(mainPanel, "Eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainPanel, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    protected abstract T obtenerEntidadDesdeFormulario() throws Exception;

    protected abstract void agregarEntidad(T entidad) throws Exception;

    protected abstract List<T> buscarEntidades(String id, String nombre) throws Exception;

    protected abstract void eliminarEntidad(T entidad) throws Exception;

    protected abstract List<T> obtenerEntidades() throws Exception;

    protected abstract void limpiarFormulario();
}
