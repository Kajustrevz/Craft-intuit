package com.craft.craftapp.comment;

import com.craft.craftapp.post.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    //List<Comment> findByPostId(Long postId, Pageable pageable);

    //Slice<Comment> findByPostId(Long postId, Pageable pageable);

    Slice<Comment> findByPostIdAndParentId(Long postId, Long parentId, Pageable pageable);
    Slice<Comment> findByPostAndParent(Post post, Comment parent, Pageable pageable);
}
