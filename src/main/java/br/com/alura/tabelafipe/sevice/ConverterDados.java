package br.com.alura.tabelafipe.sevice;

import java.util.List;

public interface ConverterDados {
    <T> T converter(String json, Class<T> clazz);

    <T> List<T> obterLista(String json, Class<T> clazz);
}
