package com.example.projeto_gradesync;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

public class GradeRepository {

    private final GradeDao mGradeDao;
    private final LiveData<List<Grade>> mAllGrades;

    public GradeRepository(Application application) {
        GradeSyncDatabase db = GradeSyncDatabase.getDatabase(application);
        mGradeDao = db.gradeDao();
        mAllGrades = mGradeDao.getAllGrades();
    }

    public LiveData<List<Grade>> getAllGrades() {
        return mAllGrades;
    }

    public LiveData<List<Grade>> getGradesPorTurma(int turmaId) {
        return mGradeDao.getGradesPorTurma(turmaId);
    }

    public void insert(Grade grade) {
        GradeSyncDatabase.databaseWriteExecutor.execute(() -> {
            mGradeDao.insert(grade);
        });
    }

    public void insertAll(List<Grade> grades) {
        GradeSyncDatabase.databaseWriteExecutor.execute(() -> {
            mGradeDao.insertAll(grades);
        });
    }

    public void update(Grade grade) {
        GradeSyncDatabase.databaseWriteExecutor.execute(() -> {
            mGradeDao.update(grade);
        });
    }

    public void delete(Grade grade) {
        GradeSyncDatabase.databaseWriteExecutor.execute(() -> {
            mGradeDao.delete(grade);
        });
    }

    public void deleteAll() {
        GradeSyncDatabase.databaseWriteExecutor.execute(() -> {
            mGradeDao.deleteAll();
        });
    }
}
