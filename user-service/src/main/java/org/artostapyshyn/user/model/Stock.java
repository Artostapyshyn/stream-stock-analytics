package org.artostapyshyn.user.model;

import lombok.*;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock {

    @Id
    private String id;
    private String ticker;
    private String companyName;
    private int quantity;
    private Portfolio portfolio;
}
