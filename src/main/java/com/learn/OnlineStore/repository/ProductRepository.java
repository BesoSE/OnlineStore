package com.learn.OnlineStore.repository;

import com.learn.OnlineStore.model.Document;
import com.learn.OnlineStore.model.Product;
import com.learn.OnlineStore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByAuthor(User user);
}
