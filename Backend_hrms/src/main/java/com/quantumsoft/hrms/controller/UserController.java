package com.quantumsoft.hrms.controller;

import com.quantumsoft.hrms.dto.UpdateRoleInput;
import com.quantumsoft.hrms.dto.LoginDto;
import com.quantumsoft.hrms.entity.Admin;
import com.quantumsoft.hrms.entity.User;
import com.quantumsoft.hrms.enums.Action;
import com.quantumsoft.hrms.exception.ResourceNotFoundException;
import com.quantumsoft.hrms.repository.AdminRepository;
import com.quantumsoft.hrms.securityConfig.JwtService;
import com.quantumsoft.hrms.servicei.AuditLogServiceI;
import com.quantumsoft.hrms.servicei.UserServicei;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

//@CrossOrigin("*")
@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    @Autowired
    private UserServicei service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuditLogServiceI auditLogService;

    @Autowired
    private AdminRepository adminRepository;

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/register", consumes = "application/json", produces = "text/plain")
    public ResponseEntity<String> register(@Valid @RequestBody User user) {
        return new ResponseEntity<>(service.register(user), HttpStatus.CREATED);
    }

    /**
     * Login for Admin and Users (HR, EMPLOYEE, etc.)
     */
    @PostMapping(value = "/login", consumes = "application/json", produces = "text/plain")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        logger.info("Login API called for username: {}", loginDto.getUsername());

        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

            logger.info("Authenticating user: {}", loginDto.getUsername());

            Authentication authentication = authenticationManager.authenticate(authToken);

            logger.info("Authentication status: {}", authentication.isAuthenticated());

            if (!authentication.isAuthenticated()) {
                logger.warn("Authentication failed for: {}", loginDto.getUsername());
                return new ResponseEntity<>("User authentication failed..!", HttpStatus.UNAUTHORIZED);
            }

            String role = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse(null);

            logger.info("Role extracted: {}", role);

            // ADMIN flow
            if ("ROLE_ADMIN".equals(role)) {
                logger.info("Processing login for ADMIN: {}", loginDto.getUsername());

                Admin admin = adminRepository.findByUsername(authentication.getName())
                        .orElseThrow(() -> new ResourceNotFoundException("Admin record not found in database"));

                String jwtToken = jwtService.generateToken(admin.getUsername(), role);
                auditLogService.logInfo(admin.getAdminId(), Action.LOGIN, null);
                logger.info("Admin login success for {}", admin.getUsername());
                return new ResponseEntity<>(jwtToken, HttpStatus.OK);
            }

            logger.info("Processing login for non-admin user");

            // USER flow
            User user = service.findByUsername(loginDto.getUsername());

            if (user.isLoginFirstTime()) {
                logger.warn("First-time login block for: {}", loginDto.getUsername());
                return new ResponseEntity<>("You are trying to login first time, reset the password before login.", HttpStatus.NOT_ACCEPTABLE);
            } else if ("DEACTIVE".equalsIgnoreCase(user.getStatus().name())) {
                logger.warn("Inactive user login attempt: {}", loginDto.getUsername());
                return new ResponseEntity<>("INACTIVE users are not allowed to login.", HttpStatus.NOT_ACCEPTABLE);
            }

            String jwtToken = jwtService.generateToken(user.getUsername(), role);
            auditLogService.logInfo(user.getUserId(), Action.LOGIN, null);
            logger.info("User login successful for {}", user.getUsername());
            return new ResponseEntity<>(jwtToken, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Login failed due to exception: ", e);
            return new ResponseEntity<>("Login failed due to internal error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/forgotPwd")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        return new ResponseEntity<>(service.forgotPassword(email), HttpStatus.OK);
    }

    @PatchMapping("/resetPwd")
    public ResponseEntity<String> resetPassword(@RequestParam String email, @RequestParam String otp, @RequestParam String newPassword) {
        return new ResponseEntity<>(service.resetPassword(email, otp, newPassword), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @PatchMapping("/updateRole")
    public ResponseEntity<String> updateRole(@RequestBody UpdateRoleInput updateRoleInput) {
        return new ResponseEntity<>(service.updateRole(updateRoleInput), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/manageStatus/{userId}")
    public ResponseEntity<String> manageStatus(@PathVariable UUID userId, @RequestParam String status) {
        return new ResponseEntity<>(service.manageStatus(userId, status), HttpStatus.OK);
    }

    @GetMapping("/single/{userId}")
    public ResponseEntity<User> getUser(@PathVariable UUID userId) {
        return new ResponseEntity<>(service.getUser(userId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('HR') or hasRole('MANAGER') or hasRole('FINANCE')")
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(service.getUsers(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable String role) {
        List<User> usersByRole = service.getUsersByRole(role.toUpperCase());
        return new ResponseEntity<>(usersByRole, HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = service.findByUsername(username);
        SecurityContextHolder.clearContext();
        auditLogService.logInfo(user.getUserId(), Action.LOGOUT, null);

        logger.info("Logged out: {}", username);
        return new ResponseEntity<>("User logout successfully...!", HttpStatus.OK);
    }
}
