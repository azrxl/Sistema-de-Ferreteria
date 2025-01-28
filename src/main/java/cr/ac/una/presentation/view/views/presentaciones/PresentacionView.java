package cr.ac.una.presentation.view.views.presentaciones;

import cr.ac.una.presentation.controller.Controller;
import cr.ac.una.presentation.view.components.AbstractTableModel;
import cr.ac.una.logic.objects.Articulo;
import cr.ac.una.logic.objects.Presentacion;

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

class PresentacionTableModel extends AbstractTableModel<Presentacion> {
    public PresentacionTableModel(List<Presentacion> items) {
        super(new String[]{"Capacidad", "Unidad", "Compra", "Venta", "Cantidad"}, items);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Presentacion item = items.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> item.getCapacidad(); // Capacidad
            case 1 -> item.getUnidad(); // Unidad
            case 2 -> item.getPrecioCompra() + "$"; // Precio Compra
            case 3 -> item.getPrecioVenta() + "$"; // Precio Venta
            case 4 -> item.getCantidad(); // Cantidad
            default -> null;
        };
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) throws NumberFormatException {
        Presentacion item = items.get(rowIndex);
        try {
            switch (columnIndex) {
                case 0 -> item.setCapacidad(aValue.toString()); // Editar Capacidad
                case 2 -> item.setPrecioCompra(Double.parseDouble(aValue.toString())); // Editar Precio Compra
                case 3 -> item.setPrecioVenta(Double.parseDouble(aValue.toString())); // Editar Precio Venta
                case 4 -> item.setCantidad(Integer.parseInt(aValue.toString())); // Editar Cantidad
            }
            fireTableCellUpdated(rowIndex, columnIndex); // Notificar cambio en la tabla
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Formato inválido para el campo.");
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // Solo los campos de Capacidad, Precio Compra, Precio Venta y Cantidad son editables
        return columnIndex == 0 || columnIndex == 2 || columnIndex == 3 || columnIndex == 4;
    }
}
