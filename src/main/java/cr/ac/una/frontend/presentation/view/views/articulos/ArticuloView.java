package cr.ac.una.frontend.presentation.view.views.articulos;

import cr.ac.una.frontend.presentation.controller.Controller;
import cr.ac.una.frontend.presentation.view.components.AbstractEntityView;
import cr.ac.una.frontend.presentation.view.components.AbstractTableModel;
import cr.ac.una.frontend.presentation.view.views.presentaciones.PresentacionView;
import cr.ac.una.proxy.objects.Articulo;
import cr.ac.una.proxy.objects.Categoria;
import cr.ac.una.proxy.objects.Subcategoria;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ArticuloView extends AbstractEntityView<Articulo> {
    private Subcategoria subcategoriaSeleccionada;
    private final JTextField categoriaField;
    private final JTextField subcategoriaField;
    private final JTextField marcaField;

    private final PresentacionView presentacionView;

    public ArticuloView() {
        super(new ArticuloTableModel(new ArrayList<>(),null));

        // Inicializar PresentacionView
        presentacionView = new PresentacionView();
        presentacionView.setController(controller);

        // Panel para la subcategoría
        JPanel subcategoriaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel subcategoriaLabel = new JLabel("Subcategoría ");
        subcategoriaField = new JTextField(67);
        subcategoriaField.setEditable(false);
        subcategoriaField.setPreferredSize(new Dimension(400, 25));
        subcategoriaPanel.add(subcategoriaLabel);
        subcategoriaPanel.add(subcategoriaField);

        // Panel para la categoría
        JPanel categoriaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel categoriaLabel = new JLabel("Categoría       ");
        categoriaField = new JTextField(67);
        categoriaField.setEditable(false);
        categoriaField.setPreferredSize(new Dimension(400, 25));
        categoriaPanel.add(categoriaLabel);
        categoriaPanel.add(categoriaField);

        JLabel marcaLabel = new JLabel("Marca         ");
        marcaField = new JTextField(61);
        marcaField.setEditable(true);
        marcaField.setPreferredSize(new Dimension(200, 28));

        // Agregar los paneles al layout principal
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(categoriaPanel);   // Categoría
        topPanel.add(subcategoriaPanel); // Subcategoría

        // Panel derecho para las presentaciones
        JPanel presentacionPanel = presentacionView.getMainPanel();

        // Configurar el layout principal con JSplitPane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JPanel(new BorderLayout()), presentacionPanel);
        splitPane.setLeftComponent(new JScrollPane(secondPanel));
        splitPane.setRightComponent(presentacionPanel);
        splitPane.setDividerLocation(750); // Divisor inicial
        splitPane.setResizeWeight(0.6); // Proporción para la tabla principal

        marcaPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        marcaPanel.add(marcaLabel);
        marcaPanel.add(marcaField);

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH); // Parte superior con subcategoría y categoría
        mainPanel.add(splitPane, BorderLayout.CENTER); // Parte central con Artículos y Presentaciones
    }

    @Override
    public void setController(Controller controller) {
        super.setController(controller);
        ((ArticuloView.ArticuloTableModel) table.getModel()).controller = controller; // Conecta el controlador al modelo

    }

    public void setSubcategoriaSeleccionada(Subcategoria subcategoria) {
        this.subcategoriaSeleccionada = subcategoria;

        if (subcategoria == null) {
            subcategoriaField.setText(""); // Limpiar el campo de subcategoría
            categoriaField.setText(""); // Limpiar el campo de categoría
            // Deshabilitar la pestaña si es necesario
            controller.disableArticulosTab();
        } else {
            subcategoriaField.setText(subcategoria.toString()); // Mostrar el nombre de la subcategoría
            Categoria categoria = controller.getCategoriaById(subcategoria.getCategoriaID());
            categoriaField.setText(categoria != null ? categoria.toString() : ""); // Mostrar la categoría
            cargarEntidades(controller.getArticulosPorSubcategoria(subcategoria));
            // Habilitar la pestaña
            controller.enableArticulosTab();
        }
    }

    @Override
    protected void onElementoSeleccionado(Articulo entidad) {
        controller.initPresentacionView(entidad);
    }

    @Override
    protected Articulo obtenerEntidadDesdeFormulario() throws Exception {
        String id = codigoField.getText();
        String marca = marcaField.getText();
        String nombre = nombreField.getText();
        String descripcion = descripcionField.getText();

        if (id.isEmpty() || nombre.isEmpty() || descripcion.isEmpty() || marca.isEmpty()) {
            throw new Exception("Todos los campos son requeridos.");
        }

        if (subcategoriaSeleccionada == null) {
            throw new Exception("Debe seleccionar una subcategoría.");
        }

        return new Articulo(id, nombre, marca, descripcion, subcategoriaSeleccionada.getId());
    }

    @Override
    protected void agregarEntidad(Articulo articulo) throws Exception {
        controller.agregarArticulo(articulo);
    }

    @Override
    protected List<Articulo> buscarEntidades(String id, String nombre) throws Exception {
        return controller.buscarArticulos(subcategoriaSeleccionada, id, nombre);
    }

    @Override
    protected void eliminarEntidad(Articulo articulo) throws Exception {
        controller.eliminarArticulo(articulo);
    }

    @Override
    protected List<Articulo> obtenerEntidades() throws Exception {
        return controller.getArticulosPorSubcategoria(subcategoriaSeleccionada);
    }

    @Override
    protected void limpiarFormulario() {
        codigoField.setText("");
        nombreField.setText("");
        descripcionField.setText("");
        marcaField.setText("");
    }

    public PresentacionView getPresentacionView() {
        return presentacionView;
    }

    static class ArticuloTableModel extends AbstractTableModel<Articulo> {
        private Controller controller;

        public ArticuloTableModel(List<Articulo> articulos, Controller controller) {
            super(new String[]{"ID", "Nombre", "Marca", "Descripción"}, articulos);
            this.controller = controller;
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
            String nuevoValor = aValue.toString();
            String nombreOriginal = articulo.getNombre();

            switch (columnIndex) {
                case 1 -> { // Editar Nombre
                    try {
                        articulo.setNombre(nuevoValor); // Actualizar temporalmente
                        controller.actualizarArticulo(articulo); // Intentar actualizar en el servicio
                    } catch (Exception e) {
                        articulo.setNombre(nombreOriginal); // Restaurar el nombre original
                        fireTableDataChanged(); // Restaurar el valor en la tabla
                        JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
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
}

