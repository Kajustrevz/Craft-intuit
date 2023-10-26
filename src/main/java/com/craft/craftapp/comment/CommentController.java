package com.craft.craftapp.comment;

import com.craft.craftapp.auth.User;
import com.craft.craftapp.common.dto.ApiResponse;
import com.craft.craftapp.exception.ResourceNotFoundException;
import com.craft.craftapp.exception.UserNotAuthorizedForOperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("")
    public Comment createComment(@RequestBody CommentDto comment, Authentication auth) throws ResourceNotFoundException {
        User author = (User) auth.getPrincipal();
        return commentService.createComment(comment, author);
    }

    @GetMapping("/{id}")
    public Comment getComment(@PathVariable Long id) throws ResourceNotFoundException {
        return commentService.getComment(id);
    }

    @GetMapping("/post/{postId}/parent/{parentId}")
    public Slice<Comment> listCommentSlice(@PathVariable Long postId, @PathVariable Long parentId, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "2") Integer size) throws ResourceNotFoundException {
        return commentService.listCommentsSlice(postId, parentId, page, size);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteCommentById(@PathVariable Long id, Authentication auth) throws ResourceNotFoundException, UserNotAuthorizedForOperationException {
        User author = (User) auth.getPrincipal();
        return commentService.deleteComment(id, author);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateCommentById(@PathVariable Long id, @RequestBody CommentDto commentDto, Authentication auth) throws ResourceNotFoundException, UserNotAuthorizedForOperationException {
        User author = (User) auth.getPrincipal();
        return commentService.updateComment(id, commentDto.getContent(), author);
    }

}
