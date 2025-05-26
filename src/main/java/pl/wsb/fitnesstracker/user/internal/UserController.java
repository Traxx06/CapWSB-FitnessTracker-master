package pl.wsb.fitnesstracker.user.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import pl.wsb.fitnesstracker.user.api.User;
import pl.wsb.fitnesstracker.user.api.UserNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
class UserController {

    private final UserServiceImpl userService;

    private final UserMapper userMapper;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.findAllUsers()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @PostMapping
    public UserDto addUser(@RequestBody UserDto userDto) throws InterruptedException {

        // TODO: Implement the method to add a new user.
        //  You can use the @RequestBody annotation to map the request body to the UserDto object.


        return null;
    }

    @GetMapping("simple")
    public List<UserDto> getallusears() {
        return userService.findAllUsers().stream()
                .map(user -> userMapper.toDto(user))
                .toList();
    }

    @GetMapping("{id}")
    public User getUserById(@PathVariable long id) {
        return userService.getUser(id).orElseThrow(() -> new UserNotFoundException(id));
    }
    @GetMapping("/email")
    public List<UserDto> getUserByEmail(@RequestParam("email") String email) {
        return userService.getUserByEmail(email).stream().map(userMapper::toDto).toList();
    }

    @GetMapping("/older/{date}")
    public Set<UserDto> getAllUsersOlderThan(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return userService.getUsersOlderThan(date).stream()
                .map(userMapper::toDto)
                .collect(Collectors.toSet());
    }


    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUserById(userId);
    }

}