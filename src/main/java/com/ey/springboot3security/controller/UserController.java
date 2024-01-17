package com.ey.springboot3security.controller;

import com.ey.springboot3security.entity.AuthRequest;
import com.ey.springboot3security.entity.Notes;
import com.ey.springboot3security.entity.UserInfo;
import com.ey.springboot3security.repository.UserInfoRepository;
import com.ey.springboot3security.service.JwtService;
import com.ey.springboot3security.service.UserInfoService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserInfoRepository repository;
    @Autowired
    private UserInfoService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/addNewUser")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        return service.addUser(userInfo);
    }


    @GetMapping("/user/getAllUsers")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<UserInfo> getAllUsers(){
        System.out.println( service.getAllUsers());
        return service.getAllUsers();

    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserInfo> getUserById(@PathVariable int id){
        Optional<UserInfo> user = repository.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @DeleteMapping("/user/{id}")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable int id){
        repository.deleteById(id);
        return  ResponseEntity.noContent().build();
    }

    @PutMapping("/user/{id}")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateUserById(@PathVariable int id, @RequestBody UserInfo userInfo) {
        Optional<UserInfo> existingUser = repository.findById(id);

        if (existingUser.isPresent()) {
            UserInfo existingUsers = existingUser.get();
            existingUsers.setName(userInfo.getName());
            existingUsers.setEmail(userInfo.getEmail());
            existingUsers.setRoles(userInfo.getRoles());


            UserInfo updatedUser = repository.save(existingUsers);
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private String getRoleFromAuthentication(Authentication authentication) {
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getAuthorities().stream().findFirst().orElseThrow().getAuthority();
        }
        return "ROLE_USER";
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

            if (authentication.isAuthenticated()) {
                String username = authRequest.getUsername();
                String token = jwtService.generateToken(username);
                String role = getRoleFromAuthentication(authentication);

                Map<String, String> response = new HashMap<>();
                response.put("username", username);
                response.put("role",role);
                response.put("token", token);

                return ResponseEntity.ok(response);
            } else {
                throw new UsernameNotFoundException("Invalid user credentials");
            }
        } catch (AuthenticationException e) {
            throw new UsernameNotFoundException("Invalid user credentials", e);
        }

    }


}
