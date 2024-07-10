package com.aluracursos.literalura.modelos;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String title;
    private String languages;
    private double downloadsAmount;

    @ManyToOne
    private Author author;

    public Book() {}

    public Book(DataBook dataBook, Author dataAuthor) {
        this.title = dataBook.title();
        this.author = dataAuthor;
        this.languages = dataBook.languages().get(0);
        this.downloadsAmount = dataBook.downloadsAmount();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public double getDownloadsAmount() {
        return downloadsAmount;
    }

    public void setDownloadsAmount(double downloadsAmount) {
        this.downloadsAmount = downloadsAmount;
    }

    @Override
    public String toString() {
        return """
                ------ LIBRO ------
                Titulo: %s
                Autor: %s
                Idioma: %s 
                Numero de descargas: %s
                """.formatted(title, author.getName(), languages, downloadsAmount);

    }
}

