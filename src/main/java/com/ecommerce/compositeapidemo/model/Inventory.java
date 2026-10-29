package com.ecommerce.compositeapidemo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    private String product_id;
    private String stock_status;
    private Integer available_units;
    private String warehouse_location;
    private Integer estimated_delivery_days;

    // Getters y Setters
    public String getProduct_id() { return product_id; }
    public void setProduct_id(String product_id) { this.product_id = product_id; }

    public String getStock_status() { return stock_status; }
    public void setStock_status(String stock_status) { this.stock_status = stock_status; }

    public Integer getAvailable_units() { return available_units; }
    public void setAvailable_units(Integer available_units) { this.available_units = available_units; }

    public String getWarehouse_location() { return warehouse_location; }
    public void setWarehouse_location(String warehouse_location) { this.warehouse_location = warehouse_location; }

    public Integer getEstimated_delivery_days() { return estimated_delivery_days; }
    public void setEstimated_delivery_days(Integer estimated_delivery_days) { this.estimated_delivery_days = estimated_delivery_days; }
}