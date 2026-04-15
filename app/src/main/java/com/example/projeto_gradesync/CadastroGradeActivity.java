package com.example.projeto_gradesync;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class CadastroGradeActivity extends AppCompatActivity {

    private TurmaViewModel mTurmaViewModel;
    private DisciplinaViewModel mDisciplinaViewModel;
    private ProfessorViewModel mProfessorViewModel;
    private GradeViewModel mGradeViewModel;

    private MaterialAutoCompleteTextView actvClass, actvSubject, actvTeacher, actvDay;
    private TextInputEditText etTime;

    private List<Turma> mTurmas = new ArrayList<>();
    private List<Disciplina> mDisciplinas = new ArrayList<>();
    private List<Professor> mProfessores = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_grade);

        // Inicializa os ViewModels
        mTurmaViewModel = new ViewModelProvider(this).get(TurmaViewModel.class);
        mDisciplinaViewModel = new ViewModelProvider(this).get(DisciplinaViewModel.class);
        mProfessorViewModel = new ViewModelProvider(this).get(ProfessorViewModel.class);
        mGradeViewModel = new ViewModelProvider(this).get(GradeViewModel.class);

        // Inicializa os componentes da View
        actvClass = findViewById(R.id.actv_class);
        actvSubject = findViewById(R.id.actv_subject);
        actvTeacher = findViewById(R.id.actv_teacher);
        actvDay = findViewById(R.id.actv_day);
        etTime = findViewById(R.id.et_time);
        MaterialButton btnSchedule = findViewById(R.id.btn_schedule_lesson);

        setupDropdowns();

        btnSchedule.setOnClickListener(v -> scheduleLesson());
    }

    private void setupDropdowns() {
        // Observa Turmas
        mTurmaViewModel.getAllTurmas().observe(this, turmas -> {
            mTurmas = turmas;
            List<String> names = new ArrayList<>();
            for (Turma t : turmas) names.add(t.getNome());
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
            actvClass.setAdapter(adapter);
        });

        // Observa Disciplinas
        mDisciplinaViewModel.getAllDisciplinas().observe(this, disciplinas -> {
            mDisciplinas = disciplinas;
            List<String> names = new ArrayList<>();
            for (Disciplina d : disciplinas) names.add(d.getNome());
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
            actvSubject.setAdapter(adapter);
        });

        // Observa Professores
        mProfessorViewModel.getAllProfessores().observe(this, professores -> {
            mProfessores = professores;
            List<String> names = new ArrayList<>();
            for (Professor p : professores) names.add(p.getNome());
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
            actvTeacher.setAdapter(adapter);
        });

        // Configura Dias da Semana
        String[] days = {
                getString(R.string.monday),
                getString(R.string.tuesday),
                getString(R.string.wednesday),
                getString(R.string.thursday),
                getString(R.string.friday)
        };
        ArrayAdapter<String> adapterDays = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, days);
        actvDay.setAdapter(adapterDays);
    }

    private void scheduleLesson() {
        String selectedClassName = actvClass.getText().toString();
        String selectedSubjectName = actvSubject.getText().toString();
        String selectedTeacherName = actvTeacher.getText().toString();
        String selectedDay = actvDay.getText().toString();
        String time = etTime.getText() != null ? etTime.getText().toString() : "";

        if (selectedClassName.isEmpty() || selectedSubjectName.isEmpty() || 
            selectedTeacherName.isEmpty() || selectedDay.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Recupera os IDs
        int turmaId = -1;
        for (Turma t : mTurmas) {
            if (t.getNome().equals(selectedClassName)) {
                turmaId = t.getId();
                break;
            }
        }

        int disciplinaId = -1;
        for (Disciplina d : mDisciplinas) {
            if (d.getNome().equals(selectedSubjectName)) {
                disciplinaId = d.getId();
                break;
            }
        }

        int professorId = -1;
        for (Professor p : mProfessores) {
            if (p.getNome().equals(selectedTeacherName)) {
                professorId = p.getId();
                break;
            }
        }

        if (turmaId != -1 && disciplinaId != -1 && professorId != -1) {
            Grade newGrade = new Grade(turmaId, disciplinaId, professorId, selectedDay, time);
            mGradeViewModel.insert(newGrade);
            Toast.makeText(this, "Aula agendada com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao recuperar dados selecionados", Toast.LENGTH_SHORT).show();
        }
    }
}
