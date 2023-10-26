package com.craft.craftapp.reaction;

import com.craft.craftapp.auth.User;
import com.craft.craftapp.auth.UserService;
import com.craft.craftapp.comment.Comment;
import com.craft.craftapp.comment.CommentService;
import com.craft.craftapp.common.dto.ApiResponse;
import com.craft.craftapp.common.dto.ApiStatus;
import com.craft.craftapp.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ReactionService {
    private final ReactionRepository reactionRepository;

    private final CommentService commentService;

    private final UserService userService;

    public ResponseEntity<ApiResponse> createUserReactionForComment(Long commentId, Long userId, ReactionType enumReactionType) throws ResourceNotFoundException {
        Comment comment = commentService.getComment(commentId);
        // if comment is deleted, don't allow reaction
        if(comment.isDeleted()){
            ApiResponse apiResponse = ApiResponse.builder().message("Reaction on deleted comment is not allowed").apiStatus(ApiStatus.ERROR).build();
            return ResponseEntity.ok(apiResponse);
        }
        User user = userService.getUser(userId);
        Reaction reaction = reactionRepository.findByCommentAndAuthor(comment, user);

        if(reaction != null) {
            if(reaction.getReactionType() != enumReactionType) {
                setUserReaction(reaction, comment, enumReactionType);
            }
        }else{
            createNewUserReaction(comment, user, enumReactionType);

        }
        ApiResponse apiResponse = ApiResponse.builder().message("Reaction is added successfully").apiStatus(ApiStatus.SUCCESS).build();
        return ResponseEntity.ok(apiResponse);

    }

    @Transactional
    private void createNewUserReaction(Comment comment, User user, ReactionType enumReactionType) {
        //add count for enumReactionType
        if(enumReactionType == ReactionType.LIKE) {
            comment.setLikeCount(comment.getLikeCount() + 1);
        }else if(enumReactionType == ReactionType.DISLIKE) {
            comment.setDislikeCount(comment.getDislikeCount() + 1);
        }
        commentService.saveComment(comment);
        Reaction newReaction = new Reaction();
        newReaction.setComment(comment);
        newReaction.setAuthor(user);
        newReaction.setReactionType(enumReactionType);
        reactionRepository.save(newReaction);
    }

    @Transactional
    private void setUserReaction(Reaction reaction, Comment comment, ReactionType enumReactionType ) {
        // remove count for reaction.getReactionType() and add count for enumReactionType
        //TODO use design patten here
        if(reaction.getReactionType() == ReactionType.LIKE) {
            comment.setLikeCount(comment.getLikeCount() - 1);
        }else if(reaction.getReactionType() == ReactionType.DISLIKE) {
            comment.setDislikeCount(comment.getDislikeCount() - 1);
        }

        if(enumReactionType == ReactionType.LIKE) {
            comment.setLikeCount(comment.getLikeCount() + 1);
        }else if(enumReactionType == ReactionType.DISLIKE) {
            comment.setDislikeCount(comment.getDislikeCount() + 1);
        }
        commentService.saveComment(comment);
        reaction.setReactionType(enumReactionType);
        reactionRepository.save(reaction);
    }

    public Slice<Reaction> getUserForReactionOnComment(Long commentId, ReactionType enumReactionType, Integer page, Integer size) throws ResourceNotFoundException {
        Pageable pageable = PageRequest.of(page, size);
        Comment comment = commentService.getComment(commentId);
        return reactionRepository.findAllByCommentAndReactionType(comment, enumReactionType, pageable);

    }


}
