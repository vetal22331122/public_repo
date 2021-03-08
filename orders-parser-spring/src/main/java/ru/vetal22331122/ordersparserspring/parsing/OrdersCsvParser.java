package ru.vetal22331122.ordersparserspring.parsing;


import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import ru.vetal22331122.ordersparserspring.model.Order;

import java.io.FileReader;
import java.io.IOException;

public class OrdersCsvParser extends OrdersParser {

    private final String filePath;

    protected OrdersCsvParser(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void run() {
        Order currentOrder = null;
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] lineInArray;
            int lineNumber = 0;
            while ((lineInArray = reader.readNext()) != null) {
                currentOrder = handleOrderFromCsv(lineInArray);
                currentOrder.setFoundOnLine(++lineNumber);
                printOrder(currentOrder);
            }
        } catch (IOException | CsvValidationException e) {
            System.err.println("Файл по указанному пути \"" + filePath + "\" отсутствует.");
        }
    }

    private Order handleOrderFromCsv(String[] orderString) {
        Order handledOrder = new Order();
        handledOrder.setCurrency(orderString[2]);
        handledOrder.setComment(orderString[3]);
        handledOrder.setFromFile(filePath);
        handledOrder.setResult("OK");
        try {
            handledOrder.setAmount(Double.parseDouble(orderString[1]));
        } catch (NumberFormatException e) {
            handledOrder.setAmount(0);
            handledOrder.setResult("WrongAmountFormat, Amount set to 0");
        }
        return handledOrder;
    }


}