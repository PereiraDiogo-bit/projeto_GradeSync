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
public interface DisciplinaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Disciplina disciplina);

    @Update
    void update(Disciplina disciplina);

    @Delete
    void delete(Disciplina disciplina);

    @Query("SELECT * FROM disciplinas ORDER BY nome ASC")
    LiveData<List<Disciplina>> getAllDisciplinas();

    @Query("SELECT COUNT(*) FROM disciplinas")
    int countDisciplinas();
}
