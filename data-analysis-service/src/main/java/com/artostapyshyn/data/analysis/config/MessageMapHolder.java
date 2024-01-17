package com.artostapyshyn.data.analysis.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Getter
@Component
public class MessageMapHolder {
    private final ConcurrentHashMap<String, String> stockDataMap = new ConcurrentHashMap<>();

}
