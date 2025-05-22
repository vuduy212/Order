package com.example.Order.service;

import com.example.Order.entity.User;
import com.example.Order.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder; // Import PasswordEncoder
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Combined service class for user management and Spring Security's UserDetailsService.
 * This class handles business logic related to users and loads user details for authentication.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Inject PasswordEncoder

    /**
     * Constructor for UserManagementService.
     *
     * @param userRepository The repository for User entities.
     * @param passwordEncoder The password encoder for handling user passwords.
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Loads user-specific data by username for Spring Security.
     * This method is called during the authentication process.
     *
     * @param username The username to load.
     * @return A UserDetails object containing the user's information and authorities.
     * @throws UsernameNotFoundException if the user is not found.
     */
    @Transactional(readOnly = true) // Ensure transactional context for database access
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find the user by username in the database.
        // If not found, throw UsernameNotFoundException.
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Convert the Set<Role> from the User entity into a Collection<? extends GrantedAuthority>
        // which is required by Spring Security.
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())) // Role names should be prefixed with "ROLE_" (e.g., "ROLE_ADMIN")
                .collect(Collectors.toSet());

        // Return a Spring Security User object.
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(), // Password from DB (already encoded)
                user.getEnabled(), // enabled: true if the user account is active
                true, // accountNonExpired: true if the user's account has not expired
                true, // credentialsNonExpired: true if the user's credentials (password) have not expired
                true, // accountNonLocked: true if the user's account is not locked
                authorities // collection of authorities (roles) granted to the user
        );
    }

    /**
     * Retrieves all users from the database.
     *
     * @return A list of all users.
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        // When FetchType.EAGER is used on the roles relationship in the User entity,
        // roles will be loaded along with the users.
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return An Optional containing the user if found, or empty otherwise.
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Registers a new user with a default role (e.g., ROLE_CLIENT).
     * This is a basic example; you might want to add more validation and error handling.
     *
     * @param username The username for the new user.
     * @param rawPassword The raw (unencoded) password for the new user.
     * @param email The email for the new user.
     * @param defaultRole The default role to assign (e.g., "ROLE_CLIENT").
     * @return The newly created User entity.
     * @throws IllegalArgumentException if username or email already exists.
     */
    @Transactional
    public User registerNewUser(String username, String rawPassword, String email, String defaultRole) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already taken: " + username);
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered: " + email);
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(rawPassword)); // Encode password before saving
        newUser.setEmail(email);
        newUser.setEnabled(true); // New user is enabled by default

        // You would typically fetch the Role entity from the database here
        // For simplicity, let's assume you have a RoleRepository and can find the role by name
        // Role clientRole = roleRepository.findByName(defaultRole)
        //     .orElseThrow(() -> new RuntimeException("Default role not found: " + defaultRole));
        // newUser.addRole(clientRole);

        // For now, let's just save the user. You'll need to implement role assignment.
        // This part requires RoleRepository to fetch the actual Role object.
        // For demonstration, we'll omit direct role assignment here and assume it's handled elsewhere
        // or you'll add RoleRepository injection and logic.

        return userRepository.save(newUser);
    }

    // You can add other user management methods here, e.g., update user, delete user, assign roles.
}
