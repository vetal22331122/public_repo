package org.example.ali;

public class Main {
    public static final String GET_REQUEST_BEFORE_OFFSET = "https://gpsfront.aliexpress.com/getRecommendingResults.do?callback=jQuery183090984577765775_1615416835306&widget_id=5547572&platform=pc&limit=10&offset=";
    public static final String GET_REQUEST_AFTER_OFFSET = "&phase=1&productIds2Top=&postback=a8659f3c-5d89-4e10-ae67-9e7540563f4b&_=1615416936912";
    public static final int NUMBER_OF_PRODUCTS_TO_PARSE = 100;

    public static void main(String[] args) {
        ProductsParser parser = new ProductsParser(GET_REQUEST_BEFORE_OFFSET,
                GET_REQUEST_AFTER_OFFSET,
                NUMBER_OF_PRODUCTS_TO_PARSE,
                "./ali.csv");

        parser.parseProducts();
    }
}
