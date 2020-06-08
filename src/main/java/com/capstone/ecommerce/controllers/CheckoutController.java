package com.capstone.ecommerce.controllers;


import com.capstone.ecommerce.model.*;
import com.capstone.ecommerce.repositories.*;
import com.capstone.ecommerce.services.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
@SessionAttributes("products")
public class CheckoutController{

    private final UserRepository userRepo;
    private final TransactionRepository transactionRepo;
    private final AddressRepository addressRepo;
    private final ProductRepository productRepo;
    private final TransactionProductRepository transProdRepo;

    public CheckoutController(UserRepository userRepo, TransactionRepository transactionRepo, AddressRepository addressRepo, ProductRepository productRepo, TransactionProductRepository transProdRepo) {
        this.userRepo = userRepo;
        this.transactionRepo = transactionRepo;
        this.addressRepo = addressRepo;
        this.productRepo = productRepo;
        this.transProdRepo = transProdRepo;
    }

    public List<Product> comparison(List<Product> purchasableProducts) {
        List<Product> comparing = new ArrayList<>();

        for (Product product : purchasableProducts) {
            comparing.add(this.productRepo.getOne(product.getId()));
        }
        return comparing;
    }

//    @Value("${STRIPE_PUBLIC_KEY}")
    private String stripePublicKey = "pk_test_B1PgkqwpndJUHJCdlY0I9leL00c395TbE5";


    @GetMapping("/baddress")
    public String goToBillAddress(Model model) {
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")){
            return "home";
        }
        User shopper = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Address bill_address = this.addressRepo.findByUserAndAddresstype(shopper, "Billing");
        if (bill_address != null) {
            model.addAttribute("bill_address", bill_address);
        } else {
            bill_address = new Address();
            bill_address.setAddresstype("Billing");
            bill_address.setUser(shopper);
            model.addAttribute("bill_address", bill_address);
        }
        return "purchases/baddress";
    }

    @PostMapping("/baddress")
    public String submitBillAddress(Model model,
                                    Address bill_address) {
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")){
            return "home";
        }
        User shopper = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (bill_address.getId() > 0) {
//            this.addressRepo.save(bill_address);
//        } else {
//            bill_address.setUser(shopper);
            this.addressRepo.save(bill_address);
//        }
        model.addAttribute("bill_address", bill_address);
        Address ship_address = this.addressRepo.findByUserAndAddresstype(shopper, "Shipping");
        if (ship_address != null) {
            model.addAttribute("ship_address", ship_address);
        } else {
            ship_address = new Address();
            ship_address.setAddresstype("Shipping");
            ship_address.setUser(shopper);
            model.addAttribute("ship_address", ship_address);
        }
        return "purchases/saddress";
    }

    @PostMapping("/saddress")
    public String submitShipAddress(Model model,
                                    Address ship_address,
                                    @ModelAttribute("products") ShoppingCart products) {
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")){
            return "home";
        }
        double total = 0.00;
        User shopper = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (ship_address.getId() > 0) {
//            this.addressRepo.save(ship_address);
//        } else {
            this.addressRepo.save(ship_address);
//        }

        Address bill_address = this.addressRepo.findByUserAndAddresstype(shopper, "Billing");
        for (Product product : products) {
            total = total + product.getPrice();
        }
        total = Math.round(total * 100.00) / 100.00;
        model.addAttribute("total", total);
        model.addAttribute("bill_address", bill_address);
        model.addAttribute("ship_address", ship_address);
        model.addAttribute("products", products);
        model.addAttribute("stripePublicKey", stripePublicKey);
        model.addAttribute("currency", "USD");
        model.addAttribute("email", shopper.getEmail());
        return "purchases/checkout";
    }


    @GetMapping("/addresses")
    public String goToAddressEntry(Model model) {
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")){
            return "home";
        }
        User shopper = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Address ship_address = this.addressRepo.findByUserAndAddresstype(shopper, "Shipping");
        Address bill_address = this.addressRepo.findByUserAndAddresstype(shopper, "Billing");
        if (ship_address != null) {
            model.addAttribute("ship_address", ship_address);
        } else {
            ship_address = new Address();
            ship_address.setAddresstype("Shipping");
            model.addAttribute("ship_address", ship_address);
        }
        if (bill_address != null) {
            model.addAttribute("bill_address", bill_address);
        } else {
            bill_address = new Address();
            bill_address.setAddresstype("Billing");
            model.addAttribute("bill_address", bill_address);
        }

        return "purchases/addresses";

    }

    @PostMapping("/addresses")
    public String submitAddresses(Model model,
                                  Address bill_address,
                                  Address ship_address,
                                  @ModelAttribute("products") ShoppingCart products) {
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")){
            return "home";
        }
        User shopper = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (bill_address.getId() > 0) {

            this.addressRepo.save(bill_address);
        } else {
            bill_address.setUser(shopper);
            this.addressRepo.save(bill_address);
        }
        if (ship_address.getId() > 0) {
            this.addressRepo.save(ship_address);
        } else {
            ship_address.setUser(shopper);
            this.addressRepo.save(ship_address);
        }
        model.addAttribute("bill_address", bill_address);
        model.addAttribute("ship_address", ship_address);
        model.addAttribute("products", products);
        return "purchases/checkout";
    }


    @Autowired
    private StripeService stripeService;
