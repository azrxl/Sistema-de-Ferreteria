package cr.ac.una.presentation.views.concrete_views;

import cr.ac.una.presentation.controller.Controller;
import cr.ac.una.presentation.views.components.tables.PresentacionTableModel;
import cr.ac.una.proxy.Articulo;
import cr.ac.una.proxy.Presentacion;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class PresentacionView {

    private JPanel presentaciones;
    private JScrollPane tableScrollPanel;
    private JTable presentacionesTable;
    private JPanel agregarPanel;
    private JLabel capacidadLabel;
    private JLabel unidadLabel;
    private JTextField capacidadField;
    private JTextField unidadField;
    private JButton agregarButton;
    private JButton eliminarButton;

    private final PresentacionTableModel tableModel;

    private Controller controller;

    private Articulo articuloSeleccionado;

    public PresentacionView() {
        tableModel = new PresentacionTableModel(new ArrayList<>());
        presentacionesTable.setModel(tableModel);
        agregarButton.addActionListener(e -> agregarPresentacion());
        eliminarButton.addActionListener(e -> eliminarPresentacion());
    }

    public JPanel getMainPanel() {
        return presentaciones;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    private void agregarPresentacion() {
        String capacidad = capacidadField.getText().trim();
        String unidad = unidadField.getText().trim();
        if(articuloSeleccionado == null) {
            JOptionPane.showMessageDialog(presentaciones, "Seleccione una articulo", "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (capacidad.isEmpty() || unidad.isEmpty()) {
            JOptionPane.showMessageDialog(presentaciones, "Todos los campos son requeridos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int capacidadNum = Integer.parseInt(capacidad); // Validar que capacidad sea numérico
            Presentacion presentacion = new Presentacion(unidad, capacidad, articuloSeleccionado.getId(), 0.0, 0.0, 0);
            controller.agregarPresentacion(presentacion);
            cargarPresentaciones(controller.getPresentacionesPorArticulo(articuloSeleccionado));
            capacidadField.setText("");
            unidadField.setText("");
            JOptionPane.showMessageDialog(presentaciones,"Presentacion agregada con exito");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(presentaciones, "La capacidad debe ser un entero numerico.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(presentaciones, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarPresentacion() {
        int selectedRow = presentacionesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(presentaciones, "Seleccione una fila para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Presentacion presentacionSeleccionada = tableModel.getItemAt(selectedRow);
            controller.eliminarPresentacion(presentacionSeleccionada);
            cargarPresentaciones(controller.getPresentacionesPorArticulo(articuloSeleccionado));
            JOptionPane.showMessageDialog(presentaciones, "Eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(presentaciones, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cargarPresentaciones(List<Presentacion> presentaciones) {
        tableModel.setItems(presentaciones);
    }

    public void setSubcategoriaSeleccionada(Articulo entidad) {
        this.articuloSeleccionado = entidad;
        cargarPresentaciones(controller.getPresentacionesPorArticulo(articuloSeleccionado));
    }

    public void onElementoSeleccionado(Presentacion presentacion) {
        //TODO: Posible futura implementacion.
    }
}
