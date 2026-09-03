package com.example.streamflix.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record WatchProgressRequest(
        @Min(0) int progressSeconds,
        @Min(1) Integer durationSeconds
) {
}
