package com.paypal.payment.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.paypal.payment.model.PaymentDto;
import com.paypal.payment.service.PaypalPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
public class PaypalPaymentController {
    @Autowired
    private PaypalPaymentService service;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/payment/create")
    public RedirectView createPayment
            (@RequestBody PaymentDto paymentDto) throws PayPalRESTException {

        String cancelUrl = "http://localhost:8081/payment/cancel";
        String successUrl = "http://localhost:8081/payment/success";
        Payment payment = service.createPayment
        (
                Double.valueOf(paymentDto.getTotal()),
                paymentDto.getCurrency(),
                "Paypal",
                "sale",
                paymentDto.getDescription(),
                cancelUrl,
                successUrl
        );

        for(Links link: payment.getLinks()) {
            if (link.getRel().equals("approval_url")) {
                return new RedirectView(link.getHref());
            }
        }

        return new RedirectView("/payment/error");
    }

    @GetMapping("/payment/success")
    public String executePayment (
        @RequestParam("paymentId") String paymentId,
        @RequestParam("PayerID") String payerId
    )
        throws PayPalRESTException {

        Payment payment = service.executePayment(payerId, paymentId);

        if (payment.getState().equals("approved")) {
            return "success";
        }

        throw new RuntimeException("an error occurred");
    }

    @GetMapping("/payment/error")
    public String paymentError() {
        return "error";
    }

    @GetMapping("/payment/cancel")
    public String cancelPayment() {
        return "cancel";
    }
}
