package in.scalive.swiftcart.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import in.scalive.swiftcart.dto.request.AddToCartRequestDTO;
import in.scalive.swiftcart.dto.request.UpdateCartItemRequestDTO;
import in.scalive.swiftcart.dto.response.CartItemResponseDTO;
import in.scalive.swiftcart.dto.response.CartResponseDTO;
import in.scalive.swiftcart.entity.Cart;
import in.scalive.swiftcart.entity.CartItem;
import in.scalive.swiftcart.entity.Product;
import in.scalive.swiftcart.exception.InsufficientStockException;
import in.scalive.swiftcart.exception.ResourceNotFoundException;
import in.scalive.swiftcart.repository.CartItemRepository;
import in.scalive.swiftcart.repository.CartRepository;
import in.scalive.swiftcart.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

//cart service imp dono cartitem or cart ke kaam samhal legi ham alga
//alag service nhi bana rhe cart or cartitem ko manage krne ke liye

@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
	
	private final CartRepository cartRepo;
	private final ProductRepository prodRepo;
	private final CartItemRepository cartItemRepo;
	
	private Cart getCart(Long userId) {
		Optional<Cart> opt=cartRepo.findByUserId(userId);
		if(opt.isEmpty()) {
			throw new ResourceNotFoundException("Cart","userId",userId);
		}
		return opt.get();
	}
	
	private Product findProductById(Long productId) {
		Optional<Product> opt=prodRepo.findById(productId);
		if(opt.isEmpty()) {
			throw new ResourceNotFoundException("Product","productId",productId);
		}
		return opt.get();
	}
	
	private CartItem findCartItemById(Long cartItemId) {
		Optional<CartItem> opt=cartItemRepo.findById(cartItemId);
		if(opt.isEmpty()) {
			throw new ResourceNotFoundException("CartItem","cartItemId",cartItemId);
		}
		return opt.get();
	}
	
	private void validateProductAvailability(Product product,int requestQuantity) {
		if(!product.getIsAvailable()) {
			throw new InsufficientStockException(product.getName()+"is not available");
		}
		if(product.getStockQuantity()<requestQuantity) {
			throw new InsufficientStockException("Insufficient stock for "+product.getName()+". Available quantity is "+product.getStockQuantity()+", Requested: "+requestQuantity);
		}
	}
	
	private CartResponseDTO mapTCartResponseDTO(Cart cart) {
		List<CartItemResponseDTO> items=new ArrayList<>();
		for(CartItem item:cart.getCartItems()) {
			items.add(mapToCartItemResponseDTO(item));
		}
		return CartResponseDTO.builder()
				.id(cart.getId())
				.userId(cart.getUser().getId())
				.userName(cart.getUser().getFullName())
				.items(items)
				.totalItems(cart.getTotalItems())
				.totalAmount(cart.getTotalAmount())
				.createdAt(cart.getCreatedAt())
				.updatedAt(cart.getUpdatedAt())
				.build();
	}
	
	private CartItemResponseDTO mapToCartItemResponseDTO(CartItem item) {
		Product product=item.getProduct();
		return CartItemResponseDTO.builder()
				.id(item.getId())
				.productId(product.getId())
				.productName(product.getName())
				.productImage(product.getImageUrl())
				.productSku(product.getSku())
				.unitPrice(product.getPrice())
				.quantity(item.getQuantity())
				.subTotal(item.getSubTotal())
				.availableStock(product.getStockQuantity())
				.addedAt(item.getAddedAt())
				.build();
	}

	@Override
	public CartResponseDTO getCartByUserId(Long userId) {
		Cart cart = getCart(userId);
		return mapTCartResponseDTO(cart);
	}

	@Override
	public CartResponseDTO addItemToCart(Long userId, AddToCartRequestDTO dto) {
		Cart cart = getCart(userId);
		Product product = findProductById(dto.getProductId());
		validateProductAvailability(product, dto.getQuantity());
		
		Optional<CartItem> opt= cartItemRepo.findByCartIdAndProductId(cart.getId(),product.getId());
		if(opt.isPresent()) {
			CartItem cartItem = opt.get();//this line is an persistent state mtlb jo data repo  se aaya taki hibernate usko track kr paye
			int newQty=cartItem.getQuantity()+dto.getQuantity();
			validateProductAvailability(product, newQty);
			cartItem.setQuantity(newQty);
			cartItem.calculateSubTotal();
			
			
			//this line is not compulsory b/c of hibernate manage this itself
			cartItemRepo.save(cartItem);
		}else {
			CartItem cartItem=CartItem.builder()
					.cart(cart)
					.product(product)
					.quantity(dto.getQuantity())
					.unitPrice(product.getPrice())
					.build();
			cartItem.calculateSubTotal();
			cartItemRepo.save(cartItem);
		}
		cartRepo.save(cart);
		return mapTCartResponseDTO(cart);
	}

	@Override
	public CartResponseDTO updateCartItem(Long userId, Long cartItemId, UpdateCartItemRequestDTO dto) {
		Cart cart = getCart(userId);
		
		CartItem cartItem = findCartItemById(cartItemId);
		if(!cartItem.getCart().getId().equals(cart.getId())) {
			throw new ResourceNotFoundException("CartItem","id",cartItem);
		}
		validateProductAvailability(cartItem.getProduct(), dto.getQuantity());
		
		cartItem.setQuantity(dto.getQuantity());
		cartItem.calculateSubTotal();
		cart.recalculateTotals();
		
		cartRepo.save(cart);
		return mapTCartResponseDTO(cart);
		
	}

	@Override
	public CartResponseDTO removeItemFromCart(Long userId, Long cartItemId) {
		Cart cart = getCart(userId);
		
		CartItem cartItem = findCartItemById(cartItemId);
		if(!cartItem.getCart().getId().equals(cart.getId())) {
			throw new ResourceNotFoundException("CartItem","id",cartItem);
		}
		cart.removeCartItem(cartItem);
		cart.recalculateTotals();//ye bss safty check jaisa hai
		cartRepo.save(cart);
		return mapTCartResponseDTO(cart);
	}

	@Override
	public CartResponseDTO clearCart(Long userId) {
		Cart cart = getCart(userId);
		cart.clearCart();
		cartRepo.save(cart);
		return mapTCartResponseDTO(cart);
	}

	@Override
	public CartItemResponseDTO getCartItem(Long userId, Long cartItemId) {
		Cart cart = getCart(userId);
		CartItem cartItem = findCartItemById(cartItemId);
		if(!cartItem.getCart().getId().equals(cart.getId())) {
			throw new ResourceNotFoundException("CartItem","id",cartItem);
		}
		return mapToCartItemResponseDTO(cartItem);
	}

	@Override
	public Boolean isProductInCart(Long userId, Long productId) {
		Optional<Cart> opt = cartRepo.findByUserId(userId);
		if(!opt.isPresent()) return false;
		Cart cart = opt.get();
		return cartItemRepo.existsByCartIdAndProductId(cart.getId(), productId);
	}

	@Override
	public CartResponseDTO incrementItemQuantity(Long userId, Long productId, int quantity) {
		if(quantity<=0) {
			throw new IllegalArgumentException("Quantity must be positive");
		}
		Cart cart = getCart(userId);
		Optional<CartItem> opt = cartItemRepo.findByCartIdAndProductId(cart.getId(), productId);
		if(!opt.isPresent()) {
			throw new ResourceNotFoundException("CartItem","productId",productId);
		}
		
		CartItem cartItem = opt.get();
		int newQuantity=cartItem.getQuantity()+quantity;
		validateProductAvailability(cartItem.getProduct(), newQuantity);
		cartItem.setQuantity(newQuantity);
		cartItem.calculateSubTotal();
		cart.recalculateTotals();
		cartRepo.save(cart);
		return mapTCartResponseDTO(cart);
	}

	@Override
	public CartResponseDTO decrementItemQuantity(Long userId, Long productId,int quantity) {
		if(quantity<=0) {
			throw new IllegalArgumentException("Quantity must be Positive");
		}
		Cart cart = getCart(userId);
		Optional<CartItem> opt = cartItemRepo.findByCartIdAndProductId(cart.getId(), productId);
		if(!opt.isPresent()) {
			throw new ResourceNotFoundException("CartItem","productId",productId);
		}
		
		CartItem cartItem = opt.get();
		int newQuantity=cartItem.getQuantity()-quantity;
		if(newQuantity<=0) {
			cart.removeCartItem(cartItem);
		}else {
			cartItem.setQuantity(newQuantity);
			cartItem.calculateSubTotal();
		}
		
		cart.recalculateTotals();
		cartRepo.save(cart);
		return mapTCartResponseDTO(cart);
	}

}
