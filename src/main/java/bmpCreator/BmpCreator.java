package bmpCreator;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class BmpCreator {

    public static void saveBmp(int imageWidth, int imageHeight, double[][] buffer) {
        BufferedImage img = map(imageWidth, imageHeight, buffer);
        savePNG(img, "C:/Users/Admin/IdeaProjects/education/ComputerGraphics/test.bmp");
    }

    private static BufferedImage map(int sizeX, int sizeY, double[][] buffer) {
        final BufferedImage res = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                System.out.print(buffer[x][y]);
                if (buffer[x][y] == 10) {
                    res.setRGB(x, y, Color.RED.getRGB());
                } else {
                    res.setRGB(x, y, getColor((int) ((1- Math.abs(buffer[x][y])) * 100)));
                }
            }
            System.out.println();
        }
        return res;
    }

    private static void savePNG(final BufferedImage bi, final String path) {
        try {
            RenderedImage rendImage = bi;
            ImageIO.write(rendImage, "bmp", new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getColor(int percent){
        percent = 100 - percent;
        int rgb = 255 * percent / 100;
        Color color = new Color(rgb, rgb, rgb);
        return  color.getRGB();
    }

}