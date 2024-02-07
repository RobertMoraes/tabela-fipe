package br.com.alura.tabelafipe.principal;

import br.com.alura.tabelafipe.model.Dados;
import br.com.alura.tabelafipe.model.DadosVeiculo;
import br.com.alura.tabelafipe.model.Modelos;
import br.com.alura.tabelafipe.sevice.impl.ConsumerApiImpl;
import br.com.alura.tabelafipe.sevice.impl.ConverterDadosImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    Scanner leitor = new Scanner(System.in);
    private final ConsumerApiImpl cosumerApiImpl = new ConsumerApiImpl();
    private final ConverterDadosImpl converterDados = new ConverterDadosImpl();

    private final String URL_API = "https://parallelum.com.br/fipe/api/v1/";

    public void buscaVeiculos() {
        System.out.println("Escolhha o tipo de veiculo: ");
        System.out.println("1 - Carros\n2 - Motos\n3 - Caminhões\n4 - Sair\n");
        var tipoSelecionado = leitor.nextLine();
        if (tipoSelecionado.equalsIgnoreCase("1")) {
            var tipo = "Carros";
            System.out.println("Tipo de veiculo selecionado - Carros");
            buscarMarcas(tipo.toLowerCase());
        } else if (tipoSelecionado.equalsIgnoreCase("2")) {
            var tipo = "Motos";
            System.out.println("Tipo de veiculo selecionado - Motos");
            buscarMarcas(tipo.toLowerCase());
        } else if (tipoSelecionado.equalsIgnoreCase("3")) {
            var tipo = "Caminhoes";
            System.out.println("Tipo de veiculo selecionado - Caminhões");
            buscarMarcas(tipo.toLowerCase());
        } else if (tipoSelecionado.equalsIgnoreCase("4")) {
            System.out.println("Programa encerrado");
            System.exit(0);
        } else {
            System.out.println("Opção inválida, tente novamente");
            buscaVeiculos();
        }


    }

    private void buscarMarcas(String tipo) {
        tipo.replaceAll(" ", "+");
        var json = cosumerApiImpl.obterDados(URL_API + tipo + "/marcas");
        System.out.println("\n::::::::::::::::::::::::::::::Retorno JSON::::::::::::::::::::::::::::::\n");
        System.out.println(json);
        var marcas = converterDados.obterLista(json, Dados.class);
        System.out.println("\n::::::::::::::::::::::::::::::Retorno MARCAS DO TIPO DE VEICULO " + tipo.toUpperCase() + "::::::::::::::::::::::::::::::\n");
        marcas.forEach(System.out::println);
        System.out.println("\nInforme o código da marca:");
        var marcaSelecionada = leitor.nextLine();
        String nmMarca = marcas.stream().filter(m -> m.codigo().equals(marcaSelecionada)).findAny().orElse(null).nome();
        buscarModelos(marcaSelecionada, tipo, nmMarca);

    }

    private void buscarModelos(String marcaSelecionada, String tipo, String nmMarca) {
        var json = cosumerApiImpl.obterDados(URL_API + tipo + "/marcas/" + marcaSelecionada + "/modelos");
        System.out.println("\n::::::::::::::::::::::::::::::Retorno JSON::::::::::::::::::::::::::::::\n");
        System.out.println(json);
        var lsModelo = converterDados.converter(json, Modelos.class);

        System.out.println("\n::::::::::::::::::::::::::::::Retorno MODELOS DA MARCA " + nmMarca.toUpperCase() + "::::::::::::::::::::::::::::::\n");
        lsModelo.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);
        System.out.println("\nInforme o nome do modelo para filtrar:");
        var modeloSelecionado = leitor.nextLine();
        List<Dados> modelosFiltrados = lsModelo.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(modeloSelecionado.toLowerCase()))
                .collect(Collectors.toList());
        System.out.println("\n::::::::::::::::::::::::::::::Retorno MODELOS FILTRADOS::::::::::::::::::::::::::::::\n");
        modelosFiltrados.forEach(System.out::println);
        System.out.println("\nInforme o código do modelo:");
        var cdModelo = leitor.nextLine();
        String nmModelo = lsModelo.modelos().stream().filter(m -> m.codigo().equals(cdModelo)).findAny().orElse(null).nome();
        buscarAnos(cdModelo, tipo, marcaSelecionada, nmModelo);
    }

    private void buscarAnos(String cdModelo, String tipo, String marcaSelecionada, String nmModelo) {
        var json = cosumerApiImpl.obterDados(URL_API + tipo + "/marcas/" + marcaSelecionada + "/modelos/" + cdModelo + "/anos");
        System.out.println("\n::::::::::::::::::::::::::::::Retorno JSON::::::::::::::::::::::::::::::\n");
        System.out.println(json);
        var lsAno = converterDados.obterLista(json, Dados.class);
        var lsVeiculos = new ArrayList<>();
        System.out.println("\n::::::::::::::::::::::::::::::Retorno VEICULOS MODELO " + nmModelo.toUpperCase() + "::::::::::::::::::::::::::::::\n");
        for (int i = 0; i < lsAno.size(); i++) {
            var jsonVeiculos = cosumerApiImpl.obterDados(URL_API + tipo + "/marcas/" + marcaSelecionada + "/modelos/" + cdModelo + "/anos/" + lsAno.get(i).codigo());
            DadosVeiculo veiculo = converterDados.converter(jsonVeiculos, DadosVeiculo.class);
            lsVeiculos.add(veiculo);
        }
        lsVeiculos.forEach(System.out::println);

/*        lsAno.forEach(System.out::println);
        System.out.println("\nInforme o código do veiculo desejado:");
        var cdVeiculo = leitor.nextLine();
        buscarDetalhesVeiculo(cdModelo, tipo, marcaSelecionada, cdVeiculo);*/
    }

    private void buscarDetalhesVeiculo(String cdModelo, String tipo, String marcaSelecionada, String cdVeiculo) {
        var json = cosumerApiImpl.obterDados(URL_API + tipo + "/marcas/" + marcaSelecionada + "/modelos/" + cdModelo + "/anos/" + cdVeiculo);
        System.out.println("\n::::::::::::::::::::::::::::::Retorno JSON::::::::::::::::::::::::::::::\n");
        System.out.println(json);
        DadosVeiculo veiculo = converterDados.converter(json, DadosVeiculo.class);
        System.out.println("\n::::::::::::::::::::::::::::::Retorno VEICULO::::::::::::::::::::::::::::::\n");
        System.out.println(veiculo);
    }

}
