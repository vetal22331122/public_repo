import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        WebSite skillbox = new WebSite();

        StringBuilder builder = new ForkJoinPool().invoke(new SiteMapTexter("https://skillbox.ru/", 0, skillbox));
        try {
            writeBuilderToTXTFile("data/map.txt", builder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeBuilderToTXTFile(String path, StringBuilder builder) throws IOException {
        File output = new File(path);
        output.createNewFile();
        try (FileWriter writer = new FileWriter(output)) {
            writer.write(builder.toString());
            writer.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("Готовый txt-файл создан по адресу " + path);
    }
}
