package com.craft.craftapp.reaction;


import com.craft.craftapp.auth.User;
import com.craft.craftapp.common.dto.ApiResponse;
import com.craft.craftapp.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reaction")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;

    @PutMapping("/comment/{commentId}")
    public ResponseEntity<ApiResponse> createReaction(@PathVariable Long commentId, @RequestParam(defaultValue = "LIKE") String reaction, Authentication auth) throws ResourceNotFoundException {
        ReactionType reactionType = getReactionType(reaction);
        User user = (User) auth.getPrincipal();
        return reactionService.createUserReactionForComment(commentId, user.getId(), reactionType);
    }

    @GetMapping("/comment/{commentId}")
    public Slice<Reaction> getReactionUserList(@PathVariable Long commentId, @RequestParam(defaultValue = "LIKE") String reaction,  @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) throws ResourceNotFoundException {
        ReactionType reactionType = getReactionType(reaction);
        return reactionService.getUserForReactionOnComment(commentId, reactionType, page, size);
    }

    private ReactionType getReactionType(String reaction) {
        ReactionType reactionType;
        switch (reaction) {
            case "DISLIKE":
                reactionType = ReactionType.DISLIKE;
                break;
            case "ANGRY":
                reactionType = ReactionType.ANGRY;
                break;
            case "SAD":
                reactionType = ReactionType.SAD;
                break;
            case "LAUGH":
                reactionType = ReactionType.LAUGH;
                break;
            default:
                reactionType = ReactionType.LIKE;
                break;
        }
        return reactionType;
    }

}
