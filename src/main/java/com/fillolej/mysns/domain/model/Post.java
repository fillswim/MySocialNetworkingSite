package com.fillolej.mysns.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "posts")
public class Post extends BaseEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "caption", nullable = false)
    private String caption;

    @Column(name = "location")
    private String location;

    @Column(name = "likes")
    private Integer likes;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "liked_user")
    @CollectionTable(
            name = "post_liked_users",
            joinColumns = @JoinColumn(name = "post_id"))
    private Set<String> likedUsers = new LinkedHashSet<>();

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ToString.Exclude
    @OneToMany(
//            fetch = FetchType.EAGER,
            mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @ToString.Exclude
    @OneToOne(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Image image;

}