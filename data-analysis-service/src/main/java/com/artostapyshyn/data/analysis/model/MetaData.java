package com.artostapyshyn.data.analysis.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MetaData {
    private String information;
    private String symbol;
    private String lastRefreshed;
    private String outputSize;
    private String timeZone;
}
