package com.example.projeto_gradesync;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

/**
 * ViewModel para a camada de Disciplinas.
 */
public class DisciplinaViewModel extends AndroidViewModel {

    private final DisciplinaRepository mRepository;
    private final LiveData<List<Disciplina>> mAllDisciplinas;

    public DisciplinaViewModel(@NonNull Application application) {
        super(application);
        mRepository = new DisciplinaRepository(application);
        mAllDisciplinas = mRepository.getAllDisciplinas();
    }

    public LiveData<List<Disciplina>> getAllDisciplinas() {
        return mAllDisciplinas;
    }

    public void insert(Disciplina disciplina) {
        mRepository.insert(disciplina);
    }

    public void update(Disciplina disciplina) {
        mRepository.update(disciplina);
    }

    public void delete(Disciplina disciplina) {
        mRepository.delete(disciplina);
    }
}
