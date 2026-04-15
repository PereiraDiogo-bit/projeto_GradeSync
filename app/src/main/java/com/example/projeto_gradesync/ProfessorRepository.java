package com.example.projeto_gradesync;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

/**
 * Repository para abstrair o acesso à fonte de dados (Room).
 * Centraliza a lógica de execução em threads separadas.
 */
public class ProfessorRepository {

    private final ProfessorDao mProfessorDao;
    private final LiveData<List<Professor>> mAllProfessores;

    public ProfessorRepository(Application application) {
        GradeSyncDatabase db = GradeSyncDatabase.getDatabase(application);
        mProfessorDao = db.professorDao();
        mAllProfessores = mProfessorDao.getAllProfessores();
    }

    // LiveData permite observar as mudanças nos dados automaticamente na View
    public LiveData<List<Professor>> getAllProfessores() {
        return mAllProfessores;
    }

    // Operações de escrita devem ser executadas fora da Main Thread
    public void insert(Professor professor) {
        GradeSyncDatabase.databaseWriteExecutor.execute(() -> {
            mProfessorDao.insert(professor);
        });
    }

    public void update(Professor professor) {
        GradeSyncDatabase.databaseWriteExecutor.execute(() -> {
            mProfessorDao.update(professor);
        });
    }

    public void delete(Professor professor) {
        GradeSyncDatabase.databaseWriteExecutor.execute(() -> {
            mProfessorDao.delete(professor);
        });
    }
}
