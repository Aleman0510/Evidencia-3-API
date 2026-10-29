package com.ecommerce.compositeapidemo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    private String product_id;
    private Integer stock_quantity;
    private String warehouse_location;

    // Getters y Setters
    public String getProduct_id() { return product_id; }
    public void setProduct_id(String product_id) { this.product_id = product_id; }
    public Integer getStock_quantity() { return stock_quantity; }
    public void setStock_quantity(Integer stock_quantity) { this.stock_quantity = stock_quantity; }
    public String getWarehouse_location() { return warehouse_location; }
    public void setWarehouse_location(String warehouse_location) { this.warehouse_location = warehouse_location; }
}