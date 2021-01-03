package com.learn.OnlineStore.controller;

import com.learn.OnlineStore.dto.FinishPurchase;
import com.learn.OnlineStore.service.PurchaseService;
import com.sun.mail.iap.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PurchaseContrlller {

    @Autowired
    private PurchaseService purchaseService;

    @GetMapping("/addToCart/{id}")
    public String formPurchaseItem(@PathVariable("id") Long id, HttpServletRequest httpServletRequest){
        FinishPurchase purchase=new FinishPurchase();
        purchase.setEmail(httpServletRequest.getUserPrincipal().getName());
        Map<Long,Integer> m=new HashMap<>();
        m.put(id,1);
        purchase.setProductIdproductCount(m);
        purchaseService.finishPurchase(purchase);
        return "redirect:/";

    }

    @GetMapping("/cart")
    public String showCart(Model model,HttpServletRequest httpServletRequest){
        FinishPurchase purchase=new FinishPurchase();
        model.addAttribute("products",purchaseService.getCartProducts(httpServletRequest.getUserPrincipal().getName()));
       model.addAttribute("purchase",purchase);
        model.addAttribute("purchaseitems",purchaseService.getCart(httpServletRequest.getUserPrincipal().getName()));
        return "cart";
    }
    @PostMapping("/finishPurchase")
    public String finishPurchase(FinishPurchase purchase,HttpServletRequest httpServletRequest){
        purchase.setEmail(httpServletRequest.getUserPrincipal().getName());
            purchaseService.addOrder(purchase);
        return "redirect:/";
    }

    @GetMapping("deletePurchaseItem/{id}")
    public String deletePurchaseItemFromCart(@PathVariable("id") Long id){
        purchaseService.deletePurchaseItem(id);
        return "redirect:/cart";
    }


}
