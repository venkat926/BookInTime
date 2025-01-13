package org.kvn.BookInTime.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreationResponseDTO {

    private String name;

    private String email;

    private String phoneNo;

    private String address;

}
