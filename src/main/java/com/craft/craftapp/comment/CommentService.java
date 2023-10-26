package com.craft.craftapp.comment;

import com.craft.craftapp.auth.User;
import com.craft.craftapp.common.dto.ApiResponse;
import com.craft.craftapp.common.dto.ApiStatus;
import com.craft.craftapp.exception.ResourceNotFoundException;
import com.craft.craftapp.exception.UserNotAuthorizedForOperationException;
import com.craft.craftapp.post.Post;
import com.craft.craftapp.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;;
    private final PostService postService;

    public Comment createComment(CommentDto request, User author) throws ResourceNotFoundException {
        Post post = postService.getPost(request.getPostId());
        Comment parent = null;
        if(request.getParentId() != null) {
            parent = getComment(request.getParentId());
        }

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setAuthor(author);
        comment.setPost(post);
        comment.setParent(parent);
        comment.setLikeCount(0);
        comment.setDislikeCount(0);
        return commentRepository.save(comment);
    }

    public Comment getComment(Long id) throws ResourceNotFoundException {
        return commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("Comment with id %d not found", id)));
    }

//    public List<Comment> listComments(Long postId, Integer page, Integer size) {
//        Pageable pageable = PageRequest.of(page, size);
//        return commentRepository.findByPostId(postId, pageable);
//    }

    public Slice<Comment> listCommentsSlice(Long postId, Long parentId, Integer page, Integer size) throws ResourceNotFoundException {
        Pageable pageable = PageRequest.of(page, size);
        Post post = postService.getPost(postId);
        Comment parent = null;
        if(parentId!=-1){
            parent = getComment(parentId);
        }
        return commentRepository.findByPostAndParent(post, parent, pageable);
    }

    public ResponseEntity<ApiResponse> deleteComment(Long id, User author) throws ResourceNotFoundException, UserNotAuthorizedForOperationException {
        Comment comment = getComment(id);
        if(!comment.getAuthor().getId().equals(author.getId())){
            throw new UserNotAuthorizedForOperationException(String.format("User is not authorize to delete the Comment with id %d ", id));
        }
        comment.setDeleted(true);
        comment.setDislikeCount(0);
        comment.setLikeCount(0);
        comment.setContent("This comment has been deleted");  //comment.setContent("");
        commentRepository.save(comment);
        ApiResponse apiResponse = ApiResponse.builder().message("Comment deletion is successful").apiStatus(ApiStatus.SUCCESS).build();
        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<ApiResponse> updateComment(Long id, String updatedContent, User author) throws ResourceNotFoundException, UserNotAuthorizedForOperationException {
        Comment comment = getComment(id);
        if(!comment.getAuthor().getId().equals(author.getId())){
            throw new UserNotAuthorizedForOperationException(String.format("User is not authorize to update the Comment with id %d ", id));
        }
        if(comment.isDeleted()){
            ApiResponse apiResponse = ApiResponse.builder().message(String.format("Comment with id %d does not exist", id)).apiStatus(ApiStatus.ERROR).build();
            //return ResponseEntity.ok(apiResponse);
            throw new ResourceNotFoundException(String.format("Comment with id %d does not exist", id));
        }
        comment.setContent(updatedContent);
        commentRepository.save(comment);
        ApiResponse apiResponse = ApiResponse.builder().message("Comment update is successful").apiStatus(ApiStatus.SUCCESS).build();
        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<ApiResponse> saveComment(Comment comment) {
        commentRepository.save(comment);
        ApiResponse apiResponse = ApiResponse.builder().message("Comment update is successful").apiStatus(ApiStatus.SUCCESS).build();
        return ResponseEntity.ok(apiResponse);
    }


}
