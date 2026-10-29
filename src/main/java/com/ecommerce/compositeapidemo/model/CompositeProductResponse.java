package com.ecommerce.compositeapidemo.model;

public class CompositeProductResponse {
    private String productId;
    private String title;
    private Double priceUsd;
    private Integer availableUnits;
    private String stockStatus;
    private String warehouseLocation;
    private Integer estimatedDeliveryDays;

    // Constructor
    public CompositeProductResponse(String productId, String title, Double priceUsd, Integer availableUnits, String stockStatus, String warehouseLocation, Integer estimatedDeliveryDays) {
        this.productId = productId;
        this.title = title;
        this.priceUsd = priceUsd;
        this.availableUnits = availableUnits;
        this.stockStatus = stockStatus;
        this.warehouseLocation = warehouseLocation;
        this.estimatedDeliveryDays = estimatedDeliveryDays;
    }

    // Getters y Setters
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Double getPriceUsd() { return priceUsd; }
    public void setPriceUsd(Double priceUsd) { this.priceUsd = priceUsd; }
    public Integer getAvailableUnits() { return availableUnits; }
    public void setAvailableUnits(Integer availableUnits) { this.availableUnits = availableUnits; }
    public String getStockStatus() { return stockStatus; }
    public void setStockStatus(String stockStatus) { this.stockStatus = stockStatus; }
    public String getWarehouseLocation() { return warehouseLocation; }
    public void setWarehouseLocation(String warehouseLocation) { this.warehouseLocation = warehouseLocation; }
    public Integer getEstimatedDeliveryDays() { return estimatedDeliveryDays; }
    public void setEstimatedDeliveryDays(Integer estimatedDeliveryDays) { this.estimatedDeliveryDays = estimatedDeliveryDays; }
}