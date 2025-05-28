package pl.wsb.fitnesstracker.user.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wsb.fitnesstracker.user.api.User;
import pl.wsb.fitnesstracker.user.api.UserNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/users")
@CrossOrigin
@RequiredArgsConstructor
 public class UserController {

    private final UserServiceImpl userService;

    private final UserMapper userMapper;

    /**
     * Retrieves all users.
     *
     * @return list of all users as {@link UserDto}
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.findAllUsers()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    /**
     * Creates a new user.
     *
     * @param userDto the user data to create
     * @return created user as {@link UserDto}
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto addUser(@RequestBody UserDto userDto) {
        User userToCreate = userMapper.toEntity(userDto);
        User createdUser = userService.createUser(userToCreate);
        return userMapper.toDto(createdUser);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id user ID
     * @return user as {@link UserDto}
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable long id) {
        User user = userService.getUser(id).orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toDto(user);
    }

    /**
     * Retrieves users by email.
     *
     * @param email the email to search
     * @return list of matching {@link UserDto}
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/email")
    public List<UserDto> getUserByEmail(@RequestParam("email") String email) {
        return userService.getUserByEmail(email)
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    /**
     * Updates user by ID.
     *
     * @param id user ID
     * @param userDto new user data
     * @return updated user as {@link UserDto}
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        User updatedData = userMapper.toEntity(userDto);
        User updatedUser = userService.updateUser(id, updatedData);
        return ResponseEntity.ok(userMapper.toDto(updatedUser));
    }

    /**
     * Deletes user by ID.
     *
     * @param userId the ID of user to delete
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }

    /**
     * Retrieves users older than specified age.
     *
     * @param age minimum age
     * @return list of {@link UserDto}
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/olderthan/{age}")
    public List<UserDto> getUsersOlderThan(@PathVariable int age) {
        return userService.findUsersOlderThan(age)
                .stream()
                .map(userMapper::toDto)
                .toList();
    }
}