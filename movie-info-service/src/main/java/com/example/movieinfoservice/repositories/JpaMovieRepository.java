package com.example.movieinfoservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.movieinfoservice.models.JpaMovie;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface JpaMovieRepository extends JpaRepository<JpaMovie, String> {
}
