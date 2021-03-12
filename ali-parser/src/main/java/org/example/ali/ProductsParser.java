package org.example.ali;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;
import java.util.HashSet;

public class ProductsParser {
    private String getRequestBeforeOffset;
    private String getRequestAfterOffset;
    private int offset = 0;
    private int numberOfProductsToParse;
    private String csvOutputPath;
    private int alreadyParsed = 0;
    private HashSet<Long> usedIds= new HashSet<>();
    private CsvMapper csvMapper;
    private CsvSchema csvSchema;
    private JSONParser jsonParser = new JSONParser();

    private HashSet<JSONObject> objectsToWrite = new HashSet<>();

    public ProductsParser(String getRequestBeforeOffset, String getRequestAfterOffset,
                          int numberOfProductsToParse, String csvOutputPath) {
        this.getRequestBeforeOffset = getRequestBeforeOffset;
        this.getRequestAfterOffset = getRequestAfterOffset;
        this.numberOfProductsToParse = numberOfProductsToParse;
        this.csvOutputPath = csvOutputPath;
        csvMapper = new CsvMapper();
        csvSchema = csvMapper
                .schemaFor(CsvOutputFormat.class)
                .withHeader()
        ;
    }

    public void parseProducts() {
        while (alreadyParsed < numberOfProductsToParse) {
            try {
                parseGroupOfProducts();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        new File(csvOutputPath).delete();
        try {
            writeProducts();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Готовый файл создан по адресу " + new File(csvOutputPath).getAbsolutePath());
    }

    public void parseGroupOfProducts() throws IOException, ParseException {
        String requestAddress = getRequestBeforeOffset + offset + getRequestAfterOffset;
        URL request = new URL(requestAddress);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(request.openStream())
        );
        String inputLine = in.readLine();
        String result = inputLine.substring(inputLine.indexOf("{"), inputLine.length()-2);

        JSONObject object = (JSONObject) jsonParser.parse(result);
        JSONArray items = (JSONArray) object.get("results");

        for (Object o : items) {
            long currentId = (long) ((JSONObject) o).get("productId");
            if (usedIds.add(currentId) && alreadyParsed != numberOfProductsToParse) {
                objectsToWrite.add((JSONObject) o);
                alreadyParsed++;
            }
        }
        offset += 10;
        in.close();
    }

    private void writeProducts() throws IOException {
        JSONArray array = new JSONArray();
        array.addAll(objectsToWrite);
        csvMapper
                .writerFor(JSONArray.class)
                .with(JsonGenerator.Feature.IGNORE_UNKNOWN)
                .with(csvSchema)
                .writeValue(new FileWriter(csvOutputPath, true), array);

    }

}
