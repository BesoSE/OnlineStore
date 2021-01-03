package com.learn.OnlineStore.service;

import com.learn.OnlineStore.dto.ProductDto;
import com.learn.OnlineStore.model.Document;
import com.learn.OnlineStore.model.Product;
import com.learn.OnlineStore.model.User;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface ProductService {
    List<Product> findAll();
    Product findById(Long id);
    Product saveProduct(Document d, ProductDto productDto,String email);
    Product editProduct(Document d, ProductDto productDto, Set<Document> Oldimg, User author);
    void deleteProduct(Long id,String email);
    void changeStatus(Long id,String email);
    List<Product> findAllByAuthor(User user);
    Page<Product> findPaginated(int pageNo,int pageSize,String sortField,String sortDirection);



}
