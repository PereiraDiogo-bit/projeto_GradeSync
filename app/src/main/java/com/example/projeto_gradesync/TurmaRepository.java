package com.example.projeto_gradesync;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

/**
 * Repository para gerenciar a fonte de dados das Turmas.
 */
public class TurmaRepository {

    private final TurmaDao mTurmaDao;
    private final LiveData<List<Turma>> mAllTurmas;

    public TurmaRepository(Application application) {
        GradeSyncDatabase db = GradeSyncDatabase.getDatabase(application);
        mTurmaDao = db.turmaDao();
        mAllTurmas = mTurmaDao.getAllTurmas();
    }

    public LiveData<List<Turma>> getAllTurmas() {
        return mAllTurmas;
    }

    public void insert(Turma turma) {
        GradeSyncDatabase.databaseWriteExecutor.execute(() -> {
            mTurmaDao.insert(turma);
        });
    }

    public void update(Turma turma) {
        GradeSyncDatabase.databaseWriteExecutor.execute(() -> {
            mTurmaDao.update(turma);
        });
    }

    public void delete(Turma turma) {
        GradeSyncDatabase.databaseWriteExecutor.execute(() -> {
            mTurmaDao.delete(turma);
        });
    }
}
