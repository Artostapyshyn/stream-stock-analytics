package org.artostapyshyn.user.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String phone;
}

