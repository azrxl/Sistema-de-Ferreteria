package cr.ac.una.presentation.views.components.tables;

import cr.ac.una.proxy.Presentacion;

import java.util.List;

public class PresentacionTableModel extends AbstractTableModel<Presentacion> {
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