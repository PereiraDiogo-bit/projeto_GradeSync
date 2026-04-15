package com.example.projeto_gradesync;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "turmas")
public class Turma {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nome;

    public Turma(String nome) {
        this.nome = nome;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
