package com.craft.craftapp.post;


import com.craft.craftapp.auth.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    private boolean isDeleted; // soft delete
    @OneToOne
    @JsonIgnore
    private User author;

}
