package com.example.projeto_gradesync;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

/**
 * ViewModel para fornecer dados à UI e sobreviver a mudanças de configuração.
 * Atua como ponte entre a View e o Repository.
 */
public class ProfessorViewModel extends AndroidViewModel {

    private final ProfessorRepository mRepository;
    private final LiveData<List<Professor>> mAllProfessores;

    public ProfessorViewModel(@NonNull Application application) {
        super(application);
        mRepository = new ProfessorRepository(application);
        mAllProfessores = mRepository.getAllProfessores();
    }

    public LiveData<List<Professor>> getAllProfessores() {
        return mAllProfessores;
    }

    public void insert(Professor professor) {
        mRepository.insert(professor);
    }

    public void update(Professor professor) {
        mRepository.update(professor);
    }

    public void delete(Professor professor) {
        mRepository.delete(professor);
    }
}
