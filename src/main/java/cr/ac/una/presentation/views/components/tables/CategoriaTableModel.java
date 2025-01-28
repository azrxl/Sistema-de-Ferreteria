package cr.ac.una.presentation.views.components.tables;


import cr.ac.una.proxy.Categoria;

import java.util.List;

public class CategoriaTableModel extends AbstractTableModel<Categoria> {
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

