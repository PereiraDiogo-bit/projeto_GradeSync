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
public interface TurmaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Turma turma);

    @Update
    void update(Turma turma);

    @Delete
    void delete(Turma turma);

    @Query("SELECT * FROM turmas ORDER BY nome ASC")
    LiveData<List<Turma>> getAllTurmas();

    @Query("SELECT COUNT(*) FROM turmas")
    int countTurmas();
}
