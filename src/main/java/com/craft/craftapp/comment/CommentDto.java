package com.craft.craftapp.comment;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class CommentDto {
    private Long parentId;
    private long postId;
    private String content;
}
