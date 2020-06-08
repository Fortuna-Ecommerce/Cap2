package com.capstone.ecommerce.controllers;

import com.capstone.ecommerce.model.Product;
import com.capstone.ecommerce.model.ShoppingCart;
import com.capstone.ecommerce.model.ProductImages;
import com.capstone.ecommerce.repositories.ProductImagesRepository;
import com.capstone.ecommerce.repositories.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@SessionAttributes("products")
public class HomeController {
    private ProductRepository productRepo;


    public HomeController(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @GetMapping("/")
    public String welcome(Model model) {

        if(model.getAttribute("products") == null) {
            ShoppingCart products = new ShoppingCart();
            model.addAttribute("products", products);
        }

        return "home";
    }

    @GetMapping("products/productInventory")
    public String getProducts(Model model) {
        List<Product> products = productRepo.findAll();
        model.addAttribute("products", products);
        return "products/productInventory";
    }

//  ADD

    @GetMapping("products/productInventory/add")
    public String showAddProduct() {
        return "products/productInventory";
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
        List<Product> products = productRepo.findByNameContaining(keyword);
        model.addAttribute("products", products);
        return "products/productInventory";
    }
}