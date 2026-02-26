package com.example.ecommerce.controller;

import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@SessionAttributes("cart")
public class ProductController {

    private static final String UPLOAD_DIR = "uploads";

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ModelAttribute("cart")
    public List<CartItem> createCart() {
        return new ArrayList<>();
    }

    @GetMapping("/")
    public String homeRedirect() {
        return "redirect:/products";
    }

    @GetMapping("/products")
    public String listProducts(@RequestParam(value = "keyword", required = false) String keyword,
                               Model model) {
        List<Product> products = productService.searchProducts(keyword);
        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        return "product-list";
    }

    @GetMapping("/products/{id}")
    public String productDetails(@PathVariable Long id, Model model) {
        Optional<Product> optionalProduct = productService.getProductById(id);
        if (optionalProduct.isEmpty()) {
            return "redirect:/products";
        }
        model.addAttribute("product", optionalProduct.get());
        return "product-detail";
    }

    @GetMapping("/products/new")
    public String showCreateProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "product-form";
    }

    @PostMapping("/products")
    public String createProduct(@ModelAttribute Product product,
                                @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = saveImage(imageFile);
            product.setImageUrl(imageUrl);
        }
        productService.saveProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/products/{id}/edit")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        Optional<Product> optionalProduct = productService.getProductById(id);
        if (optionalProduct.isEmpty()) {
            return "redirect:/products";
        }
        model.addAttribute("product", optionalProduct.get());
        return "product-form";
    }

    @PostMapping("/products/update")
    public String updateProduct(@ModelAttribute Product product,
                                @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = saveImage(imageFile);
            product.setImageUrl(imageUrl);
        }
        productService.saveProduct(product);
        return "redirect:/products";
    }

    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("productId") Long productId,
                            @RequestParam(value = "quantity", defaultValue = "1") int quantity,
                            @ModelAttribute("cart") List<CartItem> cart) {
        if (quantity < 1) {
            quantity = 1;
        }

        Optional<Product> optionalProduct = productService.getProductById(productId);
        if (optionalProduct.isEmpty()) {
            return "redirect:/products";
        }

        Product product = optionalProduct.get();

        CartItem existingItem = null;
        for (CartItem item : cart) {
            if (item.getProduct().getId().equals(product.getId())) {
                existingItem = item;
                break;
            }
        }

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            cart.add(new CartItem(product, quantity));
        }

        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String viewCart(@ModelAttribute("cart") List<CartItem> cart, Model model) {
        double total = 0.0;
        for (CartItem item : cart) {
            total += item.getTotalPrice();
        }
        model.addAttribute("cartItems", cart);
        model.addAttribute("total", total);
        return "cart";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam("productId") Long productId,
                                 @ModelAttribute("cart") List<CartItem> cart) {
        cart.removeIf(item -> item.getProduct().getId().equals(productId));
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkout(@ModelAttribute("cart") List<CartItem> cart, Model model) {
        double total = 0.0;
        for (CartItem item : cart) {
            total += item.getTotalPrice();
        }
        model.addAttribute("cartItems", cart);
        model.addAttribute("total", total);
        return "checkout";
    }

    @PostMapping("/checkout/confirm")
    public String confirmCheckout(@ModelAttribute("cart") List<CartItem> cart, Model model) {
        cart.clear();
        model.addAttribute("message", "Thank you! Your order has been placed (fake order for learning).");
        return "order-success";
    }

    private String saveImage(MultipartFile imageFile) throws IOException {
        // Always use an absolute path so Tomcat does not try to write
        // into its temporary work directory.
        Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFilename = imageFile.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            originalFilename = "image";
        }

        String fileName = System.currentTimeMillis() + "-" + originalFilename;
        Path filePath = uploadPath.resolve(fileName);
        Files.createDirectories(filePath.getParent());
        imageFile.transferTo(filePath.toFile());

        // This URL is what the browser uses. It is mapped in WebConfig
        // to point to the physical uploads/ folder on disk.
        return "/uploads/" + fileName;
    }
}

