package ru.vetal22331122.ordersparserspring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.vetal22331122.ordersparserspring.exceptions.UnsupportedFileTypeException;
import ru.vetal22331122.ordersparserspring.parsing.OrdersParserFactory;

@SpringBootApplication
public class OrdersParserConsoleApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(OrdersParserConsoleApplication.class, args);
    }

    @Override
    public void run(String... args) {
        //String[] files = {"data/orders1.csv", "data/orders2.json"};
        for (String arg : args) {
            try {
                new Thread(OrdersParserFactory.getParser(arg)).start();
            } catch (UnsupportedFileTypeException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
