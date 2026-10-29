package com.ecommerce.compositeapidemo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CompositeProductResponse(
        @JsonProperty("product_id") String productId,
        @JsonProperty("title") String title,
        @JsonProperty("price_usd") Double priceUsd,
        @JsonProperty("price_mxn") Double priceMxn,
        @JsonProperty("stock_status") String stockStatus,
        @JsonProperty("available_units") Integer availableUnits,
        @JsonProperty("warehouse_location") String warehouseLocation,
        @JsonProperty("estimated_delivery_days") Integer estimatedDeliveryDays,
        @JsonProperty("currency_conversion_source") String currencyConversionSource,
        @JsonProperty("status") String status
) {
}