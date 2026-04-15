package com.example.projeto_gradesync;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class GradeViewModel extends AndroidViewModel {

    private final GradeRepository mRepository;
    private final LiveData<List<Grade>> mAllGrades;

    public GradeViewModel(@NonNull Application application) {
        super(application);
        mRepository = new GradeRepository(application);
        mAllGrades = mRepository.getAllGrades();
    }

    public LiveData<List<Grade>> getAllGrades() {
        return mAllGrades;
    }

    public LiveData<List<Grade>> getGradesPorTurma(int turmaId) {
        return mRepository.getGradesPorTurma(turmaId);
    }

    public void insert(Grade grade) {
        mRepository.insert(grade);
    }

    public void insertAll(List<Grade> grades) {
        mRepository.insertAll(grades);
    }

    public void update(Grade grade) {
        mRepository.update(grade);
    }

    public void delete(Grade grade) {
        mRepository.delete(grade);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }
}
