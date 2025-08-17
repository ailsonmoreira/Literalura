package com.alura.literalura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AuthorDTO(String name, @JsonAlias("birth_year") Integer dataNascimento, @JsonAlias("death_year") Integer dataFalecimento) {
    public String getFullName() {
        // Verifica se o nome está no formato "sobrenome, nome"
        if (name != null && name.contains(",")) {
            String[] parts = name.split(","); // Divide em duas partes: sobrenome e nome
            String sobrenome = parts[0].trim();  // Sobrenome (primeira parte)
            String nome = parts[1].trim();  // Nome (segunda parte)
            return nome + " " + sobrenome;  // Junta nome e sobrenome no formato desejado
        }
        return name;  // Se não estiver no formato esperado, retorna o nome original
    }
}
