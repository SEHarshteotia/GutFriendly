package com.gutfriendly.app.user.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gutfriendly.app.user.dto.AddToCartDTO;
import com.gutfriendly.app.user.dto.CartDTO;
import com.gutfriendly.app.user.dto.CartItemDTO;
import com.gutfriendly.app.user.exception.BadRequestException;
import com.gutfriendly.app.user.exception.ConflictException;
import com.gutfriendly.app.user.exception.ResourceNotFoundException;
import com.gutfriendly.app.admin.model.FoodImages;
import com.gutfriendly.app.admin.model.FoodItemsDetails;
import com.gutfriendly.app.admin.repository.FoodItemsDetailsRepository;
import com.gutfriendly.app.user.model.Cart;
import com.gutfriendly.app.user.model.CartItem;
import com.gutfriendly.app.user.model.UserDetails;
import com.gutfriendly.app.user.repository.CartItemRepository;
import com.gutfriendly.app.user.repository.CartRepository;
import com.gutfriendly.app.user.repository.UserRepo;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepo;

    @Autowired
    private CartItemRepository cartItemRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FoodItemsDetailsRepository foodRepo;

    // Adds a food item to the user's cart.
    // Creates a cart if the user does not already have one.
    // Increases quantity if the same food item already exists.
    @Transactional
    public CartDTO addToCart(
            int userId,
            AddToCartDTO request) {

        if (request == null) {
            throw new BadRequestException(
                    "Cart request is required"
            );
        }

        if (request.getQuantity() <= 0) {
            throw new BadRequestException(
                    "Quantity must be greater than zero"
            );
        }

        UserDetails user = userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        FoodItemsDetails food = foodRepo
                .findById(request.getFoodId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Food item not found"
                        ));

        if (!food.isAvailable()) {
            throw new BadRequestException(
                    "Food item is currently unavailable"
            );
        }

        Cart cart = cartRepo.findByUser(user)
                .orElseGet(() -> createCart(user));

        // A cart can contain food from only one shop.
        validateSameShop(cart, food);

        Optional<CartItem> existingItem =
                cartItemRepo.findByCartCartIdAndFoodFoodId(
                        cart.getCartId(),
                        food.getFoodId()
                );

        if (existingItem.isPresent()) {

            CartItem item = existingItem.get();

            item.setQuantity(
                    item.getQuantity()
                            + request.getQuantity()
            );

            cartItemRepo.save(item);

        } else {

            CartItem item = new CartItem();

            item.setFood(food);
            item.setQuantity(
                    request.getQuantity()
            );
            item.setUnitPrice(
                    food.getPrice()
            );

            cart.addItem(item);

            cartRepo.save(cart);
        }

        return convertToCartDTO(cart);
    }

    // Returns the complete cart of one user.
    @Transactional(readOnly = true)
    public CartDTO getCart(int userId) {

        UserDetails user = userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        Optional<Cart> cartOptional =
                cartRepo.findByUser(user);

        if (cartOptional.isEmpty()) {

            return new CartDTO(
                    0,
                    0,
                    BigDecimal.ZERO,
                    new ArrayList<>()
            );
        }

        return convertToCartDTO(
                cartOptional.get()
        );
    }

    // Replaces the quantity of one cart item.
    @Transactional
    public CartDTO updateQuantity(
            int userId,
            int cartItemId,
            int quantity) {

        if (quantity <= 0) {
            throw new BadRequestException(
                    "Quantity must be greater than zero"
            );
        }

        CartItem item = cartItemRepo.findById(cartItemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart item not found"
                        ));

        validateCartOwnership(
                item.getCart(),
                userId
        );

        item.setQuantity(quantity);

        cartItemRepo.save(item);

        return convertToCartDTO(
                item.getCart()
        );
    }

    // Removes one item from the user's cart.
    @Transactional
    public CartDTO removeItem(
            int userId,
            int cartItemId) {

        CartItem item = cartItemRepo.findById(cartItemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart item not found"
                        ));

        Cart cart = item.getCart();

        validateCartOwnership(
                cart,
                userId
        );

        cart.removeItem(item);

        cartRepo.save(cart);

        return convertToCartDTO(cart);
    }

    // Removes every item from the user's cart.
    @Transactional
    public CartDTO clearCart(int userId) {

        UserDetails user = userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        Cart cart = cartRepo.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart not found"
                        ));

        cart.getItems().clear();

        cartRepo.save(cart);

        return convertToCartDTO(cart);
    }

    // Creates a new empty cart for the user.
    private Cart createCart(
            UserDetails user) {

        Cart cart = new Cart();

        cart.setUser(user);

        return cartRepo.save(cart);
    }

    // Prevents food from different shops
    // from being added to one cart.
    private void validateSameShop(
            Cart cart,
            FoodItemsDetails newFood) {

        if (cart.getItems() == null ||
                cart.getItems().isEmpty()) {

            return;
        }

        int existingShopId = cart.getItems()
                .get(0)
                .getFood()
                .getShop()
                .getShopId();

        int newShopId = newFood
                .getShop()
                .getShopId();

        if (existingShopId != newShopId) {

            throw new ConflictException(
                    "Your cart contains food from another shop. "
                    + "Clear the cart before adding this item."
            );
        }
    }

    // Prevents users from updating or removing
    // another user's cart items.
    private void validateCartOwnership(
            Cart cart,
            int userId) {

        if (cart.getUser().getUser_id() != userId) {

            throw new ConflictException(
                    "This cart does not belong to the user"
            );
        }
    }

    // Converts the Cart entity into CartDTO.
    private CartDTO convertToCartDTO(
            Cart cart) {

        List<CartItemDTO> itemDTOs =
                new ArrayList<>();

        int totalItems = 0;

        BigDecimal totalAmount =
                BigDecimal.ZERO;

        if (cart.getItems() != null) {

            for (CartItem item : cart.getItems()) {

                BigDecimal itemTotal =
                        item.getUnitPrice().multiply(
                                BigDecimal.valueOf(
                                        item.getQuantity()
                                )
                        );

                String imageUrl =
                        getPrimaryImage(
                                item.getFood()
                        );

                CartItemDTO itemDTO =
                        new CartItemDTO(
                                item.getCartItemId(),
                                item.getFood().getFoodId(),
                                item.getFood().getFoodName(),
                                imageUrl,
                                item.getUnitPrice(),
                                item.getQuantity(),
                                itemTotal
                        );

                itemDTOs.add(itemDTO);

                totalItems +=
                        item.getQuantity();

                totalAmount =
                        totalAmount.add(
                                itemTotal
                        );
            }
        }

        return new CartDTO(
                cart.getCartId(),
                totalItems,
                totalAmount,
                itemDTOs
        );
    }

    // Returns the primary food image.
    // Uses the first image when no image is marked primary.
    private String getPrimaryImage(
            FoodItemsDetails food) {

        if (food.getImages() == null ||
                food.getImages().isEmpty()) {

            return null;
        }

        for (FoodImages image : food.getImages()) {

            if (image.isPrimaryImage()) {
                return image.getImageUrl();
            }
        }

        return food.getImages()
                .get(0)
                .getImageUrl();
    }
}