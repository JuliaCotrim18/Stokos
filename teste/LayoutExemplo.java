// TesteGUI.java

import javax.swing.*;
import java.awt.*;

public class LayoutExemplo {
    public static void main(String[] args) {
        // Cria a janela principal
        JFrame frame = new JFrame("Layouts Combinados");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // Define o layout do JFrame como FlowLayout (coloca painéis lado a lado)
        frame.setLayout(new FlowLayout());

        // ------------------
        // PANEL 1 com BorderLayout
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        panel1.setPreferredSize(new Dimension(180, 250)); // Tamanho fixo para exibição

        panel1.add(new JButton("Norte"), BorderLayout.NORTH);
        panel1.add(new JButton("Centro"), BorderLayout.CENTER);
        panel1.add(new JButton("Sul"), BorderLayout.SOUTH);

        // ------------------
        // PANEL 2 com GridLayout (2x2)
        JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayout(2, 2));
        panel2.setPreferredSize(new Dimension(180, 250));

        panel2.add(new JLabel("Nome:"));
        panel2.add(new JTextField(10));
        panel2.add(new JLabel("Idade:"));
        panel2.add(new JTextField(3));

        // ------------------
        // Adiciona os dois painéis no frame
        frame.add(panel1);
        frame.add(panel2);

        // Exibe a janela
        frame.setVisible(true);
    }
}
