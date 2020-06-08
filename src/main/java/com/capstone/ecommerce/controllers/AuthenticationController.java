package com.capstone.ecommerce.controllers;

import com.capstone.ecommerce.model.*;
import com.capstone.ecommerce.repositories.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes("products")
public class AuthenticationController {

    private final UserRepository userRepo;
    private final TransactionRepository transactionRepo;
    private final AddressRepository addressRepo;
    private final ProductRepository productRepo;
    private final TransactionProductRepository transProdRepo;

    public AuthenticationController(UserRepository userRepo, TransactionRepository transactionRepo, AddressRepository addressRepo,
                       ProductRepository productRepo, TransactionProductRepository transProdRepo) {
        this.userRepo = userRepo;
        this.transactionRepo = transactionRepo;
        this.addressRepo = addressRepo;
        this.productRepo = productRepo;
        this.transProdRepo = transProdRepo;
    }


    @GetMapping("/admin")
    public String toTheAdminPortal(Model model){
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")){
            return "home";
        }
        User person = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userRepo.getOne(person.getId());
        if(user.getAdmin()){
            model.addAttribute("user", user);
            return "/users/adminportal";
        }
        return "home";
    }

    @GetMapping("/transactions")
    public String toTheTransactions(Model model){
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")){
            return "home";
        }
        User person = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userRepo.getOne(person.getId());
        if(user.getAdmin()){
            model.addAttribute("user", user);
            ArrayList<TransactionTieup> finalSend = new ArrayList<>();
            List<Transaction> baseTransactions = transactionRepo.findAll();
            List<Transactions_Product> transProds = transProdRepo.findAll();
            for(Transaction transaction : baseTransactions){
                List<Long> quantities = new ArrayList<>();
                List<Product> products = new ArrayList<>();
                TransactionTieup saveFull = new TransactionTieup();
                saveFull.setId(transaction.getId());
                saveFull.setCreated_at(transaction.getCreated_at());
                saveFull.setModified_at(transaction.getModified_at());
                saveFull.setStripeId(transaction.getStripeTransID());
                saveFull.setStatus(transaction.getTransactionStatus());
                saveFull.setType(transaction.getTransactionType());
                saveFull.setUsername(transaction.getUser().getUsername());
                saveFull.setEmail(transaction.getUser().getEmail());
                saveFull.setStripeCustomer(transaction.getUser().getStripeToken());
                saveFull.setShipping(addressRepo.findByUserAndAddresstype(transaction.getUser(), "Shipping"));
                saveFull.setBilling(addressRepo.findByUserAndAddresstype(transaction.getUser(), "Billing"));
                saveFull.setTotal(transaction.getFinalAmount());
                for(Transactions_Product transproduct : transProds){
                    if(transaction == transproduct.getTransaction()){
                        quantities.add(transproduct.getQuantity());
                        products.add(transproduct.getProduct());
                    }
                }
                saveFull.setQuantity(quantities);
                saveFull.setProduct(products);
                finalSend.add(saveFull);
            }


            model.addAttribute("transactions", finalSend);
            return "purchases/transactions";
        }
        return "home";
    }


    @GetMapping("/login")
    public String showLoginForm(Model model) {
        if(model.getAttribute("products") == null) {
            ShoppingCart products = new ShoppingCart();
            model.addAttribute("products", products);
        }
        return "users/login";
    }

}
