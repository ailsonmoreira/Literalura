package com.alura.literalura.service;

public interface IDataConversor {

    <T> T obterDados(String json, Class<T> classe);

}
