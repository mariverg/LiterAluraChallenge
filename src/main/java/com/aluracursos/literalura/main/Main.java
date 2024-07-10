package com.aluracursos.literalura.main;


import com.aluracursos.literalura.modelos.*;
import com.aluracursos.literalura.repository.AuthorRepository;
import com.aluracursos.literalura.repository.BookRepository;
import com.aluracursos.literalura.service.ConvertData;
import com.aluracursos.literalura.service.FetchAPI;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private Scanner keyboard = new Scanner(System.in);
    private FetchAPI fetchAPI = new FetchAPI();
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvertData convertData = new ConvertData();
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private List<Author> authors;
    private List<Book> books;

    public Main(BookRepository repository, AuthorRepository authorRepository) {
        this.bookRepository = repository;
        this.authorRepository = authorRepository;
    }

    public void showMenu() {
        int option = -1;
        while (option != 0) {
            String menu = """
                    1 - Buscar libro por titulo
                    2 - Libros registrados
                    3 - Autores registrados
                    4 - Autores registrados vivos en un determinado año
                    5 - libros por idioma
                    6 - Top 10 libros mas descargados
                    7 - Buscar Autores por nombre
                                        
                    0 - Salir
                    """;
            System.out.println("------------");
            System.out.println("Elija una opcion");
            System.out.println(menu);
            option = keyboard.nextInt();
            keyboard.nextLine();

            switch (option) {
                case 1:
                    getBookByTitle();
                    break;
                case 2:
                    showRegisteredBooks();
                    break;
                case 3:
                    showRegisteredAuthors();
                    break;
                case 4:
                    showLivesAuthorsByYear();
                    break;

                case 5:
                    showBooksByLanguages();
                    break;
                case 6:
                    findTop10BooksMoreDownloads();
                    break;
                case 7:
                    getAuthor();
                    break;

                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }

    private Data searchBook() {
        System.out.println("Ingrese el nombre del libro que quiere buscar: ");
        String title = keyboard.nextLine();
        String json = fetchAPI.getData(URL_BASE + title.toLowerCase().replace(" ", "+"));
        return convertData.getData(json, Data.class);

    }

    private void getBookByTitle() {
        Data data = searchBook();
        if (!data.results().isEmpty()) {
            DataBook dataBook = data.results().get(0);
            DataAuthor dataAuthor = dataBook.author().get(0);
            System.out.println("""
                    El libro encontrado y almacenado fue: 
                    Nombre: %s
                    Author: %s
                    Idioma: %s
                    """.formatted(dataBook.title(), dataAuthor.name(), dataBook.languages()));
            Book searchedBook = bookRepository.findByTitleContainingIgnoreCase(dataBook.title());

            if (searchedBook != null) System.out.println("El libro ya existe");
            else {
                Author searchedAuthor = authorRepository.findByNameIgnoreCase(dataAuthor.name());
                if (searchedAuthor == null) {
                    Author author = new Author(dataAuthor);
                    authorRepository.save(author);
                    Book book = new Book(dataBook, author);
                    bookRepository.save(book);
                } else {
                    Book book = new Book(dataBook, searchedAuthor);
                    bookRepository.save(book);
                }
            }
        } else System.out.println("Libro no encontrado");

    }

    private void showRegisteredBooks() {
        try {
            books = bookRepository.findAll();
        } catch (InputMismatchException e) {
            System.out.println(e.getMessage());
        }
        if (books.isEmpty()) System.out.println("No hay libros registrados");
        books.forEach(System.out::println);
    }

    private void showRegisteredAuthors() {
        authors = authorRepository.findAll();
        if (authors.isEmpty()) System.out.println("No hay autores registrados");
        authors.forEach(System.out::println);
    }

    private void showLivesAuthorsByYear() {
        try {
            System.out.println("Ingrese el año de nacimiento del autor que quiere buscar: ");
            int year = keyboard.nextInt();
            authors = authorRepository.authorsLiveForYear(year);
        } catch (InputMismatchException e) {
            System.out.println(e.getMessage());
        }
        if (authors.isEmpty()) System.out.println("No hay autores registrados");
        authors.forEach(System.out::println);

    }

    private void showBooksByLanguages() {
        List<String> languages = new ArrayList<>();
        languages.add("es");
        languages.add("en");
        languages.add("it");
        languages.add("hu");
        languages.add("tl");

        System.out.println("""
                Escriba el idioma en el que desea hacer la busqueda
                Los idiomas aceptados son: [Es, En It, Hu, Tl]
                """);
        String keyword = keyboard.nextLine();
        if (!languages.contains(keyword.toLowerCase())) System.out.println("Idioma incorrecto");
        bookRepository.findByLanguages(keyword).forEach(System.out::println);

    }

    private void findTop10BooksMoreDownloads() {
        List<Book> books = bookRepository.topFiveDownloads();
        books.forEach(System.out::println);

    }

    private void getAuthor() {
        System.out.println("Ingrese el nombre del Autor: ");
        try {
            String authorName = keyboard.nextLine();
            Author author = authorRepository.findByNameIgnoreCase(authorName);
            System.out.println(author);
        } catch (InputMismatchException e) {
            System.out.println(e.getMessage());
        }


    }
}
