import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class ThumbIcon implements Icon {
    private static final int WIDTH = 40;
    private static final int HEIGHT = 40;
    private ImageIcon imageIcon;

    public ThumbIcon(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        imageIcon.paintIcon(c, g2d, 0, 0);
        g2d.dispose();

        g.drawImage(image, x, y, WIDTH, HEIGHT, null);
    }

    @Override
    public int getIconWidth() {
        return WIDTH;
    }

    @Override
    public int getIconHeight() {
        return HEIGHT;
    }
}