package com.spring.boot.security.saml.controllers;

import com.spring.boot.security.saml.stereotypes.CurrentUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class LandingController {

    @GetMapping("/landing")
    public String landing(@CurrentUser User user, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            log.debug("Current authentication instance from security context is null");
        }
        else {
            log.debug("Current authentication instance from security context: {}", this.getClass().getSimpleName());
        }
        model.addAttribute("username", user.getUsername());
        return "pages/landing";
    }

}
