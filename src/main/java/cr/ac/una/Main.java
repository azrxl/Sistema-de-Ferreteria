package cr.ac.una;

import cr.ac.una.logic.Service;
import cr.ac.una.presentation.controller.Controller;
import cr.ac.una.presentation.model.Model;
import cr.ac.una.presentation.view.views.articulos.ArticuloView;
import cr.ac.una.presentation.view.views.categorias.CategoriaView;
import cr.ac.una.presentation.view.views.presentaciones.PresentacionView;
import cr.ac.una.presentation.view.views.subcategorias.SubcategoriaView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            JFrame window = new JFrame();
            JTabbedPane tabbedPane = new JTabbedPane(); // Crear el JTabbedPane principal
            window.setContentPane(tabbedPane);

            try {
                Service.instance().cargar();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(tabbedPane, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
            }

            CategoriaView categoriaView = new CategoriaView();
            SubcategoriaView subcategoriaView = new SubcategoriaView();
            ArticuloView articuloView = new ArticuloView();
            PresentacionView presentacionView = new PresentacionView();
            Model model = new Model();

            // Pasamos el JTabbedPane al Controller
            Controller controller = new Controller(model, categoriaView, subcategoriaView, articuloView, tabbedPane);

            // Agregar las vistas al JTabbedPane
            tabbedPane.addTab("Categorias", categoriaView.getMainPanel());
            tabbedPane.addTab("Subcategorias", subcategoriaView.getMainPanel());
            tabbedPane.addTab("Articulos", articuloView.getMainPanel());


            // Configurar la ventana principal
            window.setPreferredSize(new Dimension(1200, 600));
            window.setResizable(false);
            window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            window.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    try {
                        Service.instance().guardar();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });

            window.pack();
            window.setVisible(true);
            window.setTitle("Prueba");

            // Inicializar la aplicación
            controller.initApp();
        });

    }
}
