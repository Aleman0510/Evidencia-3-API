package com.ecommerce.compositeapidemo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

/** Mapea la respuesta de Frankfurter: {"amount":1.0,"base":"USD","date":"...","rates":{"MXN":17.5}} */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ExchangeRateResponse(String base, String date, Map<String, Double> rates) {
}