package com.example.projeto_gradesync;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

/**
 * Repository para gerenciar a fonte de dados das Disciplinas.
 */
public class DisciplinaRepository {

    private final DisciplinaDao mDisciplinaDao;
    private final LiveData<List<Disciplina>> mAllDisciplinas;

    public DisciplinaRepository(Application application) {
        GradeSyncDatabase db = GradeSyncDatabase.getDatabase(application);
        mDisciplinaDao = db.disciplinaDao();
        mAllDisciplinas = mDisciplinaDao.getAllDisciplinas();
    }

    public LiveData<List<Disciplina>> getAllDisciplinas() {
        return mAllDisciplinas;
    }

    public void insert(Disciplina disciplina) {
        GradeSyncDatabase.databaseWriteExecutor.execute(() -> mDisciplinaDao.insert(disciplina));
    }

    public void update(Disciplina disciplina) {
        GradeSyncDatabase.databaseWriteExecutor.execute(() -> mDisciplinaDao.update(disciplina));
    }

    public void delete(Disciplina disciplina) {
        GradeSyncDatabase.databaseWriteExecutor.execute(() -> mDisciplinaDao.delete(disciplina));
    }
}
