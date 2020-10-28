import main.java.LogMode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class QuadroResizer implements Runnable {
    private File[] files;
    private int newWidth;
    private String dstFolder;
    private long start;
    private LogMode logMode;

    public QuadroResizer(File[] files, int newWidth, String dstFolder, long start, LogMode logMode) {
        this.files = files;
        this.newWidth = newWidth;
        this.dstFolder = dstFolder;
        this.start = start;
        this.logMode = logMode;
    }


    @Override
    public void run() {
        try
        {
            for(File file : files)
            {
                BufferedImage image = ImageIO.read(file);
                if(image == null) {
                    continue;
                }

                int newHeight = (int) Math.round(
                        image.getHeight() / (image.getWidth() / (double) newWidth)
                );
                BufferedImage newImage = new BufferedImage(
                        newWidth, newHeight, BufferedImage.TYPE_INT_RGB
                );

                int widthStep = image.getWidth() / newWidth;
                int heightStep = image.getHeight() / newHeight;

                for (int x = 0; x < newWidth; x++) {
                    for (int y = 0; y < newHeight; y++) {
                        newImage.setRGB(x, y, qAverageRGB(getPixelsSegment(image, x * widthStep, y * heightStep, widthStep, heightStep)));
                    }
                }

                File newFile = new File(dstFolder + "/" + file.getName());
                ImageIO.write(newImage, "jpg", newFile);
                this.logMode = LogMode.OFF;
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private List<Integer> getPixelsSegment (BufferedImage image, int x, int y, int widthStep, int heightStep) {
        List<Integer> pixelsSegment = new ArrayList<>();
        for (int i = 0; i < widthStep; i++) {
            for (int j = 0; j < heightStep; j++) {
                pixelsSegment.add(image.getRGB(x + i, y + j));
            }
        }
        return pixelsSegment;
    }

    private int qAverageRGB(List<Integer> rgbs) {
        int averageRGB = 0;
        int averageRed = 0;
        int averageGreen = 0;
        int averageBlue = 0;
        int averageAChannel = 0;

        for (Integer rgb : rgbs) {
            int aChannel = rgb >> 24;
            int red = (rgb - (aChannel << 24)) >> 16;
            int green = (rgb - (aChannel << 24) - (red << 16)) >> 8;
            int blue = rgb - (aChannel << 24) - (red << 16) - (green << 8);
            averageAChannel += aChannel * aChannel;
            averageRed += red *red;
            averageGreen += green * green;
            averageBlue += blue * blue;
        }

        averageAChannel = (int) Math.sqrt(averageAChannel / rgbs.size());
        averageRed = (int) Math.sqrt(averageRed / rgbs.size());
        averageGreen = (int) Math.sqrt(averageGreen / rgbs.size());
        averageBlue = (int) Math.sqrt(averageBlue / rgbs.size());
        averageRGB = (averageAChannel << 24) + (averageRed << 16) + (averageGreen << 8) + averageBlue;
        return averageRGB;
    }
}
