package com.learn.OnlineStore.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

public class FinishPurchase {
    @NotNull
    private Map<Long,Integer> productIdproductCount;

    @NotEmpty
    private String email;

    private String comment;

    public FinishPurchase() {

    }

    public Map<Long, Integer> getProductIdproductCount() {
        return productIdproductCount;
    }

    public void setProductIdproductCount(Map<Long, Integer> productIdproductCount) {
        this.productIdproductCount = productIdproductCount;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
