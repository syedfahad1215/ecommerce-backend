package com.nt.service.cart;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.nt.exception.ResourceNotFoundException;
import com.nt.model.Cart;
import com.nt.model.CartItem;
import com.nt.model.Product;
import com.nt.repo.CartItemRepository;
import com.nt.repo.CartRepository;
import com.nt.service.product.IProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {
	
	private final CartItemRepository cartItemRepository;
	
    private final CartRepository cartRepository;
    
    private final IProductService productService;
    
    private final ICartService cartService;
    
    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        //1. Get the cart
        //2. Get the product
        //3. Check if the product already in the cart
        //4. If Yes, then increase the quantity with the requested quantity
        //5. If No, then initiate a new CartItem entry.
    	try {
    		Cart cart = cartService.getCart(cartId);
            Product product = productService.getProductById(productId);
            CartItem cartItem = cart.getItems()
                    .stream()
                    .filter(item -> item.getProduct().getId().equals(productId))
                    .findFirst().orElse(new CartItem());
            if (cartItem.getId() == null) {
                cartItem.setCart(cart);
                cartItem.setProduct(product);
                cartItem.setQuantity(quantity);
                cartItem.setUnitPrice(product.getPrice());
            }
            else {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
            }
            cartItem.setTotalPrice();
            cart.addItem(cartItem);
            cartItemRepository.save(cartItem);
            cartRepository.save(cart);
    	} catch(Exception e) {
    		System.out.println(e.getLocalizedMessage());
    		throw e;
    	}
        
    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
    	try {
    		Cart cart = cartService.getCart(cartId);
    		CartItem itemToRemove = getCartItem(cartId, productId);
            cart.removeItem(itemToRemove);
            cartRepository.save(cart);
    		
    	} catch(Exception e) {
    		System.out.println(e.getLocalizedMessage());
    		throw e;
    	}
        
        
    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) 
    {
        Cart cart = cartService.getCart(cartId);
        cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                });
        BigDecimal totalAmount = cart.getItems()
                .stream().map(CartItem ::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId) 
    {
    	Cart cart = null;
    	try {
    		cart = cartService.getCart(cartId);
    	} catch(Exception e) {
    		System.out.println(e.getLocalizedMessage());
    		throw e;
    	}
        
        return  cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElseThrow(() -> new ResourceNotFoundException("Item not found"));
    }
}
