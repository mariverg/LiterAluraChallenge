package com.aluracursos.literalura.modelos;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public record DataBook(
        @JsonAlias("title") String title,
        @JsonAlias("authors") List<DataAuthor> author,
        @JsonAlias("languages") List<String> languages,
        @JsonAlias("download_count") double downloadsAmount) {
}

