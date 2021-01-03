package com.learn.OnlineStore.service;

import com.learn.OnlineStore.dto.FinishPurchase;
import com.learn.OnlineStore.dto.ProductDto;
import com.learn.OnlineStore.model.Order;
import com.learn.OnlineStore.model.Product;
import com.learn.OnlineStore.model.PurchaseItem;
import com.learn.OnlineStore.model.User;
import com.learn.OnlineStore.repository.OrderRepository;
import com.learn.OnlineStore.repository.ProductRepository;
import com.learn.OnlineStore.repository.PurchaseItemRepository;
import com.learn.OnlineStore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PurchaseItemRepository purchaseItemRepository;

    @Override
    public void finishPurchase(FinishPurchase purchase) {


        User user=userRepository.findByEmail(purchase.getEmail());

        purchase.getProductIdproductCount().forEach((k,v)->{
            Product product=productService.findById(k);
            PurchaseItem purchaseItem=new PurchaseItem();
            purchaseItem.setProduct(product);
            purchaseItem.setCount(v);
            purchaseItem.setUser(user);
            purchaseItemRepository.save(purchaseItem);
        });



    }

    @Override
    public void addOrder(FinishPurchase purchase) {
        Order  order=new Order();
        User user=userRepository.findByEmail(purchase.getEmail());
        order.setUser(user);
        order.setComment(purchase.getComment());
        List<PurchaseItem> lp=purchaseItemRepository.findByUserAndOrder(user,null);
        if(!lp.isEmpty()) {


            for (PurchaseItem p : lp) {
                p.setOrder(order);
            }
            orderRepository.save(order);
        }else{
            throw new RuntimeException("Please add some products to do cart.");
        }
    }

    @Override
    public List<PurchaseItem> getCart(String email) {
        User user=userRepository.findByEmail(email);

      return  purchaseItemRepository.findByUserAndOrder(user,null);

    }

    @Override
    public List<Product> getCartProducts(String email) {
        User user=userRepository.findByEmail(email);
        List<Product> porudctList=new ArrayList<>();
        List <PurchaseItem> lp= purchaseItemRepository.findByUserAndOrder(user,null);
        for (PurchaseItem p:lp) {
            porudctList.add(p.getProduct());

        }
    return porudctList;
    }

    @Override
    public void deletePurchaseItem(Long id) {
        Optional<PurchaseItem> optional=purchaseItemRepository.findById(id);
        if(optional.isPresent()){
            purchaseItemRepository.deleteById(id);
        }else{
            throw new RuntimeException("There is no item with id:"+id);
        }
    }
}
