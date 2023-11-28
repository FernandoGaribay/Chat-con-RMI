package componentes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.swing.JPanel;

public class reciveMessage extends JPanel {
    private String text;

    public reciveMessage(String text) {
        this.text = text;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Configuración del ancho máximo permitido
        int maxWidth = 450;

        FontMetrics fontMetrics = g.getFontMetrics();
        int lineHeight = fontMetrics.getHeight();

        String[] lines = splitTextByWidth(text, fontMetrics, maxWidth);

        int textWidth = 0;
        int textHeight = lines.length * lineHeight;

        for (String line : lines) {
            int lineWidth = fontMetrics.stringWidth(line);
            if (lineWidth > textWidth) {
                textWidth = lineWidth;
            }
        }

        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(10, 0, textWidth + 20, textHeight + 10); // MARGEN
        g.setColor(Color.BLACK);

        for (int i = 0; i < lines.length; i++) {
            int y = (i + 1) * lineHeight;
            g.drawString(lines[i], 10 + 10, y); // MARGEN
        }
    }

    private String[] splitTextByWidth(String text, FontMetrics fontMetrics, int maxWidth) {
        String[] words = text.split("\\s+");
        StringBuilder currentLine = new StringBuilder();
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (fontMetrics.stringWidth(currentLine.toString() + word) <= maxWidth) {
                currentLine.append(word).append(" ");
            } else {
                result.append(currentLine.toString().trim()).append("\n");
                currentLine = new StringBuilder(word + " ");
            }
        }

        result.append(currentLine.toString().trim());
        return result.toString().split("\n");
    }

    @Override
    public Dimension getPreferredSize() {
        int textHeight = calculateTextHeight(text, getFontMetrics(getFont()), 450);
        return new Dimension(648, textHeight + 20);
    }

    private int calculateTextHeight(String text, FontMetrics fontMetrics, int maxWidth) {
        String[] lines = splitTextByWidth(text, fontMetrics, maxWidth);
        return lines.length * fontMetrics.getHeight();
    }

//    public static void main(String[] args) {
//        javax.swing.SwingUtilities.invokeLater(() -> {
//            javax.swing.JFrame frame = new javax.swing.JFrame("Custom Panel Example");
//            frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
//
//            reciveMessage customPanel = new reciveMessage("Este es un ejemplo de un texto largo que deberá ser dividido en varias líneas para ajustarse al ancho máximo de 450 píxeles.");
//
//            frame.getContentPane().add(customPanel);
//
//            frame.pack();
//            frame.setVisible(true);
//        });
//    }
}
