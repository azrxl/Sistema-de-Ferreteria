package cr.ac.una.presentation.views.components.tables;

import cr.ac.una.proxy.Subcategoria;

import java.util.List;

public class SubcategoriaTableModel extends AbstractTableModel<Subcategoria> {
    public SubcategoriaTableModel(List<Subcategoria> subcategorias) {
        super(new String[]{"ID", "Nombre", "Descripción"}, subcategorias);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Subcategoria subcategoria = getItemAt(rowIndex);
        return switch (columnIndex) {
            case 0 -> subcategoria.getId(); // Columna ID
            case 1 -> subcategoria.getNombre(); // Columna Nombre
            case 2 -> subcategoria.getDescripcion(); // Columna Descripción
            default -> null;
        };
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Subcategoria subcategoria = getItemAt(rowIndex);
        switch (columnIndex) {
            case 1 -> subcategoria.setNombre((String) aValue); // Actualizar Nombre
            case 2 -> subcategoria.setDescripcion((String) aValue); // Actualizar Descripción
        }
        fireTableCellUpdated(rowIndex, columnIndex); // Notificar cambios
    }
}
