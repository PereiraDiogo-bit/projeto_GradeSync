package com.example.projeto_gradesync;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Professor.class, Disciplina.class, Turma.class, Grade.class}, version = 6, exportSchema = false)
public abstract class GradeSyncDatabase extends RoomDatabase {

    public abstract ProfessorDao professorDao();
    public abstract DisciplinaDao disciplinaDao();
    public abstract TurmaDao turmaDao();
    public abstract GradeDao gradeDao();

    private static volatile GradeSyncDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static GradeSyncDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (GradeSyncDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    GradeSyncDatabase.class, "gradesync_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
