package org.kvn.BookInTime.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.kvn.BookInTime.model.Users;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreationRequestDTO {

    @NotBlank(message = "User Name should not be blank")
    private String name;

    @NotBlank(message = "User Email should not be blank")
    @Email
    private String email;

    @NotBlank(message = "User PhoneNo should not be blank")
    private String phoneNo;

    private String address;

    @NotBlank(message = "User password should not be blank")
    private String password;

    public Users toUser() {
        return Users.builder()
                .name(name)
                .email(email)
                .phoneNo(phoneNo)
                .address(address)
                .build();
    }
}
