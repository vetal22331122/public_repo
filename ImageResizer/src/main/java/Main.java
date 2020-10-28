import main.java.LogMode;

import java.io.File;

public class Main
{

    private static int newWidth = 300;

    public static void main(String[] args)
    {
        String srcFolder = "data/src";
        String dstFolder = "data/dst";
        String dstFolder2 = "data/dst2";
        String dstFolder3 = "data/dst3";
        String dstFolderScaler = "data/dstScaler";

        File srcDir = new File(srcFolder);

        File[] files = srcDir.listFiles();

        int middle = files.length / 2;
        File[] files1 = new File[files.length - middle];
        System.arraycopy(files, 0, files1, 0, files1.length);
        File[] files2 = new File[files.length - files1.length];
        System.arraycopy(files, middle, files2, 0, files2.length);

        //метод ближайшего соседа
        ImageResizer resizer1 = new ImageResizer(files1, newWidth, dstFolder, System.currentTimeMillis());
        ImageResizer resizer2 = new ImageResizer(files2, newWidth, dstFolder, System.currentTimeMillis());
        new Thread(resizer1).run();
        new Thread(resizer2).run();

        //далее откомменчены мои эксперименты с разными алгоритмами ресайза
        UpgradedImageResizer resizer3 = new UpgradedImageResizer(files1, newWidth, dstFolder2, System.currentTimeMillis(), LogMode.ON);
        UpgradedImageResizer resizer4 = new UpgradedImageResizer(files2, newWidth, dstFolder2, System.currentTimeMillis(), LogMode.OFF);
        QuadroResizer resizer5 = new QuadroResizer(files1, newWidth, dstFolder3, System.currentTimeMillis(), LogMode.ON);
        QuadroResizer resizer6 = new QuadroResizer(files2, newWidth, dstFolder3, System.currentTimeMillis(), LogMode.ON);
        new Thread(resizer3).run();
        new Thread(resizer4).run();
        new Thread(resizer5).run();
        new Thread(resizer6).run();

        //======================================================================

        //использование библиотеки
        ImageScaler scaler1 = new ImageScaler(files1, newWidth, dstFolderScaler, System.currentTimeMillis());
        ImageScaler scaler2 = new ImageScaler(files2, newWidth, dstFolderScaler, System.currentTimeMillis());
        new Thread(scaler1).run();
        new Thread(scaler2).run();


    }
}
