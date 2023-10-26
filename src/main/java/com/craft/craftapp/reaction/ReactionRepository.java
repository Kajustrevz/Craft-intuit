package com.craft.craftapp.reaction;


import com.craft.craftapp.auth.User;
import com.craft.craftapp.comment.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    Slice<Reaction> findAllByCommentAndReactionType(Comment comment, ReactionType enumReactionType, Pageable pageable);

    Reaction findByCommentAndAuthor(Comment comment, User user);

   // Slice<Reaction> findAllByCommentAndUserAndReactionType(Comment comment, User user, EnumReactionType enumReactionType, Pageable pageable);
}
