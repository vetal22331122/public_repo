package ru.vetal22331122.ordersparserspring.parsing;

import org.apache.commons.io.FilenameUtils;
import ru.vetal22331122.ordersparserspring.exceptions.UnsupportedFileTypeException;

public class OrdersParserFactory {

    public static OrdersParser getParser(String path) throws UnsupportedFileTypeException {
        OrdersParser parser;
        String extension = FilenameUtils.getExtension(path);

        switch (extension) {
            case ("csv"):
                parser = new OrdersCsvParser(path);
                break;
            case ("json"):
                parser = new OrdersJsonParser(path);
                break;
            default:
                throw new UnsupportedFileTypeException("Формат файла \"" + extension + "\" не поддерживается");
        }
        return parser;
    }
}