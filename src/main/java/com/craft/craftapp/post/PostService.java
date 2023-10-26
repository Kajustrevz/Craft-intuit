package com.craft.craftapp.post;

import com.craft.craftapp.auth.User;
import com.craft.craftapp.common.dto.ApiResponse;
import com.craft.craftapp.common.dto.ApiStatus;
import com.craft.craftapp.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;

    public Post createPost(User author, PostDto post) {
        Post newPost = new Post();
        newPost.setAuthor(author);
        newPost.setTitle(post.getTitle());
        newPost.setContent(post.getContent());
        return postRepository.save(newPost);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPost(Long id) throws ResourceNotFoundException {
        Optional<Post> post = postRepository.findById(id);
        if(post.isEmpty()){
            throw new ResourceNotFoundException(String.format("No post found with id=%s", id));
        }
        return post.get();
    }

    public ResponseEntity<ApiResponse> deletePost(Long id) throws ResourceNotFoundException {
        Post post = getPost(id);
        post.setDeleted(true);
        post.setContent("This post has been deleted");
        postRepository.save(post);
        ApiResponse apiResponse = ApiResponse.builder().message("Post deletion is successful").apiStatus(ApiStatus.SUCCESS).build();
        log.debug("Post deletion is successful");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    }
