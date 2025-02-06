package cr.ac.una.frontend.presentation.view.views.login;

import cr.ac.una.frontend.presentation.controller.Controller;
import cr.ac.una.proxy.objects.User;

import javax.swing.*;
import java.awt.*;

public class LoginView {
    private JPanel loginPanel;
    private JTextField identificacionField;
    private JPasswordField contrasenaField;
    private JButton loginButton;
    private JButton finishButton;

    private JPanel container; // Contenedor con CardLayout que contiene "login" y "main"
    private Controller controller;  // Controller de la aplicación

    public LoginView() {
        initActions();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    private void initActions() {
        // Acción para el botón Login
        loginButton.addActionListener(e -> {
            String usuario = identificacionField.getText();
            char[] pwd = contrasenaField.getPassword();
            User user = new User(usuario, new String(pwd));
            try {
                // Delegar al Controller la operación de login
                User loggedUser = controller.login(user);
                JOptionPane.showMessageDialog(null,
                        "Login exitoso. Bienvenido, " + loggedUser.getIdentificacion() + "!",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                // Inicializar la aplicación principal (cargar datos, configurar vistas, etc.)
                controller.initApp();
                // Cambiar la vista al panel principal
                CardLayout cl = (CardLayout) container.getLayout();
                cl.show(container, "main");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Error en login: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Acción para el botón Terminar (cierra la aplicación)
        finishButton.addActionListener(e -> {
            System.exit(0);
        });
    }

    public JPanel getLoginPanel() {
        return loginPanel;
    }

    // Permite inyectar el contenedor para cambiar las tarjetas
    public void setContainer(JPanel container) {
        this.container = container;
    }
}