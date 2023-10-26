package com.craft.craftapp.post;

import com.craft.craftapp.auth.User;
import com.craft.craftapp.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PostServiceTest {

    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        postService = new PostService(postRepository);
    }

    @Test
    public void testCreatePost() {
        User author = new User();
        PostDto postDto = new PostDto();
        postDto.setTitle("Test Title");
        postDto.setContent("Test Content");

        Post post = new Post();
        post.setAuthor(author);
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());

        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post createdPost = postService.createPost(author, postDto);

        assertNotNull(createdPost);
        assertEquals(post.getTitle(), createdPost.getTitle());
        assertEquals(post.getContent(), createdPost.getContent());
        assertEquals(post.getAuthor(), createdPost.getAuthor());

        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    public void testGetAllPosts() {
        List<Post> posts = new ArrayList<>();
        posts.add(new Post());
        posts.add(new Post());

        when(postRepository.findAll()).thenReturn(posts);

        List<Post> allPosts = postService.getAllPosts();

        assertNotNull(allPosts);
        assertEquals(posts.size(), allPosts.size());

        verify(postRepository, times(1)).findAll();
    }

    @Test
    public void testGetPost() throws ResourceNotFoundException {
        Long postId = 1L;
        Post post = new Post();
        post.setId(postId);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        Post foundPost = postService.getPost(postId);

        assertNotNull(foundPost);
        assertEquals(post.getId(), foundPost.getId());

        verify(postRepository, times(1)).findById(postId);
    }

    @Test
    public void testGetPostNotFound() {
        Long postId = 1L;

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> postService.getPost(postId));

        verify(postRepository, times(1)).findById(postId);
    }
}