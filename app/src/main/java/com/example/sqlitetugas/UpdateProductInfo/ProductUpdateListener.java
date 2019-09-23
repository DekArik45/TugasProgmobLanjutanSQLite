package com.example.sqlitetugas.UpdateProductInfo;

import com.example.sqlitetugas.pojo.Product;

public interface ProductUpdateListener {
    void onProductInfoUpdated(Product product, int position);
}
