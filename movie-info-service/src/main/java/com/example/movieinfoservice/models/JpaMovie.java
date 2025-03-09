package com.example.movieinfoservice.models;

import javax.persistence.*;



@Entity
@Table(name="movies")
public class JpaMovie {

    @Id
    private String id;
    private String name;
    private String description;

    public JpaMovie() {
    }

    public JpaMovie(String movieId, String name, String description) {
        this.id = movieId;
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMovieId() {
        return id;
    }

    public void setMovieId(String movieId) {
        this.id = movieId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
