package cr.ac.una.presentation.views.concrete_views;

import cr.ac.una.presentation.views.components.abstract_view.AbstractEntityView;
import cr.ac.una.presentation.views.components.tables.SubcategoriaTableModel;
import cr.ac.una.proxy.Categoria;
import cr.ac.una.proxy.Subcategoria;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SubcategoriaView extends AbstractEntityView<Subcategoria> {
    private Categoria categoriaSeleccionada;
    private final JTextField categoriaTextField;

    public SubcategoriaView() {
        super(new SubcategoriaTableModel(new ArrayList<>()));

        JPanel categoriaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Layout horizontal
        JLabel categoriaLabel = new JLabel("Categoria     ");
        categoriaTextField = new JTextField(67); // Tamaño del campo de texto
        categoriaTextField.setEditable(false); // No editable

        // Agregar los componentes al panel de categoría
        categoriaPanel.add(categoriaLabel);
        categoriaPanel.add(categoriaTextField);

        // Agregar el panel de categoría encima del agregarPanel
        mainPanel.setLayout(new BorderLayout()); // Usar BorderLayout en el mainPanel
        mainPanel.add(categoriaPanel, BorderLayout.NORTH); // Ubicar el panel de categoría en la parte superior
        mainPanel.add(secondPanel, BorderLayout.CENTER);
    }

    public void setCategoriaSeleccionada(Categoria categoria) {
        this.categoriaSeleccionada = categoria;

        if (categoria == null) {
            categoriaTextField.setText(""); // Limpiar el campo de texto
            // Deshabilitar la pestaña (suponiendo que el controlador o el TabbedPaneManager tiene acceso a esto)
            controller.disableSubcategoriasTab();
        } else {
            categoriaTextField.setText(categoria.toString()); // Mostrar el nombre de la categoría
            cargarEntidades(controller.getSubcategoriasPorCategoria(categoria));
            // Habilitar la pestaña
            controller.enableSubcategoriasTab();
        }
    }

    @Override
    protected void onElementoSeleccionado(Subcategoria entidad) {
        controller.initArticuloView(entidad);
    }

    @Override
    protected Subcategoria obtenerEntidadDesdeFormulario() throws Exception {
        String id = codigoField.getText();
        String nombre = nombreField.getText();
        String descripcion = descripcionField.getText();

        if (id.isEmpty() || nombre.isEmpty() || descripcion.isEmpty()) {
            throw new Exception("Todos los campos son requeridos.");
        }

        if (categoriaSeleccionada == null) {
            throw new Exception("Debe seleccionar una categoría.");
        }

        return new Subcategoria(id, nombre, descripcion, categoriaSeleccionada.getId());
    }

    @Override
    protected void agregarEntidad(Subcategoria subcategoria) throws Exception {
        controller.agregarSubcategoria(subcategoria);
    }

    @Override
    protected List<Subcategoria> buscarEntidades(String id, String nombre) throws Exception {
        return controller.buscarSubcategorias(categoriaSeleccionada, id, nombre);
    }

    @Override
    protected void eliminarEntidad(Subcategoria subcategoria) throws Exception {
        controller.eliminarSubcategoria(subcategoria);
    }

    @Override
    protected List<Subcategoria> obtenerEntidades() throws Exception {
        return controller.getSubcategoriasPorCategoria(categoriaSeleccionada);
    }

    @Override
    protected void limpiarFormulario() {
        codigoField.setText("");
        nombreField.setText("");
        descripcionField.setText("");
    }
}