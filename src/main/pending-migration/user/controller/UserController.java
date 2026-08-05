package com.gutfriendly.app.user.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gutfriendly.app.user.dto.ProfileResponseDTO;
import com.gutfriendly.app.user.dto.UpdateProfileDTO;
import com.gutfriendly.app.user.dto.UserLoginDTO;
import com.gutfriendly.app.user.model.UserAddress;
import com.gutfriendly.app.user.model.UserDetails;
import com.gutfriendly.app.user.service.UserService;

@RestController
@RequestMapping("/users")

public class UserController {

    @Autowired
    private UserService service;

    // Registers a new user.
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody UserDetails user) {

        service.saveUser(user);

        return ResponseEntity.ok(
                "User Registered Successfully!"
        );
    }

    // Logs in a user and returns frontend session details.
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody UserLoginDTO loginDTO) {

        UserDetails user =
                service.login(loginDTO);

        return ResponseEntity.ok(
                Map.of(
                        "message", "Login Successful",
                        "userId", user.getUser_id(),
                        "fname", user.getFname(),
                        "rewardPoints", user.getRewardPoints()
                )
        );
    }

    // Returns one user's profile.
    @GetMapping("/profile/{id}")
    public ResponseEntity<?> getProfile(
            @PathVariable int id) {

        return ResponseEntity.ok(
                service.getProfile(id)
        );
    }

    // Updates one user's profile.
    @PutMapping("/profile/{id}")
    public ResponseEntity<?> updateProfile(
            @PathVariable int id,
            @RequestBody UpdateProfileDTO updateDTO) {

        ProfileResponseDTO updatedProfile =
                service.updateProfile(
                        id,
                        updateDTO
                );

        return ResponseEntity.ok(
                updatedProfile
        );
    }

    // Adds an address to a user.
    @PostMapping("/address/{id}")
    public ResponseEntity<?> addAddress(
            @PathVariable int id,
            @RequestBody UserAddress address) {

        UserAddress savedAddress =
                service.addAddress(
                        id,
                        address
                );

        return ResponseEntity.ok(
                savedAddress
        );
    }

    // Returns all addresses of one user.
    @GetMapping("/address/{id}")
    public ResponseEntity<?> getAddress(
            @PathVariable int id) {

        return ResponseEntity.ok(
                service.getAddress(id)
        );
    }

    // Deletes an address using its address ID.
    @DeleteMapping("/address/{id}")
    public ResponseEntity<?> deleteAddress(
            @PathVariable int id) {

        service.deleteAddress(id);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Address deleted successfully!"
                )
        );
    }

    // Deletes an address after verifying user ownership.
    @DeleteMapping("/{userId}/address/{addressId}")
    public ResponseEntity<?> deleteAddress(
            @PathVariable int userId,
            @PathVariable int addressId) {

        service.deleteAddress(
                userId,
                addressId
        );

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Address deleted successfully!"
                )
        );
    }

    // Deletes one user account.
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(
            @PathVariable int id) {

        service.deleteAccount(id);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Account deleted successfully!"
                )
        );
    }
}