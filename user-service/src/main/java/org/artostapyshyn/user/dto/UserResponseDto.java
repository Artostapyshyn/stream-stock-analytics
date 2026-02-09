package org.artostapyshyn.user.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private Long id;
    private String email;
    private String role;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String phone;
}

