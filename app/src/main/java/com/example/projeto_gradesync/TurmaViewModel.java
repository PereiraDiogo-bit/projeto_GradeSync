package com.example.projeto_gradesync;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

/**
 * ViewModel para a camada de Turmas.
 */
public class TurmaViewModel extends AndroidViewModel {

    private final TurmaRepository mRepository;
    private final LiveData<List<Turma>> mAllTurmas;

    public TurmaViewModel(@NonNull Application application) {
        super(application);
        mRepository = new TurmaRepository(application);
        mAllTurmas = mRepository.getAllTurmas();
    }

    public LiveData<List<Turma>> getAllTurmas() {
        return mAllTurmas;
    }

    public void insert(Turma turma) {
        mRepository.insert(turma);
    }

    public void update(Turma turma) {
        mRepository.update(turma);
    }

    public void delete(Turma turma) {
        mRepository.delete(turma);
    }
}
