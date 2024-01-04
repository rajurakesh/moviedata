package com.example.movie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

import com.example.movie.model.Movie;
import com.example.movie.model.MovieRowMapper;
import com.example.movie.repository.MovieRepository;

// Write your code here
@Service
public class MovieH2Service implements MovieRepository {
    @Autowired
    private JdbcTemplate db;

    @Override
    public ArrayList<Movie> getMovies() {
        List<Movie> movies = db.query("select * from MOVIELIST", new MovieRowMapper());
        ArrayList<Movie> moviesList = new ArrayList<>(movies);
        return moviesList;
    }

    @Override
    public Movie addMovie(Movie movie) {
        db.update("insert into MOVIELIST (movieName,leadActor) values (?, ?)", movie.getMovieName(),
                movie.getLeadActor());
        return db.queryForObject("select * from MOVIELIST where movieName = ? and leadActor = ?", new MovieRowMapper(),
                movie.getMovieName(), movie.getLeadActor());
    }

    @Override
    public Movie getMovieById(int movieId) {
        try {
            Movie movie = db.queryForObject("select * from MOVIELIST where movieId = ?", new MovieRowMapper(), movieId);
            return movie;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Movie updateMovie(int movieId, Movie movie) {
        if (movie.getMovieName() != null) {
            db.update("update MOVIELIST set movieName = ? movieId = ?", movie.getMovieName(), movieId);
        }
        if (movie.getLeadActor() != null) {
            db.update("update MOVIELIST set leadActor = ? movieId = ?", movie.getLeadActor(), movieId);
        }
        return getMovieById(movieId);
    }

    @Override
    public void deleteMovie(int movieId) {
        db.update("delete from MOVIELIST where movieId = ?", movieId);
    }
}