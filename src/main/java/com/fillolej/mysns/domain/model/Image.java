package com.fillolej.mysns.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "images")
public class Image extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @ToString.Exclude
    @Lob
    @Column(name = "image_bytes")
    private byte[] imageBytes;

    @ToString.Exclude
    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private User user;

    @ToString.Exclude
    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "post_id")
    private Post post;

}