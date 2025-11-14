package com.buccodev.banking_service.exceptions.shared;

import java.util.List;
import java.util.Map;

public record StandardError(String timeStamp, Integer status, String path, List<Map<String, String>> errors) {
}