//
//    @PostMapping("/create-charge")
//    public @ResponseBody
//    Response createCharge(String email, String token) {
//        //validate data
//        if (token == null) {
//            return new Response(false, "Stripe payment token is missing. Please, try again later.");
//        }
//
//        //create charge
//        String chargeId = stripeService.createCharge(email, token, 999); //$9.99 USD
//        if (chargeId == null) {
//            return new Response(false, "An error occurred while trying to create a charge.");
//        }
//
//        // You may want to store charge id along with order information
//
//        return new Response(true, "Success! Your charge id is " + chargeId);
//    }

    @PostMapping(value = "/charge", produces = "application/json")
    @ResponseBody
    public String charge(Model model,
                         Address bill_address,
                         Address ship_address,
                         @ModelAttribute("products") ShoppingCart products,
                         @RequestParam (name= "total") double total,
                        @RequestParam (name = "token") String token) throws Exception {
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")){
            return "home";
        }
        List<Product> originals = new ArrayList<>();
        originals = comparison(products);
        boolean isError = false;
        String errMessage = "";
        Transaction newTransaction = new Transaction();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date saleDate = new Date();
        User shopper = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User testShopper = this.userRepo.getOne(shopper.getId());
        String name = ship_address.getFirstname() + " " + ship_address.getLastname();
        String email = shopper.getEmail();
        model.addAttribute("total", total);
        if(testShopper.getStripeToken() == null){
            String customerId = stripeService.createCustomer(token, email);
            testShopper.setStripeToken(customerId);
            this.userRepo.save(testShopper);
        }
        try {
            String id = stripeService.chargeExistingCard(testShopper.getStripeToken(), total);
            newTransaction.setUser(testShopper);
            newTransaction.setStripeTransID(id);
            newTransaction.setCreated_at(formatter.format(saleDate));
            newTransaction.setTransactionStatus("Paid - Pending shipment");
            newTransaction.setTransactionType("Sale");
            newTransaction.setFinalAmount(total);
            this.transactionRepo.save(newTransaction);
            Transaction thisTransaction = this.transactionRepo.findByStripeTransID(id);
            System.out.println(thisTransaction);
            for(Product product: products){
                System.out.println("Test1");
                Transactions_Product TransProd = new Transactions_Product();
                System.out.println("Test2");
                TransProd.setProduct(product);
                TransProd.setTransaction(thisTransaction);
                TransProd.setQuantity(product.getQuantity());
                System.out.println(TransProd.getTransaction());
                System.out.println(TransProd.getProduct());
                System.out.println(TransProd.getQuantity());
                System.out.println("Test3");
                this.transProdRepo.save(TransProd);
                System.out.println("Test4");
            }
            for(int i = 0; i < originals.size(); i++){
                originals.get(i).setQuantity(originals.get(i).getQuantity() - products.get(i).getQuantity());
                this.productRepo.save(originals.get(i));
            }
            products = new ShoppingCart();
            model.addAttribute("products", products);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            isError = true;
            errMessage = "There was a problem with your card - please try again or contact your issuing agency!";
        }
//        model.addAttribute("tId", newTransaction.getId());
//        model.addAttribute("status", newTransaction.getTransactionStatus());
//        model.addAttribute("balance_transaction", id);

        return "{" +
                    "\"url\":\"/result\"," +
                    "\"error\":{"+
                        "\"isError\":\""+isError+"\","+
                        "\"message\":\""+errMessage+"\""+
                    "}"+
                "}";

    }


    @GetMapping("/result")
    private String goToResult(Model model){
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")){
            return "home";
        }
        return "purchases/result";
    }

//        @ExceptionHandler(StripeException.class)
//    public String handleError(Model model, StripeException ex) {
//        model.addAttribute("error", ex.getMessage());
//        return "result";


//    @GetMapping("/checkout")
//    public String checkout(Model model) {
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        model.addAttribute("amount", 50 * 100); // in cents
//        model.addAttribute("stripePublicKey", stripePublicKey);
//        model.addAttribute("currency", ChargeRequest.Currency.EUR);
//        return "checkout";
//    }


    }
