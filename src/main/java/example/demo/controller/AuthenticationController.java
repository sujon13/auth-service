package example.demo.controller;

import example.demo.model.AuthenticationRequest;
import example.demo.service.auth.AuthenticationService;
import example.demo.service.auth.CookieService;
import example.demo.service.auth.PasswordService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthenticationController {
    private final PasswordService passwordService;
    private final AuthenticationService authenticationService;
    private final CookieService cookieService;

    @PostMapping("/authenticate")
    public ResponseEntity<Void> getAuthToken(@Valid @RequestBody AuthenticationRequest request) {
        final String jwtToken = authenticationService.createAuthenticationToken(request);
        final ResponseCookie authCookie = cookieService.buildAuthCookie(jwtToken);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, authCookie.toString())
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        final Cookie expiredAuthCookie = cookieService.clearAuthCookie(response);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, expiredAuthCookie.toString())
                .build();
    }

    @GetMapping("/password")
    public String getPassword(@RequestParam String password) {
        return passwordService.encode(password);
    }

}
