package com.learn.OnlineStore.controller;

import com.learn.OnlineStore.dto.ProductDto;

import com.learn.OnlineStore.model.Document;
import com.learn.OnlineStore.model.Product;
import com.learn.OnlineStore.model.User;
import com.learn.OnlineStore.service.DocumentService;
import com.learn.OnlineStore.service.ProductService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

@Controller
public class MainController {
    @Autowired
    private ProductService productService;


    @Autowired
    private DocumentService documentService;

    @GetMapping("/")
    public String showProduct(Model model) {
      //  model.addAttribute("listProducts", productService.findAll());
       // return "home";
    return findPaginated(1,"title","asc",model);
    }

    //pagination
    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable("pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model){
        int pageSize=9;
        Page<Product> page=productService.findPaginated(pageNo,pageSize,sortField,sortDir);
        List<Product> productList=page.getContent();
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("listProducts",productList);
        model.addAttribute("sortField",sortField);
        model.addAttribute("sortDir",sortDir);
        model.addAttribute("reverseSortDir",sortDir.equals("asc") ? "desc" : "asc");

        return "home";

    }

    //
    @GetMapping("/login")
    public String formLogin(){

            return "login";



        }

    @GetMapping("/image")
    public void image(@Param("id") Long id, HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");

        Product p = productService.findById(id);
        Set<Document> doc = p.getImage();
        for (Document d : doc) {

            InputStream is = new ByteArrayInputStream(d.getContent());
            IOUtils.copy(is, response.getOutputStream());
        }

    }


    @GetMapping("/saveproduct")
    public String showFormSave(Model model) {
        ProductDto p = new ProductDto();
        model.addAttribute("product", p);


        return "addProduct";
    }


    @PostMapping("/saveproduct")
    public String saveProduct(@Valid ProductDto product, BindingResult bindingResult, Model model,
                              @RequestParam("image") MultipartFile multipartFile, HttpServletRequest httpServletRequest) {


        if (bindingResult.hasErrors()) {
            model.addAttribute("product", product);
            model.addAttribute("bindingResult", bindingResult);

            return "addProduct";
        } else {
            try {
                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());


                Document d = documentService.save(multipartFile.getBytes(), multipartFile.getSize(), fileName);
                productService.saveProduct(d, product,httpServletRequest.getUserPrincipal().getName());
            } catch (NumberFormatException | IOException r) {
                model.addAttribute("product", product);
                model.addAttribute("message", r.getMessage());
                return "addProduct";
            }

            return "redirect:/";
        }


    }


    @GetMapping("/product/{id}")
    public String showProduct(@PathVariable("id") Long id, Model model) {

        if (productService.findById(id) != null) {
            model.addAttribute("product", productService.findById(id));

        } else {
            model.addAttribute("error", "There is no product with id: " + id);
        }
        return "index";
    }



    //edit product
    @GetMapping("/editproduct/{id}")
    public String showFormEditProduct(@PathVariable("id") Long id, Model model,HttpServletRequest httpServletRequest) {
        if(productService.findById(id).getAuthor().getEmail().equals(httpServletRequest.getUserPrincipal().getName())) {
            model.addAttribute("product", productService.findById(id));
            return "editProduct";
        }
        return "redirect:/";
    }


    @PostMapping("/editproduct/{id}")
    public String editProduct(@PathVariable("id") Long id, @Valid ProductDto product,
                              BindingResult bindingResult, Model model,
                              @RequestParam("image") MultipartFile multipartFile,
                              HttpServletRequest httpServletRequest) {
        Product p1=productService.findById(id);
        if(p1.getAuthor().getEmail().equals(httpServletRequest.getUserPrincipal().getName())) {
            if (bindingResult.hasErrors()) {
                model.addAttribute("product", product);
                model.addAttribute("bindingResult", bindingResult);
                return "editProduct";
            } else {
                try {
                    String fileName;

                    if (multipartFile.isEmpty()) {

                        fileName = null;
                    } else {
                        fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

                    }


                    Document d = documentService.save(multipartFile.getBytes(), multipartFile.getSize(), fileName);
                    productService.editProduct(d, product, p1.getImage(), p1.getAuthor());

                } catch (NumberFormatException | IOException r) {
                    model.addAttribute("product", product);
                    model.addAttribute("message", r.getMessage());
                    return "editProduct";
                }
            }
        }else{
            return "redirect:/";
        }

        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") Long id , Model model,HttpServletRequest httpServletRequest){


        try{

            productService.deleteProduct(id,httpServletRequest.getUserPrincipal().getName());
        }catch (RuntimeException e){
            model.addAttribute("message",e.getMessage());
        }

        return "redirect:/";
    }

    @GetMapping("/status/{id}")
    public String changeStatusProduct(@PathVariable("id") Long id,Model model, HttpServletRequest httpServletRequest){

        try{
            productService.changeStatus(id,httpServletRequest.getUserPrincipal().getName());
        }catch (RuntimeException e){
            model.addAttribute("message",e.getMessage());
        }
        return "redirect:/";
    }


}

