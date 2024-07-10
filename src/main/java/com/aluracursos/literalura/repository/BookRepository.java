package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.modelos.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Book findByTitleContainingIgnoreCase(String title);

    @Query("SELECT b FROM Book b WHERE b.languages = :languages")
    List<Book> findByLanguages(String languages);

    @Query("SELECT b FROM Book b ORDER BY b.downloadsAmount DESC LIMIT 10")
    List<Book> topFiveDownloads();


}


