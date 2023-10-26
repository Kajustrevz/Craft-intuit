package com.craft.craftapp.comment;


import com.craft.craftapp.auth.User;
import com.craft.craftapp.post.Post;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIdentityReference(alwaysAsId = true) // Serialize parent as id
    private Comment parent;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Post post;

    @OneToOne
    private User author;

    private boolean isDeleted; // soft delete

    private String content;

    private int likeCount;

    private int dislikeCount;

    //json object{
    // reaction_type=
    // reaction_count=}

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp creationTimestamp;

    @UpdateTimestamp
    private Timestamp updationTimestamp;

}
