package com.capstone.ecommerce.controllers;

import com.capstone.ecommerce.model.Product;
import com.capstone.ecommerce.model.ShoppingCart;
import com.capstone.ecommerce.model.User;
import com.capstone.ecommerce.repositories.ProductImagesRepository;
import com.capstone.ecommerce.repositories.ProductRepository;
import com.capstone.ecommerce.repositories.TransactionProductRepository;
import com.capstone.ecommerce.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@SessionAttributes({"products", "category", "user"})
public class HomeController {
    private ProductRepository productRepo;
    private ProductImagesRepository productImagesRepo;
    private final UserRepository userRepo;
    private final TransactionProductRepository transProdRepo;


    public HomeController(ProductRepository productRepo, ProductImagesRepository productImagesRepo, UserRepository userRepo, TransactionProductRepository transProdRepo) {
        this.productRepo = productRepo;
        this.productImagesRepo = productImagesRepo;
        this.userRepo = userRepo;
        this.transProdRepo = transProdRepo;
    }



    @GetMapping("/")
    public String welcome(Model model) {

        if(model.getAttribute("products") == null) {
            ShoppingCart products = new ShoppingCart();
            model.addAttribute("products", products);
        }
        if (model.getAttribute("category") == null) {
            String category = "";
            model.addAttribute("category", category);
        }
//      MADE CHANGE HERE
        List<Product> allProducts = productRepo.findAll();
        model.addAttribute("showProducts", allProducts);

        return "home";
    }

    @GetMapping("products/productInventory")
    public String getProducts(Model model) {
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")){
            return "home";
        }
        User person = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userRepo.getOne(person.getId());
        if(user.getAdmin()){
            model.addAttribute("user", user);
            List<Product> products = productRepo.findAll();
            model.addAttribute("products", products);
            return "products/productInventory";
        }
      return "home";
    }

//  ADD

    @GetMapping("products/productInventory/add")
    public String showAddProduct(Model model) {
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")){
            return "home";
        }
        User person = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userRepo.getOne(person.getId());
        if(user.getAdmin()){
            model.addAttribute("user", user);
            return "products/productInventory";
        }
        return "home";
    }

//    @PostMapping("products/productInventory/add")
//    public String addProductPost(@RequestParam (name = "name") String name,
//                                 @RequestParam (name = "color") String color,
//                                 @RequestParam (name = "size") String size,
//                                 @RequestParam (name = "type") String type,
//                                 @RequestParam (name = "price") float price,
//                                 @RequestParam (name = "quan") long quan,
//                                 Model model) {
//        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")){
//            return "home";
//        }
//        User person = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User user = this.userRepo.getOne(person.getId());
//        if(user.getAdmin()){
//            model.addAttribute("user", user);
//            Product product = new Product();
//            product.setName(name);
//            product.setColor(color);
//            product.setSize(size);
//            product.setType(type);
//            product.setPrice(price);
//            product.setQuantity(quan);
//
//            productRepo.save(product);
//            return "redirect:/products/productInventory";
//        }
//        return "home";
//    }

//  DELETE
    @Transactional
    @PostMapping("productInventory/delete")
    public String deleteProductPost(@RequestParam (name = "deleteId") long id, Model model) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            return "home";
        }
        User person = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userRepo.getOne(person.getId());
        Product product = productRepo.findById(id);
        if (user.getAdmin()) {
            model.addAttribute("user", user);
//            transProdRepo.deleteByProductId(id);
            transProdRepo.deleteByProduct(product);
            productRepo.deleteById(id);
            return "redirect:/products/productInventory";
        }
        return "home";
    }

        @PostMapping("products/productInventory/add")
        public String addProductPost(@RequestParam (name = "name") String name,
                @RequestParam (name = "color") String color,
                @RequestParam (name = "size") String size,
                @RequestParam (name = "type") String type,
        @RequestParam (name = "price") float price,
        @RequestParam (name = "quan") long quan,
        @RequestParam (name = "image") String image) {
            Product product = new Product();
            product.setName(name);
            product.setColor(color);
            product.setSize(size);
            product.setType(type);
            product.setPrice(price);
            product.setQuantity(quan);
            product.setImage(image);
            productRepo.save(product);
            return "redirect:/products/productInventory";
        }

//  EDIT
    @GetMapping("/productsInventory/edit/{id}")
    public String editProductForm(@PathVariable long id, Model model) {
            model.addAttribute("product", productRepo.getOne(id));
            return "products/productInventory";
    }

    @PostMapping("/productsInventory/edit/{id}")
    public String editProduct(
                              @PathVariable long id,
                              @RequestParam (name = "name") String name,
                              @RequestParam (name = "color") String color,
                              @RequestParam (name = "size") String size,
                              @RequestParam (name = "type") String type,
                              @RequestParam (name = "price") float price,
                              @RequestParam (name = "quan") long quan) {
        Product product = productRepo.getOne(id);
        product.setName(name);
        product.setColor(color);
        product.setSize(size);
        product.setType(type);
        product.setPrice(price);
        product.setQuantity(quan);
        productRepo.save(product);
        return "redirect:/products/productInventory";
    }


    //  DELETE
    @GetMapping("/productsInventory/delete/{id}")
    public String deleteProduct(@PathVariable long id) {
        productRepo.deleteById(id);
        return "redirect:/products/productInventory";
    }


//  SEARCH
    @PostMapping("productsInventory/search")
    public String searchProduct(@RequestParam (name = "keyword") String keyword, Model model) {
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")){
            return "home";
        }
        User person = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userRepo.getOne(person.getId());
        if(user.getAdmin()){
            model.addAttribute("user", user);
            List<Product> products = productRepo.findByNameContaining(keyword);
            model.addAttribute("products", products);
            return "products/productInventory";
        }
        return "home";
    }
}
