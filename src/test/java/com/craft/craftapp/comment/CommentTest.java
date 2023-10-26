package com.craft.craftapp.comment;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class CommentTest {

    @Test
    public void testGetId() {
        Long id = 1L;

        Comment comment = new Comment();
        comment.setId(id);

        assertEquals(id, comment.getId());
    }

    @Test
    public void testSetId() {
        Long id = 1L;

        Comment comment = new Comment();
        comment.setId(id);

        assertEquals(id, comment.getId());
    }

    @Test
    public void testGetContent() {
        String content = "This is a comment.";

        Comment comment = new Comment();
        comment.setContent(content);

        assertEquals(content, comment.getContent());
    }

    @Test
    public void testSetContent() {
        String content = "This is a comment.";

        Comment comment = new Comment();
        comment.setContent(content);

        assertEquals(content, comment.getContent());
    }

    @Test
    public void testGetCreationTimestamp() {
        Timestamp creationTimestamp = Timestamp.from(Instant.now());

        Comment comment = new Comment();
        comment.setCreationTimestamp(creationTimestamp);

        assertEquals(creationTimestamp, comment.getCreationTimestamp());
    }

    @Test
    public void testSetCreationTimestamp() {
        Timestamp creationTimestamp = Timestamp.from(Instant.now());

        Comment comment = new Comment();
        comment.setCreationTimestamp(creationTimestamp);

        assertEquals(creationTimestamp, comment.getCreationTimestamp());
    }

    @Test
    public void testGetUpdationTimestamp() {
        Timestamp updationTimestamp = Timestamp.from(Instant.now());

        Comment comment = new Comment();
        comment.setUpdationTimestamp(updationTimestamp);

        assertEquals(updationTimestamp, comment.getUpdationTimestamp());
    }

    @Test
    public void testSetUpdationTimestamp() {
        Timestamp updationTimestamp = Timestamp.from(Instant.now());

        Comment comment = new Comment();
        comment.setUpdationTimestamp(updationTimestamp);

        assertEquals(updationTimestamp, comment.getUpdationTimestamp());
    }
}