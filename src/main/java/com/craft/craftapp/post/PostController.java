package com.craft.craftapp.post;

import com.craft.craftapp.auth.User;
import com.craft.craftapp.common.dto.ApiResponse;
import com.craft.craftapp.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    @Autowired
    private final PostService postService;

    @PostMapping("")
    public Post createPost(Authentication auth, @RequestBody PostDto post) {
        User author = (User) auth.getPrincipal();
        return postService.createPost(author, post);
    }

    @GetMapping("")
    public List<Post> getAllPosts(){
        return postService.getAllPosts();
    }

    @GetMapping("{id}")
    public Post getPostById(@PathVariable Long id) throws ResourceNotFoundException {
        return postService.getPost(id);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deletePostById(@PathVariable Long id) throws ResourceNotFoundException {
        return postService.deletePost(id);
    }

}
