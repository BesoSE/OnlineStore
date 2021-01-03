package com.learn.OnlineStore.service;

import com.learn.OnlineStore.dto.FinishPurchase;
import com.learn.OnlineStore.model.Product;
import com.learn.OnlineStore.model.PurchaseItem;

import java.util.List;

public interface PurchaseService {
    void finishPurchase(FinishPurchase purchase);
    void addOrder(FinishPurchase purchase);
    List<PurchaseItem> getCart(String email);
    List<Product> getCartProducts(String email);
    void deletePurchaseItem(Long id);
 }
