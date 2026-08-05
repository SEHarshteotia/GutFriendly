package com.gutfriendly.app.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gutfriendly.app.user.dto.HomePageDTO;
import com.gutfriendly.app.user.service.HomeService;


@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private HomeService homeService;

    // Returns homepage data for one user.
    @GetMapping("/user/{userId}")
    public ResponseEntity<HomePageDTO> getHomePage(
            @PathVariable int userId) {

        return ResponseEntity.ok(
                homeService.getHomePage(userId)
        );
    }
}