package cr.ac.una.frontend.presentation.view.views.info;
import javax.swing.*;
import java.awt.*;

public class AcercaDeView {
    private final JPanel mainPanel;

    public AcercaDeView() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Grupo G", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JTextArea integrantesText = new JTextArea("Integrantes:\n- Jefferson Garita\n- Billy Cordero\n- Emiliano Medina");
        integrantesText.setFont(new Font("Arial", Font.PLAIN, 16));
        integrantesText.setEditable(false);
        integrantesText.setBackground(mainPanel.getBackground());

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(integrantesText, BorderLayout.CENTER);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
