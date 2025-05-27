package com.example.blue_archive.repository;

import com.example.blue_archive.model.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {
    List<BoardComment> findByPostId(Long postId);
}
