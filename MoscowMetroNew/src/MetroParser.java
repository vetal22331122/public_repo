import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class MetroParser {
    private static HashSet<Line> mLines = new HashSet<>();
    private static ArrayList<Station> mStations = new ArrayList<>();
    private static HashSet<Connection> mConnections = new HashSet<>();
    private static JSONObject rootObject = new JSONObject();

    public static void parseHtmlToJson(String htmlFilePath, String jsonFilePath) {
        Document doc = parseDocFromFilePath(htmlFilePath);
        Elements rows = parseStationTableRowsFromDoc(doc);
        for (Element row : rows) {
            parseLine(row);
        }
        parseStations(rows);
        for (Element row : rows) {
            parseConnection(row);
        }
        writeDataToJsonFile(jsonFilePath);
    }

    public static String printStationsByLinesFromJson(String jsonFilePath) throws IOException, ParseException {
        StringBuilder builder = new StringBuilder();
        FileReader fileReader = new FileReader(jsonFilePath);
        JSONParser parser = new JSONParser();
        JSONObject fileRootObject = (JSONObject) parser.parse(fileReader);
        fileReader.close();
        JSONObject stationsByLines = (JSONObject) fileRootObject.get("stations");
        Set<String> keys = stationsByLines.keySet();
        TreeMap<String, Integer> stationsMap = new TreeMap<>();
        for (String key : keys) {
            stationsMap.put(key, ((JSONArray) stationsByLines.get(key)).size());
        }
        for (Map.Entry<String, Integer> entry : stationsMap.entrySet()) {
            builder.append("Линия " + entry.getKey() + ": количество станций - " + entry.getValue() + ";\n");
        }
        return builder.toString();
    }

    private static Elements parseStationTableRowsFromDoc(Document doc) {
        Elements rows = getRowsFromTable(doc, 3);
        rows.addAll(getRowsFromTable(doc, 4));
        rows.addAll(getRowsFromTable(doc, 5));
        return rows;
    }

    private static Elements getRowsFromTable(Document doc, int tableNumber) {
        Element table = doc.select("table").get(tableNumber);
        Elements rows = table.select("tr");
        rows.remove(0);
        if (tableNumber == 4 || tableNumber == 5) {
            rows.remove(0);
        }
        return rows;
    }

    private static void parseConnection(Element row) {
        Elements columns = row.select("td");
        Elements fragmentsLine = columns.get(0).children();
        Elements fragmentsConnection = columns.get(3).children();
        if (fragmentsLine.size() == 6 || fragmentsConnection.size() > 0) {
            TreeSet<Station> currentConnectionStations = new TreeSet<>(new StationLineComparator().thenComparing(new StationNameComparator()));
            Elements fragmentsName = columns.get(1).children();
            currentConnectionStations.add(getStationByLineAndName(getLineByNumber(fragmentsLine.get(0).text()), fragmentsName.get(0).text()));
            if (fragmentsLine.size() == 6) {
                currentConnectionStations.add(getStationByLineAndName(getLineByNumber(fragmentsLine.get(3).text()), fragmentsName.get(0).text()));
            }
            if (fragmentsConnection.size() > 0) {
                ArrayList<Station> parsedConnectedStations = parseConnectedStationsFromField(fragmentsConnection);
                currentConnectionStations.addAll(parsedConnectedStations);
            }
            mConnections.add(new Connection(currentConnectionStations));
        }
    }

    private static ArrayList<Station> parseConnectedStationsFromField(Elements fragmentsConnection) {
        ArrayList<Station> parsedConnectedStations = new ArrayList<>();
        for (int i = 0; i < fragmentsConnection.size() / 2; i++) {
            String[] wordsArray = fragmentsConnection.get(i * 2 + 1).attr("title")
                    .replaceAll("Кросс-платформенная ", "")
                    .replaceAll("\\s\\(.+\\)", "")
                    .split("\\s");
            List<String> wordsList = Arrays.asList(wordsArray)
                    .subList(3, wordsArray.length - getNumberOfWordsInLineName(getLineByNumber(fragmentsConnection.get(i * 2).text())));
            String name = concatWords(wordsList);
            parsedConnectedStations.add(getStationByLineAndName(getLineByNumber(fragmentsConnection.get(i * 2).text()), name));
        }
        return parsedConnectedStations;
    }

    private static void parseStations(Elements rows) {
        for (Element row : rows) {
            Elements columns = row.select("td");
            Elements fragmentsLine = columns.get(0).children();
            Elements fragmentsName = columns.get(1).children();
            boolean isClosed;
            if (fragmentsName.size() > 1 && fragmentsName.get(2).text().startsWith("(закрыта")) {
                isClosed = true;
            } else {
                isClosed = false;
            }
            mStations.add(new Station(fragmentsName.get(0).text(), getLineByNumber(fragmentsLine.get(0).text()), isClosed));
        }
        for (Element row : rows) {
            Elements columns = row.select("td");
            Elements fragmentsLine = columns.get(0).children();
            Elements fragmentsName = columns.get(1).children();
            boolean isClosed;
            if (fragmentsName.size() > 1 && fragmentsName.get(2).text().startsWith("(закрыта")) {
                isClosed = true;
            } else {
                isClosed = false;
            }
            if (fragmentsLine.size() == 6) {
                mStations.add(new Station(fragmentsName.get(0).text(), getLineByNumber(fragmentsLine.get(3).text()), isClosed));
            }
        }
    }

    private static void parseLine(Element row) {
        Elements columns = row.select("td");
        Elements fragmentsLine = columns.get(0).children();
        mLines.add(new Line(fragmentsLine.get(1).attr("title"), fragmentsLine.get(0).text()));
        if (fragmentsLine.size() == 6) {
            mLines.add(new Line(fragmentsLine.get(4).attr("title"), fragmentsLine.get(3).text()));
        }
    }

    private static Document parseDocFromFilePath(String path) {
        StringBuilder builder = new StringBuilder();
        try {
            List<String> lines = Files.readAllLines(Paths.get(path));
            lines.forEach(line -> builder.append(line + "\n"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Document doc = Jsoup.parse(builder.toString());
        return doc;
    }

    private static void writeDataToJsonFile(String jsonFilePath) {
        addDataToJsonRoot();
        writeJsonToFile(jsonFilePath);
    }

    private static void addDataToJsonRoot() {
        addStationsToJson();
        addConnectionsToJson();
        addLinesToJson();
    }

    private static void addConnectionsToJson() {
        JSONArray connectionsArray = new JSONArray();
        for (Connection connection : mConnections) {
            JSONArray currentArray = new JSONArray();
            for (Station station : connection.getConnectedStations()) {
                JSONObject currentObject = new JSONObject();
                currentObject.put("line", station.getLine().getNumber());
                currentObject.put("station", station.getName());
                currentArray.add(currentObject);
            }
            connectionsArray.add(currentArray);
        }
        rootObject.put("connections", connectionsArray);
    }

    private static void addStationsToJson() {
        JSONObject stationsJSON = new JSONObject();
        TreeMap<String, ArrayList<String>> stationsByLines = new TreeMap<>();
        for (Station station : mStations) {
            if (!station.isClosed()) {
                if (!stationsByLines.containsKey(station.getLine().getNumber())) {
                    stationsByLines.put(station.getLine().getNumber(), new ArrayList<>());
                }
                stationsByLines.get(station.getLine().getNumber()).add(station.getName());
            }
        }
        stationsJSON.putAll(stationsByLines);
        rootObject.put("stations", stationsJSON);

    }

    private static void writeJsonToFile(String jsonFilePath) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String output = gson.toJson(rootObject);
        try (FileWriter writer = new FileWriter(jsonFilePath)) {
            writer.write(output);
            writer.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("Готовый JSON-файл создан по адресу " + jsonFilePath);
    }

    private static Line getLineByNumber(String number) {
        for (Line line : mLines) {
            if (number.equals(line.getNumber())) {
                return line;
            }
        }
        return null;
    }

    private static Station getStationByLineAndName(Line line, String name) {
        for (Station station : mStations) {
            if (station.getLine().equals(line) && station.getName().equals(name)) {
                return station;
            }
        }
        return null;
    }

    private static int getNumberOfWordsInLineName(Line line) {
        return line.getName().split("\\s").length;
    }

    private static void addLinesToJson() {
        JSONArray linesArray = new JSONArray();
        for (Line line : mLines) {
            JSONObject currentLine = new JSONObject();
            currentLine.put("number", line.getNumber());
            currentLine.put("name", line.getName().replaceAll(" линия", ""));
            linesArray.add(currentLine);
        }
        rootObject.put("lines", linesArray);
    }

    private static String concatWords(List<String> words) {
        StringBuilder builder = new StringBuilder();
        for (String string : words) {
            builder.append(string + " ");
        }
        return builder.toString().trim();
    }
}
