package com.fillolej.mysns.repositories;

import com.fillolej.mysns.models.Image;
import com.fillolej.mysns.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByPostId(Long postId);

    Optional<Image> findByUser(User user);

}