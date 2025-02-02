package com.spring.boot.security.saml.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.saml.metadata.MetadataManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@Slf4j
@Controller
@RequestMapping("/saml")
@RequiredArgsConstructor
public class SSOController {

    private final MetadataManager metadata;

    @GetMapping("/discovery")
    public String idpSelection(HttpServletRequest request, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            log.debug("Current authentication instance from security context is null");
        }
        else {
            log.debug("Current authentication instance from security context: {}", this.getClass().getSimpleName());
        }
        if (auth == null || (auth instanceof AnonymousAuthenticationToken)) {
            Set<String> idps = metadata.getIDPEntityNames();
            for (String idp : idps) {
                log.info("Configured Identity Provider for SSO: {}", idp);
            }
            model.addAttribute("idps", idps);
            return "pages/discovery";
        } else {
            log.warn("The current user is already logged.");
            return "redirect:/landing";
        }
    }

}
