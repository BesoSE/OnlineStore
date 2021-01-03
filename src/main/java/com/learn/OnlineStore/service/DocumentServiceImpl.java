package com.learn.OnlineStore.service;


import com.learn.OnlineStore.model.Document;
import com.learn.OnlineStore.repository.DocumentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentsRepository documentsRepository;


    @Override
    public Document save(byte[] content, long size, String name) {


        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        Document d=new Document();
        d.setContent(content);
        d.setUploadTime(new Date());

        d.setName(generatedString+name);
        d.setSize(size);
       return documentsRepository.save(d);
    }

    @Override
    public List<Document> getAllFiles() {
        return documentsRepository.findAll();
    }

    @Override
    public Document findById(Long id) {
        Optional<Document> optional=documentsRepository.findById(id);
        Document document;
        if(optional.isPresent()){
            document=optional.get();
        }else {
            throw new RuntimeException("There is no file with id: "+id);
        }
        return  document;
    }
}
