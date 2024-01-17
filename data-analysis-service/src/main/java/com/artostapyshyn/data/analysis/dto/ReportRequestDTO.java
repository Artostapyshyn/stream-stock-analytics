package com.artostapyshyn.data.analysis.dto;

import java.util.List;

public record ReportRequestDTO(String requestId, String format, List<String> indicators) {
}
