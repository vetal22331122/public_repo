import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageScaler implements Runnable {

    private File[] files;
    private int newSize;
    private String dstFolder;
    private long start;

    public ImageScaler(File[] files, int newSize, String dstFolder, long start) {
        this.files = files;
        this.newSize = newSize;
        this.dstFolder = dstFolder;
        this.start = start;
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
                BufferedImage resultImage = new Scalr().resize(image, Scalr.Method.ULTRA_QUALITY, newSize);
                File newFile = new File(dstFolder + "/" + file.getName());
                ImageIO.write(resultImage, "jpg", newFile);
                image.flush();
                resultImage.flush();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("Scaler library thread finished in " + (System.currentTimeMillis() - start) + " ms");
    }
}
