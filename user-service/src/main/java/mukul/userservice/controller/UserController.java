package mukul.userservice.controller;

import lombok.RequiredArgsConstructor;
import mukul.userservice.dto.UserResponse;
import mukul.userservice.model.User;
import mukul.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String registerUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUser(@PathVariable("id") String id) {
        return userService.getUser(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public UserResponse updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }
}
