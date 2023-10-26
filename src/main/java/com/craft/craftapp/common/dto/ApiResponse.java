package com.craft.craftapp.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse {
    private String message;
    private ApiStatus apiStatus;
}
