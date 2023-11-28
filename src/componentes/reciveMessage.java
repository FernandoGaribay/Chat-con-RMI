package componentes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class reciveMessage extends JPanel {
    private String text;
    private Font font;
    private boolean isSentBySameClient; 

    public reciveMessage(String text, boolean isSentBySameClient) {
        this.text = text;
        this.font = new Font("Arial", Font.PLAIN, 26);
        this.isSentBySameClient = isSentBySameClient;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Configuración del ancho maximo permitido
        int maxWidth = 450;

        FontMetrics fontMetrics = g.getFontMetrics(font);
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

        // ANTIALIASING
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Background
        Color backgroundColor = isSentBySameClient ? new Color(0, 128, 0) : new Color(32, 44, 51);
        g.setColor(backgroundColor);
        g.fillRect(10, 0, textWidth + 20, textHeight + 10); // MARGEN
        
        // Text
        g.setColor(Color.WHITE);
        g.setFont(font);

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

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            javax.swing.JFrame frame = new javax.swing.JFrame("test");
            frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);

            reciveMessage customPanel = new reciveMessage("dfs fsdfsdfsd fsdfsdf sfsdf sdfsf fsdfs dfs sfsdf sdfsdf sdfsd fsfs s fsdf ssf sdfsdfsdfsd sfs ddsfsdfs sdf sdf sdfsf sdf sfsf dfsdfds fsfs fs sdfdf.", true);

            frame.getContentPane().add(customPanel);

            frame.pack();
            frame.setVisible(true);
        });
    }
}
