package com.example.shoplaptop.controller;

import com.example.shoplaptop.model.CartItem;
import com.example.shoplaptop.model.Product;
import com.example.shoplaptop.model.Users;
import com.example.shoplaptop.repository.IProductRepository;
import com.example.shoplaptop.repository.IUserRepository;
import com.example.shoplaptop.service.IProductService;
import com.example.shoplaptop.service.impl.CartItemService;
import com.example.shoplaptop.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartItemController {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private GlobalControllerAdvice globalControllerAdvice;

    @RequestMapping("")
    public String index(Model model) {
        System.out.println("Vào cart thành công");
        Users user = globalControllerAdvice.currentUser();
        System.out.println("In user " + user.toString());

        model.addAttribute("user", user);
        List<CartItem> cartItemList = cartItemService.getCartItemsByUser(user);
        int countProduct = cartItemService.countProductInCartItemOfUser(user);
        long totalPrice = cartItemService.totalPriceInCartItemOfUser(user);

        model.addAttribute("countProduct", countProduct);
        model.addAttribute("cartItem", cartItemList);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("userId", user.getId());
        return "/listCart";

    }

    @RequestMapping("/add")
    public String addProductInCart(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                   @RequestParam(name = "userId") Long userId,
                                   Model model,
                                   RedirectAttributes redirectAttributes,
                                   @RequestParam(name = "productId") int productId) {
        if (userId == null) {
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", "Bạn cần phải đăng nhập!");
            return "redirect:/login";
        }
        Users user = userRepository.getById(userId);
        Product product = productRepository.getById(productId);

        Pageable pageable = PageRequest.of(page, 10);
        Page<Product> products = iProductService.findAll(pageable);
        model.addAttribute("products", products);
        try {
            cartItemService.addProductToCartItemOfUser(user, product);
            redirectAttributes.addFlashAttribute("messageType", "success");
            redirectAttributes.addFlashAttribute("message", "Đã thêm vào giỏ của bạn!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/home";
    }


    @RequestMapping("/addQuantity")
    public String addQuantityProduct(@RequestParam(name = "userId") long userId,
                                     @RequestParam(name = "productId") int productId,
                                     Model model) {
        Users user = userRepository.getById(userId);
        Product product = productRepository.getById(productId);
        cartItemService.addProductToCartItemOfUser(user, product);
        List<CartItem> cartItemList = cartItemService.getCartItemsByUser(user);
        model.addAttribute("cartItem", cartItemList);
        return "redirect:/cart";
    }

    @RequestMapping("/reduceQuantity")
    public String reduceQuantityProduct(@RequestParam(name = "userId") long userId,
                                        @RequestParam(name = "productId") int productId,
                                        Model model) {
        Users user = userRepository.getById(userId);
        Product product = productRepository.getById(productId);
        cartItemService.removeProductFromCartItemOfUser(user, product);
        return "redirect:/cart";
    }

    @RequestMapping("/remove")
    public String removeCartItem(@RequestParam(name = "userId") Long userId,
                                 @RequestParam(name = "productId") int productId,
                                 RedirectAttributes redirectAttributes) {
        Users user = userRepository.getById(userId);
        Product product = productRepository.getById(productId);
        cartItemService.deleteCartItemCompletely(user, product);
        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Đã xoá sản phẩm khỏi giỏ hàng.");
        return "redirect:/cart";
    }
}
