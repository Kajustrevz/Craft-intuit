package com.craft.craftapp.post;

import lombok.*;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class PostDto {
    @NonNull
    private String title;
    @NonNull
    private String content;
}
