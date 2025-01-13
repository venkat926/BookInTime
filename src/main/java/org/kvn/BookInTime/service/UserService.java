package org.kvn.BookInTime.service;

import org.kvn.BookInTime.dto.request.UserCreationRequestDTO;
import org.kvn.BookInTime.dto.response.UserCreationResponseDTO;
import org.kvn.BookInTime.enums.UserStatus;
import org.kvn.BookInTime.enums.UserType;
import org.kvn.BookInTime.model.Users;
import org.kvn.BookInTime.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;

@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Value("${user.authority}")
    private String userAuthority;

    @Value("${admin.authority}")
    private String adminAuthority;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByEmail(username);
    }

    public UserCreationResponseDTO addUser(UserCreationRequestDTO requestDTO) {
        // build user object
        Users user = requestDTO.toUser();
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        user.setUserType(UserType.USER);
        user.setUserStatus(UserStatus.ACTIVE);
        user.setAuthorities(userAuthority);

        // save user to DB
        return saveUserOrAdmin(user);
    }

    public UserCreationResponseDTO addAdmin(UserCreationRequestDTO requestDTO) {
        // build admin(user) object
        Users admin = requestDTO.toUser();
        admin.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        admin.setUserType(UserType.ADMIN);
        admin.setUserStatus(UserStatus.ACTIVE);
        admin.setAuthorities(adminAuthority);

        // save admin to DB
        return saveUserOrAdmin(admin);
    }

    // common for saving user or admin to DB
    private UserCreationResponseDTO saveUserOrAdmin(Users user) {
        // handle exception
        try {
            user = userRepo.save(user);
        } catch (DataIntegrityViolationException e) {
            logger.error(e.getMessage());

            String message = e.getMessage();
            String errorMessage = "email or phoneNo already exists [" +
                    message.substring(message.indexOf("Duplicate"), message.lastIndexOf("for")).trim() + "]";
            throw new DataIntegrityViolationException(errorMessage);
        }

        // return response
        if (user == null) return null;
        return getUserCreationResponseDTO(user);
    }

    // to build userCreationResponseDTO
    private UserCreationResponseDTO getUserCreationResponseDTO(Users user) {
        return UserCreationResponseDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .phoneNo(user.getPhoneNo())
                .address(user.getAddress())
                .build();
    }
}
