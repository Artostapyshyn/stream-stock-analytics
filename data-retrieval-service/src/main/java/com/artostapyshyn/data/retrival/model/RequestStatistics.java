package com.artostapyshyn.data.retrival.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "request_statistics")
@AllArgsConstructor
@NoArgsConstructor
public class RequestStatistics {
    @Id
    private ObjectId id;
    private String requestType;
    private LocalDateTime timestamp;
    private long responseTime;
}
