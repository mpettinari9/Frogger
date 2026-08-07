package view;

import javax.swing.*;
import java.awt.*;

/*
 * Sprite visivo di una tana di arrivo.
 * Mostra un colore diverso a seconda dello stato: libera (verde scuro) o occupata (verde brillante).
 * Non usa un'immagine ma disegna direttamente con paintComponent, come fallback
 * nel caso non si abbia un asset dedicato.
 */
public class HomeSlotSprite extends JLabel {

    private boolean occupied;

    public HomeSlotSprite(int x, int y, int width, int height) {
        this.setBounds(x, y, width, height);
        this.occupied = false;
        this.setOpaque(false);
        this.setVisible(true);
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
        this.repaint();
    }

    public boolean isOccupied() {
        return occupied;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Tana libera: rettangolo verde scuro con bordo
        // Tana occupata: rettangolo verde brillante (con la rana dentro)
        if (occupied) {
            g.setColor(new Color(0, 220, 0));   // verde brillante = occupata
        } else {
            g.setColor(new Color(0, 80, 0));    // verde scuro = libera
        }
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

        // Bordo bianco
        g.setColor(Color.WHITE);
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
    }
}