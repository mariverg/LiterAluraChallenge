package com.aluracursos.literalura.service;

public interface IConverData {
    <T> T getData(String json, Class<T> clase);

}