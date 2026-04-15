package com.example.projeto_gradesync;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "professores")
public class Professor {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nome;

    private String diasDisponiveis;

    private int disciplinaId;

    private int cargaHoraria;

    private String turmasPreferencia;

    public Professor(String nome, String diasDisponiveis, int disciplinaId, int cargaHoraria, String turmasPreferencia) {
        this.nome = nome;
        this.diasDisponiveis = diasDisponiveis;
        this.disciplinaId = disciplinaId;
        this.cargaHoraria = cargaHoraria;
        this.turmasPreferencia = turmasPreferencia;
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

    public String getDiasDisponiveis() {
        return diasDisponiveis;
    }

    public void setDiasDisponiveis(String diasDisponiveis) {
        this.diasDisponiveis = diasDisponiveis;
    }

    public int getDisciplinaId() {
        return disciplinaId;
    }

    public void setDisciplinaId(int disciplinaId) {
        this.disciplinaId = disciplinaId;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public String getTurmasPreferencia() {
        return turmasPreferencia;
    }

    public void setTurmasPreferencia(String turmasPreferencia) {
        this.turmasPreferencia = turmasPreferencia;
    }
}
