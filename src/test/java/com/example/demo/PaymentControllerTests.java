package com.example.demo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import org.mockito.ArgumentMatchers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.example.demo.bean.*;
import com.example.demo.service.ShoppingCartService;
import com.example.demo.service.UserService;
import com.example.demo.paypal.*;

@WebMvcTest(PaymentControllerTests.class)
public class PaymentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private ShoppingCartService shoppingCartService;

    @InjectMocks
    private PaymentControllerTests yourController;

    @Autowired
    private PayPalIntegration payPalintegration;
    
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOrdemProduto_Success() throws Exception {
        Usuario mockUsuario = new Usuario();
        ShoppingCart mockCart = new ShoppingCart();
        Product mockProduct = new Product();
        mockProduct.setPrice(100.0);
        mockCart.setProducts(List.of(mockProduct));

        when(userService.getLoggedInUserId()).thenReturn(1L);
        when(userService.getUserById(anyLong())).thenReturn(Optional.of(mockUsuario));
        when(shoppingCartService.getShoppingCartById(anyLong())).thenReturn(mockCart);
        when(payPalintegration.createOrder(anyString(), anyString())).thenReturn("orderId");

        mockMvc.perform(post("/1/criarOrdem")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreateOrdemProduto_UserNotFound() throws Exception {
        when(userService.getLoggedInUserId()).thenReturn(1L);
        when(userService.getUserById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(post("/1/criarOrdem")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateOrdemProduto_ErrorCreatingOrder() throws Exception {
        Usuario mockUsuario = new Usuario();
        ShoppingCart mockCart = new ShoppingCart();
        Product mockProduct = new Product();
        mockProduct.setPrice(100.0);
        mockCart.setProducts(List.of(mockProduct));

        when(userService.getLoggedInUserId()).thenReturn(1L);
        when(userService.getUserById(anyLong())).thenReturn(Optional.of(mockUsuario));
        when(shoppingCartService.getShoppingCartById(anyLong())).thenReturn(mockCart);
        when(payPalintegration.createOrder(anyString(), anyString())).thenThrow(new IOException());

        mockMvc.perform(post("/1/criarOrdem")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
