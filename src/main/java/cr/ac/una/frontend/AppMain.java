package cr.ac.una.frontend;

import cr.ac.una.backend.logic.Service;
import cr.ac.una.frontend.presentation.controller.Controller;
import cr.ac.una.frontend.presentation.model.Model;
import cr.ac.una.frontend.presentation.view.views.articulos.ArticuloView;
import cr.ac.una.frontend.presentation.view.views.categorias.CategoriaView;
import cr.ac.una.frontend.presentation.view.views.login.LoginView;
import cr.ac.una.frontend.presentation.view.views.subcategorias.SubcategoriaView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AppMain {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            try {
                Service.instance().cargar();
            } catch (Exception ignored) {

            }
            JFrame window = new JFrame("Prueba");
            window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            window.setPreferredSize(new Dimension(1200, 600));
            window.setResizable(false);

            // Crear contenedor con CardLayout para alternar entre login y main
            JPanel container = new JPanel(new CardLayout());
            window.setContentPane(container);

            // Crear la vista principal: un JTabbedPane con las vistas de Categorías, Subcategorías y Artículos
            JTabbedPane tabbedPane = new JTabbedPane();
            CategoriaView categoriaView = new CategoriaView();
            SubcategoriaView subcategoriaView = new SubcategoriaView();
            ArticuloView articuloView = new ArticuloView();
            Model model = new Model();

            // Crear el Controller (usando el JTabbedPane)
            Controller controller = new Controller(model, categoriaView, subcategoriaView, articuloView, tabbedPane);

            // Configurar el JTabbedPane
            tabbedPane.addTab("Categorias", categoriaView.getMainPanel());
            tabbedPane.addTab("Subcategorias", subcategoriaView.getMainPanel());
            tabbedPane.addTab("Articulos", articuloView.getMainPanel());

            // Agregar la vista principal (tarjeta "main")
            container.add(tabbedPane, "main");

            // Crear la vista de login e inyectar las dependencias (Controller y contenedor)
            LoginView loginView = new LoginView();
            loginView.setController(controller);
            loginView.setContainer(container);
            container.add(loginView.getLoginPanel(), "login");

            // Mostrar inicialmente la vista de login
            CardLayout cl = (CardLayout) container.getLayout();
            cl.show(container, "login");

            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);

            // Al cerrar la ventana, guardar datos y realizar logout (si hay un usuario autenticado)
            window.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    try {
                        Service.instance().guardar();
                        if (model.getUsuario() != null) {
                            controller.logout();
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });
        });
    }
}
