package com.gutfriendly.app.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gutfriendly.app.user.dto.ProfileResponseDTO;
import com.gutfriendly.app.user.dto.UpdateProfileDTO;
import com.gutfriendly.app.user.dto.UserLoginDTO;
import com.gutfriendly.app.user.exception.BadRequestException;
import com.gutfriendly.app.user.exception.ConflictException;
import com.gutfriendly.app.user.exception.ResourceNotFoundException;
import com.gutfriendly.app.admin.model.Pincode;
import com.gutfriendly.app.admin.repository.PincodeRepository;
import com.gutfriendly.app.user.model.UserAddress;
import com.gutfriendly.app.user.model.UserDetails;
import com.gutfriendly.app.user.repository.UserAddressRepo;
import com.gutfriendly.app.user.repository.UserRepo;

@Service
public class UserService {

    @Autowired
    private UserRepo repo;

    @Autowired
    private UserAddressRepo addressRepo;

    @Autowired
    private PincodeRepository pincodeRepo;

    // Saves a new user.
    public void saveUser(UserDetails user) {

        if (user == null) {
            throw new BadRequestException(
                    "User details are required"
            );
        }

        repo.save(user);
    }

    // Validates login using phone number and password.
    public UserDetails login(
            UserLoginDTO loginDTO) {

        if (loginDTO == null) {
            throw new BadRequestException(
                    "Login details are required"
            );
        }

        if (loginDTO.getPhoneNo() == null ||
                loginDTO.getPhoneNo().trim().isEmpty()) {

            throw new BadRequestException(
                    "Phone number is required"
            );
        }

        if (loginDTO.getPassword() == null ||
                loginDTO.getPassword().isEmpty()) {

            throw new BadRequestException(
                    "Password is required"
            );
        }

        UserDetails user =
                repo.findByPhoneNo(
                        loginDTO.getPhoneNo().trim()
                );

        if (user == null) {
            throw new ResourceNotFoundException(
                    "User not found"
            );
        }

        if (!user.getPassword()
                .equals(loginDTO.getPassword())) {

            throw new BadRequestException(
                    "Invalid phone number or password"
            );
        }

        if (!user.isIs_active()) {
            throw new ConflictException(
                    "User account is inactive"
            );
        }

        return user;
    }

    // Returns the user's profile.
    public ProfileResponseDTO getProfile(
            int id) {

        UserDetails user = repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        return convertToProfileDTO(user);
    }

    // Updates the user's profile.
    public ProfileResponseDTO updateProfile(
            int id,
            UpdateProfileDTO updateDTO) {

        if (updateDTO == null) {
            throw new BadRequestException(
                    "Profile details are required"
            );
        }

        UserDetails user = repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        if (updateDTO.getFname() == null ||
                updateDTO.getFname().trim().isEmpty()) {

            throw new BadRequestException(
                    "First name is required"
            );
        }

        if (updateDTO.getLname() == null ||
                updateDTO.getLname().trim().isEmpty()) {

            throw new BadRequestException(
                    "Last name is required"
            );
        }

        if (updateDTO.getPhoneNo() == null ||
                updateDTO.getPhoneNo().trim().isEmpty()) {

            throw new BadRequestException(
                    "Phone number is required"
            );
        }

        if (updateDTO.getEmail() == null ||
                updateDTO.getEmail().trim().isEmpty()) {

            throw new BadRequestException(
                    "Email is required"
            );
        }

        user.setFname(
                updateDTO.getFname().trim()
        );

        user.setLname(
                updateDTO.getLname().trim()
        );

        user.setPhoneNo(
                updateDTO.getPhoneNo().trim()
        );

        user.setEmail(
                updateDTO.getEmail().trim()
        );

        UserDetails updatedUser =
                repo.save(user);

        return convertToProfileDTO(
                updatedUser
        );
    }

    // Returns all addresses belonging to one user.
    public List<UserAddress> getAddress(
            int id) {

        UserDetails user = repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        return user.getAddress();
    }

    // Adds a new address for one user.
    public UserAddress addAddress(
            int id,
            UserAddress address) {

        if (address == null) {
            throw new BadRequestException(
                    "Address details are required"
            );
        }

        UserDetails user = repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        if (address.getPincode() == null ||
                address.getPincode().getPin_code() == null ||
                address.getPincode()
                        .getPin_code()
                        .trim()
                        .isEmpty()) {

            throw new BadRequestException(
                    "Pincode is required"
            );
        }

        String pinCodeValue =
                address.getPincode()
                        .getPin_code()
                        .trim();

        Pincode pincode = pincodeRepo
                .findById(pinCodeValue)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Pincode not found"
                        ));

        address.setUser(user);
        address.setPincode(pincode);

        return addressRepo.save(address);
    }

    // Deletes an address using only the address ID.
    public void deleteAddress(
            int addressId) {

        UserAddress address = addressRepo
                .findById(addressId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found"
                        ));

        addressRepo.delete(address);
    }

    // Deletes an address only after checking user ownership.
    public void deleteAddress(
            int userId,
            int addressId) {

        UserAddress address = addressRepo
                .findById(addressId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found"
                        ));

        if (address.getUser().getUser_id()
                != userId) {

            throw new ConflictException(
                    "This address does not belong to the user"
            );
        }

        addressRepo.delete(address);
    }

    // Deletes one user account.
    public void deleteAccount(
            int id) {

        UserDetails user = repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        repo.delete(user);
    }

    // Converts UserDetails entity into ProfileResponseDTO.
    private ProfileResponseDTO convertToProfileDTO(
            UserDetails user) {

        return new ProfileResponseDTO(
                user.getUser_id(),
                user.getFname(),
                user.getLname(),
                user.getPhoneNo(),
                user.getEmail(),
                user.isTrustedUser()
        );
    }
}