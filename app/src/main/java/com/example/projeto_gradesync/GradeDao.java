package com.example.projeto_gradesync;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GradeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Grade grade);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Grade> grades);

    @Update
    void update(Grade grade);

    @Delete
    void delete(Grade grade);

    @Query("DELETE FROM grades")
    void deleteAll();

    @Query("SELECT * FROM grades ORDER BY turmaId ASC, " +
            "CASE diaSemana " +
            "WHEN 'Segunda-feira' THEN 1 " +
            "WHEN 'Terça-feira' THEN 2 " +
            "WHEN 'Quarta-feira' THEN 3 " +
            "WHEN 'Quinta-feira' THEN 4 " +
            "WHEN 'Sexta-feira' THEN 5 " +
            "ELSE 6 END, horario ASC")
    LiveData<List<Grade>> getAllGrades();

    @Query("SELECT * FROM grades WHERE turmaId = :turmaId ORDER BY diaSemana, horario")
    LiveData<List<Grade>> getGradesPorTurma(int turmaId);
}
