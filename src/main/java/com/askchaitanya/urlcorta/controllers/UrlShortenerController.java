package com.askchaitanya.urlcorta.controllers;

import com.askchaitanya.urlcorta.dto.RequestDTO;
import com.askchaitanya.urlcorta.dto.ResponseDTO;
import com.askchaitanya.urlcorta.services.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/app")
public class UrlShortenerController {

    @Autowired
    UrlShortenerService urlShortenerService;

    @Value("${corta.default.redirection_url}")
    private String defaultRedirectionUrl;

    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> shortenUrl(@RequestBody RequestDTO requestDTO) {
        // implement exception handling
        // add sanitise annotation for originalUrl and alias in requestDTO
        // partition originalUrl in requestDTO into different attributes of an url class
        Optional<ResponseDTO> responseDTOOptional = urlShortenerService.shortenUrl(requestDTO);
        return responseDTOOptional
                .map(responseDTO -> new ResponseEntity<>(responseDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        // stress test the shortenUrl method with all the corner cases and handle exceptions if any
    }

    //  For Reference: https://www.baeldung.com/spring-redirect-and-forward
    @GetMapping("/{customAlias}")
    ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String customAlias) {
        // implement cache here
        Optional<String> originalUrl = urlShortenerService.getOriginalUrl(customAlias);
        String redirectionUrl = originalUrl.orElse(defaultRedirectionUrl);
        // explore all other redirection strategies
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(redirectionUrl))
                .build();
        // stress test the redirection with all the corner cases and handle exceptions if any
    }

    @GetMapping
    ResponseEntity<Void> redirectToDefaultRedirectionUrl() {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(defaultRedirectionUrl))
                .build();
    }

}
