package com.alura.literalura.main;

import com.alura.literalura.dto.AuthorDTO;
import com.alura.literalura.dto.LivroDTO;
import com.alura.literalura.dto.ResultadoDTO;
import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Livro;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LivroRepository;
import com.alura.literalura.service.ConsumoApi;
import com.alura.literalura.service.DataConversion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class Main {
    @Autowired
    private AutorRepository autorRepository;
    @Autowired
    private LivroRepository livroRepository;
    private DataConversion conversor = new DataConversion();
    private Scanner input = new Scanner(System.in);
    private String url = "https://gutendex.com/books/?search=";
    ConsumoApi consumo = new ConsumoApi();

    public ResultadoDTO getLivroDTO(){
        System.out.println("Insira o título do livro");
        String livro = input.nextLine();


        try {
            String livroCodificado = URLEncoder.encode(livro, "UTF-8");

            var pesquisa = consumo.obterDados(url + livroCodificado);
            ResultadoDTO dados = conversor.obterDados(pesquisa, ResultadoDTO.class);
            return dados;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void exibeMenu(){
        System.out.println("Escolha uma opção: ");

        var opcao = -1;

        while (opcao != 0){
            var menu = """
                    ____________________________
                    1 - Buscar livro pelo título
                    2 - Listar livros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos em um determinado ano
                    5 - Listar quantidade de livros por idioma
                    0 - Sair
                    ____________________________
                    """;

            System.out.println(menu);
            opcao = input.nextInt();
            input.nextLine();

            switch (opcao){
                case 1:
                    buscarLivro();
                    break;
                case 2:
                    buscarLivrosRegistrados();
                    break;
                case 3:
                    buscarAutoresRegistrados();
                    break;
                case 4:
                    buscarAutoresPorAno();
                    break;
                case 5:
                    buscarLivroPorIdioma();
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarAutoresPorAno() {
        System.out.println("Qual ano deseja buscar?");
        Integer anoBuscado = input.nextInt();

        System.out.println("\nAutores vivos no ano de " + anoBuscado + ":");

        List<Autor> autoresVivos = autorRepository.findAutoresVivosEmAno(anoBuscado);
        autoresVivos.forEach(a -> System.out.println(a.getNome()));
    }

    private void buscarLivroPorIdioma() {
        List<Livro> livros = livroRepository.findAll();

        Map<String, Long> quantidadePorIdioma = livros.stream()
                .collect(Collectors.groupingBy(Livro::getIdioma, Collectors.counting()));

        System.out.println("\nQuantidade de livros por idioma: ");
        quantidadePorIdioma.forEach((idioma, quantidade) ->
                System.out.println(idioma + ": " + quantidade));
    }

    private void buscarAutoresRegistrados() {
        var autoresRegistrados = autorRepository.findAll();
        System.out.println("\nAutores cadastrados: ");
        autoresRegistrados.forEach(a -> System.out.println(a.getNome()));
    }

    private void buscarLivrosRegistrados() {

        var livrosEncontrados = livroRepository.findAll();
        System.out.println("\nLivros registrados: ");

        livrosEncontrados.forEach(l -> System.out.println(l.getTitulo() + " (" + l.getAutor() + ")"));

    }


    public void buscarLivro(){
        ResultadoDTO dados = getLivroDTO();
        List<LivroDTO> livros = dados.results();

        if (livros.isEmpty()){
            System.out.println("Nenhum livro encontrado");
        } else {
           LivroDTO dadosLivro = livros.get(0);
           AuthorDTO authorDTO = dadosLivro.authors().get(0);
           Autor autor = new Autor(authorDTO);
           Livro livro = new Livro(dadosLivro);
           livro.setAutor(autor);

           livroRepository.save(livro);

            System.out.println("Título: " + dadosLivro.title());
            dadosLivro.authors().forEach(author -> {
                System.out.println("Autor: " + author.getFullName());
            });
            String idiomas = String.join(", ", dadosLivro.languages());
            System.out.println("Idiomas: " + idiomas);
            System.out.println("Downloads: " + dados.count());

        }
    }

}
