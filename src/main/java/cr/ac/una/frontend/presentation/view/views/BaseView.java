package cr.ac.una.frontend.presentation.view.views;

import cr.ac.una.frontend.presentation.controller.Controller;

import javax.swing.*;

public abstract class BaseView {
    protected JLabel IDLabel;
    protected JLabel nombreLabel;
    protected JLabel descripcionLabel;
    protected JTextField codigoField;
    protected JTextField nombreField;
    protected JTextField descripcionField;
    protected JButton guardarButton;
    protected JTable table;
    protected JPanel busquedaPanel;
    protected JLabel busquedaNombreLabel;
    protected JLabel busquedaIDLabel;
    protected JTextField busquedaNombreField;
    protected JTextField busquedaIDField;
    protected JButton buscarButton;
    protected JButton eliminarButton;
    protected JPanel agregarPanel;
    protected JPanel mainPanel;
    protected JPanel listadoPanel;
    protected JScrollPane tableScrollPanel;
    protected JPanel secondPanel;
    protected JPanel marcaPanel;

    protected Controller controller;

    public BaseView() {}

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
