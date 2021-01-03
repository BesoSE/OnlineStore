package com.learn.OnlineStore.service;



import com.learn.OnlineStore.model.Document;

import java.util.List;

public interface DocumentService {
    Document save(byte[] content,long size,String name);
    List<Document> getAllFiles();
    Document findById(Long id);
}
