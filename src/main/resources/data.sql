INSERT INTO products (product_id, title, description, category, price_usd)
VALUES ('P-101', 'Laptop Gamer XYZ', 'Laptop gamer 16GB RAM, RTX, 1TB SSD', 'Computadoras', 1200.00)
ON CONFLICT (product_id) DO NOTHING;

INSERT INTO products (product_id, title, description, category, price_usd)
VALUES ('P-102', 'Mouse Inalambrico Pro', 'Mouse ergonomico 2.4GHz', 'Accesorios', 25.50)
ON CONFLICT (product_id) DO NOTHING;

INSERT INTO inventory (product_id, stock_status, available_units, warehouse_location, estimated_delivery_days)
VALUES ('P-101', 'IN_STOCK', 15, 'Almacen Chihuahua - Pasillo B3', 3)
ON CONFLICT (product_id) DO NOTHING;

INSERT INTO inventory (product_id, stock_status, available_units, warehouse_location, estimated_delivery_days)
VALUES ('P-102', 'OUT_OF_STOCK', 0, 'Almacen Monterrey - Pasillo A1', 7)
ON CONFLICT (product_id) DO NOTHING;