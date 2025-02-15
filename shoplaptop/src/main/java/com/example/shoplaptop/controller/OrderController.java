package com.example.shoplaptop.controller;

import com.example.shoplaptop.model.*;
import com.example.shoplaptop.model.dto.OrderSummaryDTO;
import com.example.shoplaptop.service.ICartItemService;
import com.example.shoplaptop.service.IOrderService;
import com.example.shoplaptop.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/home/order")
public class OrderController {
    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICartItemService iCartItemService;

    @Autowired
    private IOrderService iOrderService;

    @GetMapping("")
    public String showOrderPage(@RequestParam("user") Long userId, Model model) {
        Users user = iUserService.getById(userId);
        List<CartItem> cartItemList = iCartItemService.getCartItemsByUser(user);
        OrderSummaryDTO orderDTO = new OrderSummaryDTO();
        Long totalPrice = iCartItemService.totalPriceInCartItemOfUser(user);
        orderDTO.setAddress(user.getAddress());
        orderDTO.setFullName(user.getFullName());
        orderDTO.setPhoneNumber(user.getPhone());

        model.addAttribute("cartItemList", cartItemList);
        model.addAttribute("user", user);
        model.addAttribute("orderDTO", orderDTO);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTypes", PaymentType.values());
        return "dashboard/orders/view";
    }

    @PostMapping("/{user}/create")
    public String createOrder(@PathVariable("user") Long id,
                              @Valid @ModelAttribute("orderDTO") OrderSummaryDTO orderDTO, BindingResult bindingResult,
                              Model model, RedirectAttributes redirectAttributes) {
        Users user = iUserService.getById(id);
        orderDTO.setOrderDateTime(java.time.LocalDateTime.now());
        orderDTO.setOrderStatus(OrderStatus.processing);
        orderDTO.setPaid(false);

        new OrderSummaryDTO().validate(orderDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("cartItemList", iCartItemService.getCartItemsByUser(user));
            model.addAttribute("orderDTO", orderDTO);
            model.addAttribute("user",user);
            model.addAttribute("totalPrice", iCartItemService.totalPriceInCartItemOfUser(user));
            model.addAttribute("paymentTypes", PaymentType.values());
            return "dashboard/orders/view";
        }
        OrderSummary orderSummary = new OrderSummary();
        BeanUtils.copyProperties(orderDTO, orderSummary);

        OrderSummary savedOrder = iOrderService.save(orderSummary);
        iOrderService.saveOrderItems(savedOrder, iUserService.getById(id));

        List<CartItem> cartItemList = iCartItemService.getCartItemsByUser(user);
        iCartItemService.deleteByUser(user);

        redirectAttributes.addFlashAttribute("messageType","success");
        redirectAttributes.addFlashAttribute("message", "Đặt hàng thành công");
        return "redirect:/Vss";

    }
}
