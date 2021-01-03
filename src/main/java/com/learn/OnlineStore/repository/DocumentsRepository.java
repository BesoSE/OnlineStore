package com.learn.OnlineStore.repository;


import com.learn.OnlineStore.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentsRepository extends JpaRepository<Document,Long> {

}
