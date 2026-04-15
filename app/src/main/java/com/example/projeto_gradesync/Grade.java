package com.example.projeto_gradesync;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "grades")
public class Grade {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int turmaId;
    private int disciplinaId;
    private int professorId;
    private String diaSemana;
    private String horario;

    public Grade(int turmaId, int disciplinaId, int professorId, String diaSemana, String horario) {
        this.turmaId = turmaId;
        this.disciplinaId = disciplinaId;
        this.professorId = professorId;
        this.diaSemana = diaSemana;
        this.horario = horario;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTurmaId() {
        return turmaId;
    }

    public void setTurmaId(int turmaId) {
        this.turmaId = turmaId;
    }

    public int getDisciplinaId() {
        return disciplinaId;
    }

    public void setDisciplinaId(int disciplinaId) {
        this.disciplinaId = disciplinaId;
    }

    public int getProfessorId() {
        return professorId;
    }

    public void setProfessorId(int professorId) {
        this.professorId = professorId;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
}
