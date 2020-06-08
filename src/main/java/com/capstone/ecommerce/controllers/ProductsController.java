package com.capstone.ecommerce.controllers;

import com.capstone.ecommerce.model.Categories;
import com.capstone.ecommerce.model.Product;
import com.capstone.ecommerce.model.ShoppingCart;
import com.capstone.ecommerce.repositories.ProductRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.ArrayList;
import java.util.List;
//import com.capstone.ecommerce.repositories.CategoriesRepository;
@Controller
@SessionAttributes("products")
public class ProductsController {
    private ProductRepository productRepo;
    private ProductRepository imagesRepo;
    //  CONSTRUCTOR
    public ProductsController(ProductRepository productRepo, ProductRepository imagesRepo) {
        this.productRepo = productRepo;
        this.imagesRepo = imagesRepo;
    }
    public void seedProducts() {
        Product product1 = new Product("Rage meme", "5DADE2", "XL", "Shirt", 22.22, "Angry guy melting down",
                false,
                (long) 5);
        Product product2 = new Product("Pepe punch", "95A5A6", "L", "Shirt", 28.22, "Frog person threatening pose", false,
                (long) 23);
        Product product3 = new Product("Pepe sad", "8E44AD", "XL", "Hoodie", 35.78, "Frog person very down", true, (long) 1000);
        Product product4 = new Product("NPC face", "E74C3C", "OSFM", "Hat", 15.99, "Fellow with straight line mouth and " +
                "angly " +
                "eyebrows", false, (long) 9);
        Product product5 = new Product("Rage meme", "FDFEFE", "S", "Hoodie", 35.99, "Angry guy melting down", false, (long) 0);
        productRepo.save(product1);
        productRepo.save(product2);
        productRepo.save(product3);
        productRepo.save(product4);
        productRepo.save(product5);
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);
        products.add(product4);
        products.add(product5);
    }
    @GetMapping("/products/all")
    public String productsIndex(Model model) {
        if (model.getAttribute("products") == null) {
            ShoppingCart blankProducts = new ShoppingCart();
            model.addAttribute("products", blankProducts);
        }
        List<Product> allProducts = productRepo.findAll();
        model.addAttribute("showProducts", allProducts);
        return "products/products";
    }
    @GetMapping("products/t-shirts")
    public String viewTshirts(Model model) {
        model.addAttribute("products", productRepo.findAll());
        return "products/t-shirts";
    }
    @GetMapping("products/pullover")
    public String viewPullover(Model model) {
        model.addAttribute("products", productRepo.findAll());
        return "products/pullover";
    }
    @GetMapping("products/hats")
    public String viewHats(Model model) {
        model.addAttribute("products", productRepo.findAll());
        return "products/hats";
    }
    @GetMapping("products/t-shirts/{id}")
    public String individualTshirt(Model model, @PathVariable("id") long id) {
        Product tshirt = productRepo.getOne(id);
        model.addAttribute("tshirt", tshirt);
        return "products/t-shirts/show";
    }
    @GetMapping("products/pullover/{id}")
    public String individualPullover(Model model, @PathVariable long id) {
        Product aProduct = productRepo.getOne(id);
        model.addAttribute("pullover", aProduct);
        return "products/pullover/show";
    }
    @GetMapping("products/hats/{id}")
    public String individualHat(Model model, @PathVariable long id) {
        Product aProduct = productRepo.getOne(id);
        model.addAttribute("hat", aProduct);
        return "products/hats/show";
    }
    @GetMapping("/products/{id}")
    public String singleProduct(Model model, @PathVariable long id) {
        double salePrice = 0;
        Product aProduct = productRepo.getOne(id);
        model.addAttribute("product", aProduct);
        if(aProduct.getSpecial() == true){
            salePrice = aProduct.getPrice() - (aProduct.getPrice()*0.42);
            salePrice = Math.round(salePrice * 100.00) / 100.00;
        }
        model.addAttribute("salePrice", salePrice);
        List<Categories> categories = aProduct.getCategories();
        String cNames = "";
        for (Categories category : categories) {
            cNames = cNames + category.getCategory() + ", ";
        }
        cNames = StringUtils.chop(cNames);
        cNames = StringUtils.chop(cNames);
        model.addAttribute("categories", cNames);
        return "products/product";
    }
    @PostMapping("/products/search")
    public String searchProduct(@RequestParam(name = "keyword") String keyword,
                                @RequestParam(name = "choice") String choice,
                                RedirectAttributes redirectAttributes) {
        List<Product> chosenProducts = new ArrayList<>();
        int i = 1;
        if (choice.equals("name")) {
            chosenProducts = productRepo.findByNameContaining(keyword);
        } else if (choice.equals("category")) {
            chosenProducts = productRepo.findByCategoriesContaining(keyword);
//            for(i = 0; i < chosenProducts.size(); i++){
//              for(Product product : chosenProducts){
//                    if(chosenProducts.get(i).getId() == product.getId()){
//                        chosenProducts.remove(chosenProducts.get(i));
//                    }
//                }
//            }
        }
        redirectAttributes.addFlashAttribute("showProducts", chosenProducts);
        return "redirect:search";
    }
    @GetMapping("/products/search")
    public String searchProductLander(Model model,
                                      @ModelAttribute   ("showProducts") ArrayList<Product> showProducts){
        System.out.println(showProducts);
        model.addAttribute("showProducts", showProducts);
        return "products/products";
    }
    public void main(String[] args) {
        seedProducts();
    }
    //  SEARCH
//    @PostMapping("/products/search")
//
//    public String searchProduct(@RequestParam(name = "keyword") String keyword, Model model) {
//        List<Product> products = productRepo.findByNameContaining(keyword);
//        model.addAttribute("products", products);
//        return "products/index";
//    }
}
