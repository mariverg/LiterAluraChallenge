package com.aluracursos.literalura.modelos;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public record Data(
        @JsonAlias("results") List<DataBook> results) {
}