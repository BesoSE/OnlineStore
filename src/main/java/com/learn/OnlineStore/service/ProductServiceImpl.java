package com.learn.OnlineStore.service;

import com.learn.OnlineStore.dto.ProductDto;
import com.learn.OnlineStore.exception.ResourceNotFoundException;
import com.learn.OnlineStore.model.Document;
import com.learn.OnlineStore.model.Product;
import com.learn.OnlineStore.model.User;
import com.learn.OnlineStore.repository.ProductRepository;

import com.learn.OnlineStore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(Long id)  {
        Optional<Product> optional=productRepository.findById(id);
        Product product;
        if(optional.isPresent()){
            product=optional.get();
        }else{
            throw  new ResourceNotFoundException("There is no product with id: "+id);
        }

        return product;

    }

    @Override
    public Product saveProduct(Document d, ProductDto productDto,String email) {
        Product product=new Product();
        product.setTitle(productDto.getTitle());

        try{
            String s=String.valueOf(productDto.getPrice());
          Double p=  Double.parseDouble(s);
            product.setPrice(p);
        }catch (NumberFormatException e){
            throw new NumberFormatException("sadsa");
        }


        product.setDescription(productDto.getDescription());




        product.setImage(new HashSet<Document>(Arrays.asList(d)));
        product.setAuthor(userRepository.findByEmail(email));
        product.setStatus(true);
        product.setDate(new Date());
       return productRepository.save(product);
    }
    @Override
    public Product editProduct(Document d, ProductDto productDto, Set<Document> Oldimg, User author) {
        Product product=new Product();
        product.setId(productDto.getId());
        product.setTitle(productDto.getTitle());

        try{
            String s=String.valueOf(productDto.getPrice());
            Double p=  Double.parseDouble(s);
            product.setPrice(p);
        }catch (NumberFormatException e){
            throw new NumberFormatException("sadsa");
        }


        product.setDescription(productDto.getDescription());

        if(d.getContent().length==0){
            product.setImage(Oldimg);
        }else {

            product.setImage(new HashSet<Document>(Arrays.asList(d)));
        }

        product.setAuthor(author);
        product.setStatus(true);
        product.setDate(new Date());
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id, String email) {
        Optional<Product> optional=productRepository.findById(id);

        Product product;
        if(optional.isPresent()){
            product=optional.get();
            if(product.getAuthor().getEmail().equals(email)){
                productRepository.delete(product);
            }else{
                throw new RuntimeException("You can't delete product with id:" +id);
            }
        }else{
            throw new RuntimeException("There isn't any product with id:" +id);
        }
    }

    @Override
    public List<Product> findAllByAuthor(User user) {
        List<Product> products=productRepository.findByAuthor(user);
        return products;
    }

    @Override
    public void changeStatus(Long id, String email) {
        Optional<Product> optional=productRepository.findById(id);

        Product product;
        if(optional.isPresent()){
            product=optional.get();
            if(product.getAuthor().getEmail().equals(email)){
                if(product.isStatus()==true){
                    product.setStatus(false);
                }else{
                    product.setStatus(true);
                }
                productRepository.save(product);
            }else{
                throw new RuntimeException("You can't change status for product with id:" +id);
            }
        }else{
            throw new RuntimeException("There isn't any product with id:" +id);
        }
    }

    @Override
    public Page<Product> findPaginated(int pageNo, int pageSize,String sortField,String sortDirection) {

        Sort sort=sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable= PageRequest.of(pageNo-1,pageSize,sort);
        return productRepository.findAll(pageable);
    }
}
