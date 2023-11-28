package componentes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.swing.JPanel;

public class NewJPanel extends javax.swing.JPanel {

    private String text;

    public NewJPanel(String text) {
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
        return new Dimension(500, 200);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
