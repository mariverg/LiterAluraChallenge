package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.modelos.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Author findByNameIgnoreCase(String name);


    @Query("SELECT a  FROM  Author a WHERE :year BETWEEN a.birthYear AND a.deathYear")
    List<Author> authorsLiveForYear(int year);



}