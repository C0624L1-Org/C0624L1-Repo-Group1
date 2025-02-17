package com.example.shoplaptop.controller;

import com.example.shoplaptop.model.*;
import com.example.shoplaptop.model.dto.OrderSummaryDTO;
import com.example.shoplaptop.service.ICartItemService;
import com.example.shoplaptop.service.IOrderService;
import com.example.shoplaptop.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller

public class OrderController {
    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICartItemService iCartItemService;

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private GlobalControllerAdvice globalControllerAdvice;

    @GetMapping(value = "/home/order")
    public String showAllOrderPageCustomer(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                           @RequestParam(value = "s", required = false, defaultValue = "-1") int s,
                                           Model model) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<OrderSummary> orders = null;
        if (s >= 0 && s <= 2) {
            System.out.println("Status: " + OrderStatus.values()[s]);
            orders = iOrderService.findAllByOrderStatus(globalControllerAdvice.currentUser().getId(), OrderStatus.values()[s], pageable);
            System.out.println(orders.toString());
        } else {
            orders = iOrderService.findAllByUserId(globalControllerAdvice.currentUser().getId(), pageable);
        }
        model.addAttribute("orders", orders);
        model.addAttribute("status", OrderStatus.values());
        model.addAttribute("s", s);
        return "dashboard/orders/customer/list";
    }


    @GetMapping("/home/order/{id}/detail")
    public String showOrderDetail(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        OrderSummary order = iOrderService.getById(id);
        if (order == null) {
            System.out.println("Order Summary is null");
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", "Gặp lỗi khi thực hiện chức năng này");
            return "redirect:/home/order";
        } else {
            List<OrderItem> orderItemList = iOrderService.getOrderItemsByOrderId(order.getId());
            model.addAttribute("orderItemList", orderItemList);
            model.addAttribute("order", order);
            model.addAttribute("status", OrderStatus.values());
            return "dashboard/orders/customer/detail";
        }
    }

    @GetMapping("/home/order/add")
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
        return "dashboard/orders/customer/view";
    }

    @PostMapping("/home/order/{user}/create")
    public String createOrder(@PathVariable("user") Long id,
                              @Valid @ModelAttribute("orderDTO") OrderSummaryDTO orderDTO, BindingResult bindingResult,
                              Model model, RedirectAttributes redirectAttributes) {
        Users user = iUserService.getById(id);
        orderDTO.setOrderDateTime(java.time.LocalDateTime.now());
        orderDTO.setOrderStatus(OrderStatus.processing);
        orderDTO.setPaymentType(PaymentType.CASH);
        orderDTO.setPaid(false);

        new OrderSummaryDTO().validate(orderDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("cartItemList", iCartItemService.getCartItemsByUser(user));
            model.addAttribute("orderDTO", orderDTO);
            model.addAttribute("user", user);
            model.addAttribute("totalPrice", iCartItemService.totalPriceInCartItemOfUser(user));
            model.addAttribute("paymentTypes", PaymentType.values());
            return "dashboard/orders/customer/view";
        }
        OrderSummary orderSummary = new OrderSummary();
        BeanUtils.copyProperties(orderDTO, orderSummary);

        OrderSummary savedOrder = iOrderService.save(orderSummary);
        iOrderService.saveOrderItems(savedOrder, iUserService.getById(id));

        List<CartItem> cartItemList = iCartItemService.getCartItemsByUser(user);
        iCartItemService.deleteByUser(user);

        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Đặt hàng thành công");
        return "redirect:/home";
    }


    @GetMapping("/dashboard/orders")
    public String showOrdersPage(@RequestParam(value = "page", required = false, defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 3);
        Page<OrderSummary> orders = iOrderService.findAll(pageable);
        model.addAttribute("orders", orders);
        model.addAttribute("processingOrderStatus", OrderStatus.processing);
        model.addAttribute("status", OrderStatus.values());
        return "dashboard/orders/admin/list";
    }

    @GetMapping("/dashboard/orders/{id}/detail")
    public String showOrderDetailPage(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        OrderSummary order = iOrderService.getById(id);
        if (order == null) {
            System.out.println("Order Summary is null");
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", "Gặp lỗi khi thực hiện chức năng này");
            return "redirect:/dashboard/orders";

        } else {
            List<OrderItem> orderItemList = iOrderService.getOrderItemsByOrderId(order.getId());
            model.addAttribute("orderItemList", orderItemList);
            model.addAttribute("order", order);
            model.addAttribute("status", OrderStatus.values());
            return "dashboard/orders/admin/detail";
        }
    }

    @GetMapping("/dashboard/orders/{id}/success")
    public String successOrderSummary(@PathVariable Integer id, @RequestParam(value = "page", required = false, defaultValue = "0") int page, RedirectAttributes redirectAttributes) {
        OrderSummary order = iOrderService.getById(id);
        order.setOrderStatus(OrderStatus.successful);
        iOrderService.save(order);

        //Email
        String emailSubject = "Cảm ơn bạn đã đặt hàng tại ShopLaptop EO ẾT AI!";
        String emailContent = "Xin chào " + order.getFullName() + ",\n\n" +
                "Đơn hàng của bạn đã được xác nhận thành công với thông tin:\n" +
                "Mã đơn hàng: " + order.getId() + "\n" +
                "Ngày đặt: " + order.getOrderDateTime() + "\n" +
                "Tổng tiền: " + order.getOrderAmount() + "\n" +
                "Trạng thái: Đang giao hàng" + "\n\n" +
                "Cảm ơn bạn đã mua sắm tại ShopLaptop EO ẾT AI!";
        if (order.getUser() != null && order.getUser().getEmail() != null) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(order.getUser().getEmail());
            message.setSubject(emailSubject);
            message.setText(emailContent);
            javaMailSender.send(message);
        }
        System.out.println(order.toString());
        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Xác nhận trạng thái đơn hàng thành công");
        return "redirect:/dashboard/orders?page=" + page;
    }


    @GetMapping("/dashboard/orders/{id}/fail")
    public String failOrderSummary(@PathVariable Integer id, @RequestParam(value = "page", required = false, defaultValue = "0") int page, RedirectAttributes redirectAttributes) {
        OrderSummary order = iOrderService.getById(id);
        order.setOrderStatus(OrderStatus.failed);
        iOrderService.save(order);
        //Email
        String emailSubject = "Cảm ơn bạn đã đặt hàng tại ShopLaptop EO ẾT AI!";
        String emailContent = "Xin chào " + order.getFullName() + ",\n\n" +
                "Đơn hàng của bạn đã bị từ chối với thông tin:\n" +
                "Mã đơn hàng: " + order.getId() + "\n" +
                "Ngày đặt: " + order.getOrderDateTime() + "\n" +
                "Tổng tiền: " + order.getOrderAmount() + "\n" +
                "Trạng thái: Chúng tôi không thể liên lạc với bạn qua Số điện thoại: " + order.getPhoneNumber() + "\n\n" +
                "Cảm ơn bạn đã quan tâm tới ShopLaptop EO ẾT AI!";
        if (order.getUser() != null && order.getUser().getEmail() != null) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(order.getUser().getEmail());
            message.setSubject(emailSubject);
            message.setText(emailContent);
            javaMailSender.send(message);
        }
        System.out.println(order.toString());
        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Xác nhận trạng thái đơn hàng thành công");
        return "redirect:/dashboard/orders?page=" + page;
    }
}
