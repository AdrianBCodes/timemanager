package com.adrianbcodes.timemanager.user;

import com.adrianbcodes.timemanager.dto.UserDTO;
import org.springframework.data.domain.Page;
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
    ResponseEntity<Page<UserDTO>> getAllUsersPagedAndFiltered(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String surname,
            @RequestParam(defaultValue = "") String email,
            @RequestParam(defaultValue = "") Long projectId,
            @RequestParam(defaultValue = "id,asc") String sort
    ) {

        Page<UserDTO> foundUsers = userService.getAllUsersPagedAndFiltered(name, surname, email, projectId, page, size, sort)
                .map(User::convertToUserDTO);
        return ResponseEntity.ok(foundUsers);
    }

    @GetMapping("{id}")
    ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id).convertToUserDTO());
    }

    @PostMapping
    ResponseEntity<UserDTO> addUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.saveUser(user).convertToUserDTO());
    }

    @PutMapping("{id}")
    ResponseEntity<Long> editUser(@PathVariable Long id, @RequestBody User user) {
        User toEdit = userService.getUserById(id);
        toEdit.setName(user.getName());
        toEdit.setSurname(user.getSurname());
        toEdit.setEmail(user.getEmail());

        userService.saveUser(toEdit);
        return ResponseEntity.ok(toEdit.getId());
    }

    @DeleteMapping("{id}")
    ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}
