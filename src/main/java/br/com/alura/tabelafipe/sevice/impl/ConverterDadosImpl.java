package br.com.alura.tabelafipe.sevice.impl;

import br.com.alura.tabelafipe.sevice.ConverterDados;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.util.List;

public class ConverterDadosImpl implements ConverterDados {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public <T> T converter(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> List<T> obterLista(String json, Class<T> classe) {
        try {
            CollectionType lista = objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, classe);
            return objectMapper.readValue(json, lista);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
