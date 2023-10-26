package com.craft.craftapp.reaction;

import com.craft.craftapp.auth.User;
import com.craft.craftapp.comment.Comment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User author;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private Comment comment;

    private ReactionType reactionType;
}

