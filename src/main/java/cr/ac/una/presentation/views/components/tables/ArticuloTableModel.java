package cr.ac.una.presentation.views.components.tables;

import cr.ac.una.proxy.Articulo;
import cr.ac.una.proxy.Subcategoria;

import java.util.List;

public class ArticuloTableModel extends AbstractTableModel<Articulo> {
    public ArticuloTableModel(List<Articulo> articulos) {
        super(new String[]{"ID", "Nombre", "Marca", "Descripción"}, articulos);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Articulo articulo = getItemAt(rowIndex);
        return switch (columnIndex) {
            case 0 -> articulo.getId(); // Columna ID
            case 1 -> articulo.getNombre(); // Columna Nombre
            case 2 -> articulo.getMarca(); // Columna Descripción
            case 3 -> articulo.getDescripcion();
            default -> null;
        };
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Articulo articulo = getItemAt(rowIndex);
        switch (columnIndex) {
            case 1 -> articulo.setNombre((String) aValue); // Actualizar Nombre
            case 2 -> articulo.setDescripcion((String) aValue); // Actualizar Descripción
            case 3 -> articulo.setMarca((String) aValue);
        }
        fireTableCellUpdated(rowIndex, columnIndex); // Notificar cambios
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // Solo los campos de Capacidad, Precio Compra, Precio Venta y Cantidad son editables
        return columnIndex == 1 || columnIndex == 2 || columnIndex == 3;
    }
}
