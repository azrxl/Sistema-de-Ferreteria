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
    private JComboBox<String> unidadBox;

    private final PresentacionTableModel tableModel;

    private Controller controller;

    private Articulo articuloSeleccionado;

    public PresentacionView() {
        tableModel = new PresentacionTableModel(new ArrayList<>(),null);
        presentacionesTable.setModel(tableModel);
        agregarButton.addActionListener(e -> agregarPresentacion());
        eliminarButton.addActionListener(e -> eliminarPresentacion());
    }

    public JPanel getMainPanel() {
        return presentaciones;
    }

    public void setController(Controller controller) {
        this.controller = controller;
        ((PresentacionView.PresentacionTableModel) presentacionesTable.getModel()).controller = controller; // Conecta el controlador al modelo
    }

    public void cargarUnidades(List<String> unidades) {
        // Limpiar el JComboBox
        unidadBox.removeAllItems();

        // Agregar una opción predeterminada
        unidadBox.addItem("Seleccione una unidad");

        // Agregar las unidades válidas al JComboBox
        for (String unidad : unidades) {
            unidadBox.addItem(unidad);
        }

        // Seleccionar la primera opción por defecto
        unidadBox.setSelectedIndex(0);
    }

    private void agregarPresentacion() {
        String capacidad = capacidadField.getText().trim();
        String unidad = (String) unidadBox.getSelectedItem(); // Obtener la unidad seleccionada

        if (articuloSeleccionado == null) {
            JOptionPane.showMessageDialog(presentaciones, "Seleccione un artículo", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar que se haya seleccionado una unidad válida
        if (unidad == null || unidad.equals("Seleccione una unidad")) {
            JOptionPane.showMessageDialog(presentaciones, "Seleccione una unidad válida.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (capacidad.isEmpty()) {
            JOptionPane.showMessageDialog(presentaciones, "La capacidad es requerida.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int capacidadNum = Integer.parseInt(capacidad); // Validar que capacidad sea numérico
            Presentacion presentacion = new Presentacion(unidad, capacidad, articuloSeleccionado.getId(), 0.0, 0.0, 0);
            controller.agregarPresentacion(presentacion);
            cargarPresentaciones(controller.getPresentacionesPorArticulo(articuloSeleccionado));
            capacidadField.setText("");
            unidadBox.setSelectedIndex(0); // Reiniciar la selección del JComboBox
            JOptionPane.showMessageDialog(presentaciones, "Presentación agregada con éxito");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(presentaciones, "La capacidad debe ser un entero numérico.", "Error", JOptionPane.ERROR_MESSAGE);
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

    static class PresentacionTableModel extends AbstractTableModel<Presentacion> {
        private Controller controller;

        public PresentacionTableModel(List<Presentacion> items, Controller controller) {
            super(new String[]{"Capacidad", "Unidad", "Compra", "Venta", "Cantidad"}, items);
            this.controller = controller;
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
            Presentacion presentacion = getItemAt(rowIndex);
            String valorOriginal = null;
            try {
                switch (columnIndex) {
                    case 0: // Capacidad
                        valorOriginal = presentacion.getCapacidad();
                        presentacion.setCapacidad(aValue.toString());
                        break;

                    case 2: // Precio Compra
                        presentacion.setPrecioCompra(Double.parseDouble(aValue.toString()));
                        break;

                    case 3: // Precio Venta
                        presentacion.setPrecioVenta(Double.parseDouble(aValue.toString()));
                        break;

                    case 4: // Cantidad
                        presentacion.setCantidad(Integer.parseInt(aValue.toString()));
                        break;
                }

                // Intentar actualizar en el servicio
                controller.actualizarPresentacion(presentacion);

            } catch (Exception e) {
                if (e instanceof NumberFormatException) {
                    throw new NumberFormatException("Formato inválido para el campo.");
                }
                if (valorOriginal != null) {
                    presentacion.setCapacidad(valorOriginal);
                }
                fireTableDataChanged(); // Restaurar la tabla
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            // Solo los campos de Capacidad, Precio Compra, Precio Venta y Cantidad son editables
            return columnIndex == 0 || columnIndex == 2 || columnIndex == 3 || columnIndex == 4;
        }
    }
}


