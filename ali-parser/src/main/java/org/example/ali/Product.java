package org.example.ali;

import lombok.Data;

@Data
public class Product {
    private long productId;
    private long sellerId;
    private double oriMinPrice;
    private double oriMaxPrice;
    private long promotionId;
    private long startTime;
    private long endTime;
    private int phase;
    private String productTitle;
    private double minPrice;
    private double maxPrice;
    private double discount;
    private int totalStock;
    private int stock;
    private int orders;
    private boolean soldout;
    private String productImage;
    private String productDetailUrl;
    private String trace;
    private int totalTranpro3;
    private double productPositiveRate;
    private double productAverageStar;
    private int itemEvalTotalNum;
    private String icon;
    private long gmtCreate;
}
