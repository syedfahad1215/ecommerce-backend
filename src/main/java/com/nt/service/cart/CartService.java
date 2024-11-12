package com.nt.service.cart;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.nt.exception.ResourceNotFoundException;
import com.nt.model.Cart;
import com.nt.repo.CartItemRepository;
import com.nt.repo.CartRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService{
	
	private final CartRepository cartRepository;
	
    private final CartItemRepository cartItemRepository;
    
    private final AtomicLong cartIdGenerator = new AtomicLong(0);
	

	@Override
	public Cart getCart(Long id) {
		Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
	}

	@Transactional
	@Override
	public void clearCart(Long id) {
		Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(id);
        cart.getItems().clear();
        cartRepository.deleteById(id);
		
	}

	@Override
	public BigDecimal getTotalPrice(Long id) {
		 Cart cart = getCart(id);
	     return cart.getTotalAmount();
	}

	@Override
	public Long initializeNewCart() {
		Cart newCart = new Cart();
        Long newCartId = cartIdGenerator.incrementAndGet();
        newCart.setId(newCartId);
        return cartRepository.save(newCart).getId();
	}

	@Override
	public Cart getCartByUserId(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public Cart getCartByUserId(Long userId) {
//		 return cartRepository.findByUserId(userId);
//	}

}
