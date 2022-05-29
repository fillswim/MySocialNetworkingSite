package com.fillolej.mysns.domain.model.repositories;

import com.fillolej.mysns.domain.model.Image;
import com.fillolej.mysns.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByPostId(Long postId);

    Optional<Image> findByUser(User user);

}