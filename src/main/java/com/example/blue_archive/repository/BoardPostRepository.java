package com.example.blue_archive.repository;

import com.example.blue_archive.model.BoardPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardPostRepository extends JpaRepository<BoardPost, Long> {
}
