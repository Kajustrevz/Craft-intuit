package com.craft.craftapp.auth.dto;

import lombok.*;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class UserRequest {
    @NonNull
    private String username;
    @NonNull
    private String password;
}