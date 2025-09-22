package org.artostapyshyn.user.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;


@Table(name = "portfolios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Portfolio {

    @Id
    private String id;
    private String name;
    private String description;
    private User user;
    @MappedCollection(idColumn = "portfolio_id")
    private List<Stock> stocks;
}
