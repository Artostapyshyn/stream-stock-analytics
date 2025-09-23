package org.artostapyshyn.user.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    private Long id;
    private String email;
    private String password;
    private String role;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String phone;
}
