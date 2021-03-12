package org.example.ali;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Date;

@JsonPropertyOrder({
        "productId",
        "sellerId"
})

public abstract class CsvOutputFormat {
    @JsonProperty("productId")
    private long productId;

    @JsonProperty("sellerId")
    private long sellerId;

    @JsonProperty("oriMinPrice")
    private double oriMinPrice;

    @JsonProperty("oriMaxPrice")
    private double oriMaxPrice;

    @JsonProperty("promotionId")
    private long promotionId;

    @JsonProperty("startTime")
    private Date startTime;

    @JsonProperty("endTime")
    private Date endTime;

    @JsonProperty("phase")
    private int phase;

    @JsonProperty("productTitle")
    private String productTitle;

    @JsonProperty("minPrice")
    private double minPrice;

    @JsonProperty("maxPrice")
    private double maxPrice;

    @JsonProperty("discount")
    private double discount;

    @JsonProperty("totalStock")
    private int totalStock;

    @JsonProperty("stock")
    private int stock;

    @JsonProperty("orders")
    private int orders;

    @JsonProperty("soldout")
    private boolean soldout;

    @JsonProperty("productImage")
    private String productImage;

    @JsonProperty("productDetailUrl")
    private String productDetailUrl;

    @JsonProperty("trace")
    private String trace;

    @JsonProperty("totalTranpro3")
    private int totalTranpro3;

    @JsonProperty("productPositiveRate")
    private double productPositiveRate;

    @JsonProperty("productAverageStar")
    private double productAverageStar;

    @JsonProperty("itemEvalTotalNum")
    private int itemEvalTotalNum;

    @JsonProperty("icon")
    private String icon;

    @JsonProperty("gmtCreate")
    private long gmtCreate;

    @JsonProperty("iconHeight")
    private int iconHeight;

    @JsonProperty("iconWidth")
    private int iconWidth;

//    Не мапится в csv, так как внутренний объект
//    @JsonIgnore
//    @JsonProperty("icons")
//    private Object icons;
}
