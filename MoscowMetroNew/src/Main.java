public class Main {
    private static final String HTML_FILE_PATH = "data/moscow_metro.html";
    private static final String JSON_FILE_PATH = "data/map_moscow.json";

    public static void main(String[] args) {
        try {
            MetroParser.parseHtmlToJson(HTML_FILE_PATH, JSON_FILE_PATH);
            System.out.println(MetroParser.printStationsByLinesFromJson(JSON_FILE_PATH));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
