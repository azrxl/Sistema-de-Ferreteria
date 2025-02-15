package cr.ac.una.frontend.presentation.view.components;

import cr.ac.una.frontend.presentation.controller.Controller;

import java.util.List;

public abstract class AbstractTableModel<T> extends javax.swing.table.AbstractTableModel {
    protected String[] columnNames;
    protected List<T> items;
    protected Controller controller;

    public AbstractTableModel(String[] columnNames, List<T> items) {
        this.columnNames = columnNames;
        this.items = items;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public int getRowCount() {
        return items.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // Permitir edición solo en las columnas Nombre y Descripción
        return columnIndex == 1 || columnIndex == 2;
    }

    @Override
    public abstract Object getValueAt(int rowIndex, int columnIndex);

    @Override
    public abstract void setValueAt(Object aValue, int rowIndex, int columnIndex);

    public T getItemAt(int rowIndex) {
        return items.get(rowIndex);
    }

    public void setItems(List<T> items) {
        this.items = items;
        fireTableDataChanged();
    }
}
