package cr.ac.una.presentation.view.views.presentaciones;

import cr.ac.una.logic.objects.CarritoItem;
import cr.ac.una.logic.objects.Factura;
import cr.ac.una.presentation.controller.Controller;
import cr.ac.una.presentation.view.components.AbstractTableModel;
import cr.ac.una.logic.objects.Articulo;
import cr.ac.una.logic.objects.Presentacion;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class PresentacionView {

    private JPanel presentacionesPanel;
    private JScrollPane tableScrollPanel;
    private JTable presentacionesTable;
    private JPanel agregarPanel;
    private JLabel capacidadLabel;
    private JLabel unidadLabel;
    private JTextField capacidadField;
    private JButton agregarButton;
    private JButton eliminarButton;
    private JComboBox<String> unidadBox;
    private JPanel pedidoPanel;
    private JTable carritoTable;
    private JPanel accionPanel;
    private JButton eliminarPedidoButton;
    private JScrollPane carritoScrollPane;
    private JButton confirmarPedidoButton1;
    private JPanel mainPanel;

    private final PresentacionTableModel presentacionTableModel;
    private final CarritoTableModel carritoTableModel;

    private Controller controller;

    private Articulo articuloSeleccionado;

    public PresentacionView() {
        presentacionTableModel = new PresentacionTableModel(new ArrayList<>(),null);
        carritoTableModel = new CarritoTableModel(new ArrayList<>());

        presentacionesTable.setModel(presentacionTableModel);
        carritoTable.setModel(carritoTableModel);

        agregarButton.addActionListener(e -> agregarPresentacion());
        eliminarButton.addActionListener(e -> eliminarPresentacion());
        eliminarPedidoButton.addActionListener(e -> eliminarDelCarrito());
        confirmarPedidoButton1.addActionListener(e -> confirmarPedido());
        presentacionesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    agregarAlCarrito();
                }
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setController(Controller controller) {
        this.controller = controller;
        ((PresentacionView.PresentacionTableModel) presentacionesTable.getModel()).controller = controller; // Conecta el controlador al modelo
    }

    public void cargarUnidades(List<String> unidades) {
        // Limpiar el JComboBox
        unidadBox.removeAllItems();

        // Agregar una opción predeterminada
        unidadBox.addItem("-Seleccione una unidad-");

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
            JOptionPane.showMessageDialog(presentacionesPanel, "Seleccione un artículo", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar que se haya seleccionado una unidad válida
        if (unidad == null || unidad.equals("Seleccione una unidad")) {
            JOptionPane.showMessageDialog(presentacionesPanel, "Seleccione una unidad válida.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (capacidad.isEmpty()) {
            JOptionPane.showMessageDialog(presentacionesPanel, "La capacidad es requerida.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int capacidadNum = Integer.parseInt(capacidad); // Validar que capacidad sea numérico
            Presentacion presentacion = new Presentacion(unidad, capacidad, articuloSeleccionado.getId(), 0.0, 0.0, 0);
            controller.agregarPresentacion(presentacion);
            cargarPresentaciones(controller.getPresentacionesPorArticulo(articuloSeleccionado));
            capacidadField.setText("");
            unidadBox.setSelectedIndex(0); // Reiniciar la selección del JComboBox
            JOptionPane.showMessageDialog(presentacionesPanel, "Presentación agregada con éxito");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(presentacionesPanel, "La capacidad debe ser un entero numérico.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(presentacionesPanel, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void eliminarPresentacion() {
        int selectedRow = presentacionesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(presentacionesPanel, "Seleccione una fila para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Presentacion presentacionSeleccionada = presentacionTableModel.getItemAt(selectedRow);
            controller.eliminarPresentacion(presentacionSeleccionada);
            cargarPresentaciones(controller.getPresentacionesPorArticulo(articuloSeleccionado));
            JOptionPane.showMessageDialog(presentacionesPanel, "Eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(presentacionesPanel, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cargarPresentaciones(List<Presentacion> presentaciones) {
        presentacionTableModel.setItems(presentaciones);
    }

    public void setSubcategoriaSeleccionada(Articulo entidad) {
        this.articuloSeleccionado = entidad;
        cargarPresentaciones(controller.getPresentacionesPorArticulo(articuloSeleccionado));
    }

    public void onElementoSeleccionado(Presentacion presentacion) {
        //TODO: Posible futura implementacion.
    }

    private void agregarAlCarrito() {
        int selectedRow = presentacionesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(presentacionesPanel, "Seleccione una presentación para agregar al pedido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Se obtiene la presentación seleccionada
        Presentacion presentacionSeleccionada = presentacionTableModel.getItemAt(selectedRow);

        // Solicitar la cantidad mediante un diálogo
        String input = JOptionPane.showInputDialog(presentacionesPanel, "Ingrese la cantidad a pedir (máximo " + presentacionSeleccionada.getCantidad() + "):");
        if (input == null) { // El usuario canceló el diálogo
            return;
        }

        try {
            int cantidadSolicitada = Integer.parseInt(input);
            if (cantidadSolicitada <= 0) {
                JOptionPane.showMessageDialog(presentacionesPanel, "La cantidad debe ser mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (cantidadSolicitada > presentacionSeleccionada.getCantidad()) {
                JOptionPane.showMessageDialog(presentacionesPanel, "No hay existencias suficientes para esa cantidad.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar si el ítem ya existe en el carrito
            boolean existe = false;
            for (CarritoItem item : carritoTableModel.getItems()) {
                if (item.getPresentacion().equals(presentacionSeleccionada)) {
                    int nuevaCantidad = item.getCantidad() + cantidadSolicitada;
                    if (nuevaCantidad > presentacionSeleccionada.getCantidad()) {
                        JOptionPane.showMessageDialog(presentacionesPanel, "La cantidad total en el carrito supera las existencias disponibles.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    item.setCantidad(nuevaCantidad);
                    existe = true;
                    break;
                }
            }
            if (!existe) {
                carritoTableModel.getItems().add(new CarritoItem(presentacionSeleccionada, cantidadSolicitada));
            }
            carritoTableModel.fireTableDataChanged();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(presentacionesPanel, "La cantidad debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarDelCarrito() {
        int selectedRow = carritoTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(presentacionesPanel, "Seleccione un ítem del pedido para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        carritoTableModel.getItems().remove(selectedRow);
        carritoTableModel.fireTableDataChanged();
    }

    private void confirmarPedido() {
        if (carritoTableModel.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(presentacionesPanel, "El carrito está vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar que cada ítem respete las existencias
        for (CarritoItem item : carritoTableModel.getItems()) {
            if (item.getCantidad() > item.getPresentacion().getCantidad()) {
                JOptionPane.showMessageDialog(presentacionesPanel, "La cantidad solicitada para "
                        + item.getPresentacion().toString() + " supera las existencias disponibles.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try {
            Factura factura = controller.calcularFactura(carritoTableModel.getItems());

            // Confirmar el pedido y actualizar existencias a través del controlador
            controller.confirmarPedido(carritoTableModel.getItems());

            // Vaciar el carrito y refrescar ambas tablas
            carritoTableModel.getItems().clear();
            carritoTableModel.fireTableDataChanged();
            cargarPresentaciones(controller.getPresentacionesPorArticulo(articuloSeleccionado));

            // Mostrar la factura en un diálogo
            mostrarFactura(factura);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(presentacionesPanel, "Error al confirmar el pedido: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void mostrarFactura(Factura factura) {
        String message = "Factura del Pedido:\n\n" +
                "Subtotal: $" + String.format("%.2f", factura.getSubtotal()) + "\n" +
                "Descuento por unidades (>10 unidades): $" + String.format("%.2f", factura.getDescuentoUnidades()) + "\n" +
                "Descuento por artículos diferentes (>10 diferentes): $" + String.format("%.2f", factura.getDescuentoDiferentes()) + "\n" +
                "Descuento por monto (> $5000): $" + String.format("%.2f", factura.getDescuentoMonto()) + "\n" +
                "----------------------------------\n" +
                "Total de descuento: $" + String.format("%.2f", factura.getTotalDescuento()) + "\n" +
                "Total a pagar: $" + String.format("%.2f", factura.getTotalFinal());

        JOptionPane.showMessageDialog(mainPanel, message, "Factura del Pedido", JOptionPane.INFORMATION_MESSAGE);
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

    static class CarritoTableModel extends AbstractTableModel<CarritoItem> {

        public CarritoTableModel(List<CarritoItem> items) {
            super(new String[]{"Presentación", "Cantidad Solicitada", "Precio Unitario", "Total"}, items);
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            CarritoItem item = items.get(rowIndex);
            Presentacion p = item.getPresentacion();
            return switch (columnIndex) {
                case 0 -> p.toString();
                case 1 -> item.getCantidad();
                case 2 -> p.getPrecioVenta() + "$";
                case 3 -> p.getPrecioVenta() * item.getCantidad() + "$";
                default -> null;
            };
        }

        public List<CarritoItem> getItems() {
            return items;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {}

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    }
}


