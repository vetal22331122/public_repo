package ru.vetal22331122.ordersparserspring.parsing;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.vetal22331122.ordersparserspring.model.Order;

import java.io.FileReader;
import java.io.IOException;

public class OrdersJsonParser extends OrdersParser {

    private String filePath;

    protected OrdersJsonParser(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void run() {
        try {
            FileReader fileReader = new FileReader(filePath);
            JSONParser parser = new JSONParser();
            Order currentOrder = null;
            JSONObject fileRootObject = (JSONObject) parser.parse(fileReader);
            JSONArray orders = (JSONArray) fileRootObject.get("orders");
            for (Object object : orders) {
                JSONObject parsedObject = (JSONObject) object;
                currentOrder = handleOrderFromJson(parsedObject);
                currentOrder.setFoundOnLine(orders.indexOf(object) + 1);
                printOrder(currentOrder);
            }
        } catch (IOException | ParseException e) {
            System.err.println("Файл по указанному пути \"" + filePath + "\" отсутствует.");
        }

    }

    private Order handleOrderFromJson(JSONObject parsedObject) {
        Order handledOrder = new Order();
        handledOrder.setCurrency((String) parsedObject.get("currency"));
        handledOrder.setComment((String) parsedObject.get("comment"));
        handledOrder.setFromFile(filePath);
        handledOrder.setResult("OK");
        try {
            handledOrder.setAmount((Double) parsedObject.get("amount"));
        } catch (ClassCastException e) {
            handledOrder.setAmount(0);
            handledOrder.setResult("WrongAmountFormat, Amount set to 0");
        }



        return handledOrder;
    }
}

