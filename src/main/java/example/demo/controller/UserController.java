package example.demo.controller;

import example.demo.model.UserResponse;
import example.demo.signup.model.User;
import example.demo.model.UserRequest;
import example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("")
    //@GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me() {
        return ResponseEntity.ok(userService.buildUserResponse());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id,
                                            @RequestParam(value = "user_name", required = false) String name,
                                            @RequestBody(required = false) UserRequest userRequest) {

        log.info("id: " + id + " name: " + name);
        log.info("user request: " + userRequest);

        return ResponseEntity.ok(userService.getUser(id));
    }

    @PostMapping("/{userName}")
    public ResponseEntity<User> updateUser(@PathVariable String userName,
                                           @RequestBody UserRequest userRequest) {

        if (userName == null) {
            log.error("bad request");
            return ResponseEntity.badRequest().build();
        }

        return userService.updateUser(userName, userRequest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public ResponseEntity<User> createUser(@RequestBody UserRequest userRequest) {
        return userService.saveUser(userRequest)
                .map(user -> new ResponseEntity<>(user, HttpStatus.CREATED))
                .orElse(ResponseEntity.internalServerError().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (id == null) {
            log.error("bad request");
            return ResponseEntity.badRequest().build();
        }

        try {
            userService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/verify")
    public void verify(@PathVariable("id") final int userId) {
        userService.makeUserVerified(userId);
    }
}
