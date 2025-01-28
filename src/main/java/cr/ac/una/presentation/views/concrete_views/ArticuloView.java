package cr.ac.una.presentation.views.concrete_views;

import cr.ac.una.presentation.views.components.abstract_view.AbstractEntityView;
import cr.ac.una.presentation.views.components.tables.ArticuloTableModel;
import cr.ac.una.proxy.Articulo;
import cr.ac.una.proxy.Categoria;
import cr.ac.una.proxy.Subcategoria;

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
        super(new ArticuloTableModel(new ArrayList<>()));

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
}