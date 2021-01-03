package com.learn.OnlineStore.dto;


import org.springframework.format.annotation.NumberFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ProductDto {

    private Long id;

    @NotNull
    @NotEmpty(message = "Potrebno je da unesete naslov")
    private String title;
    @NotNull
    @NotEmpty(message = "Potrebno je da unesete opis")
    private String description;

    @NotNull
    @Min(value = 1,message = "minimalno 1")
    private double price;






    public ProductDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public Double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
