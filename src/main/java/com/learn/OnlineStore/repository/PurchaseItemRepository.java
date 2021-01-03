package com.learn.OnlineStore.repository;

import com.learn.OnlineStore.model.Order;
import com.learn.OnlineStore.model.PurchaseItem;
import com.learn.OnlineStore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseItemRepository extends JpaRepository<PurchaseItem,Long> {
    List<PurchaseItem> findByUserAndOrder(User idu, Order ido);
}
