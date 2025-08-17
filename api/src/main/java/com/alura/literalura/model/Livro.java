package com.alura.literalura.model;

import com.alura.literalura.dto.AuthorDTO;
import com.alura.literalura.dto.LivroDTO;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import java.util.List;

@Entity
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String idioma;

    @ManyToOne
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JoinColumn(name = "autor_id")
    private Autor autor;

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Livro(LivroDTO dados) {
        this.titulo = dados.title();
        this.idioma = String.join(", ", dados.languages());
        this.autor = new Autor(dados.authors().get(0));
    }

    public Livro(){
    }

    @Override
    public String toString(){
        return "Título: " + titulo +
                "\nIdioma: " + idioma + "\nAutor: " + autor;
    }
}
