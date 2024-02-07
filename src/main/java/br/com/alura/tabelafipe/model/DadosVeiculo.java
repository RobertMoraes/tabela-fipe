package br.com.alura.tabelafipe.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosVeiculo(@JsonAlias("Marca")  String marca, @JsonAlias("Modelo") String modelo, @JsonAlias("AnoModelo") Integer anoModelo,
                           @JsonAlias("Combustivel") String combustivel, @JsonAlias("Valor") String valor) {
}
