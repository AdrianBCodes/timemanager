package com.adrianbcodes.timemanager.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("{id}")
    ResponseEntity<?> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    ResponseEntity<?> addUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }

    //TODO
    @PutMapping("{id}")
    ResponseEntity<?> editUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}
