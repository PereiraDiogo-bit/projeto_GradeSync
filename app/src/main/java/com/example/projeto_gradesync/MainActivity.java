package com.example.projeto_gradesync;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private GradeViewModel mGradeViewModel;
    private TurmaDao mTurmaDao;
    private DisciplinaDao mDisciplinaDao;
    private ProfessorDao mProfessorDao;

    private TextView tvTotalTurmas, tvTotalDisciplinas, tvTotalProfessores;

    private List<Turma> mTurmas = new ArrayList<>();
    private List<Disciplina> mDisciplinas = new ArrayList<>();
    private List<Professor> mProfessores = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa DAOs para o Dashboard
        GradeSyncDatabase db = GradeSyncDatabase.getDatabase(this);
        mTurmaDao = db.turmaDao();
        mDisciplinaDao = db.disciplinaDao();
        mProfessorDao = db.professorDao();

        // Inicializa TextViews do Dashboard
        tvTotalTurmas = findViewById(R.id.tv_total_turmas);
        tvTotalDisciplinas = findViewById(R.id.tv_total_disciplinas);
        tvTotalProfessores = findViewById(R.id.tv_total_professores);

        // Inicializa ViewModels
        TurmaViewModel mTurmaViewModel = new ViewModelProvider(this).get(TurmaViewModel.class);
        DisciplinaViewModel mDisciplinaViewModel = new ViewModelProvider(this).get(DisciplinaViewModel.class);
        ProfessorViewModel mProfessorViewModel = new ViewModelProvider(this).get(ProfessorViewModel.class);
        mGradeViewModel = new ViewModelProvider(this).get(GradeViewModel.class);

        // Observa dados para garantir que estejam carregados para a geração
        mTurmaViewModel.getAllTurmas().observe(this, turmas -> mTurmas = turmas);
        mDisciplinaViewModel.getAllDisciplinas().observe(this, disciplinas -> mDisciplinas = disciplinas);
        mProfessorViewModel.getAllProfessores().observe(this, professores -> mProfessores = professores);

        MaterialCardView btnProfessores = findViewById(R.id.card_professores);
        MaterialCardView btnDisciplinas = findViewById(R.id.card_disciplinas);
        MaterialCardView btnTurmas = findViewById(R.id.card_turmas);
        MaterialCardView btnGrade = findViewById(R.id.card_grade);

        btnProfessores.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListaProfessoresActivity.class);
            startActivity(intent);
        });

        btnDisciplinas.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListaDisciplinasActivity.class);
            startActivity(intent);
        });

        btnTurmas.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListaTurmasActivity.class);
            startActivity(intent);
        });

        btnGrade.setOnClickListener(v -> showConfigDialog());
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizarDashboard();
    }

    private void atualizarDashboard() {
        Executors.newSingleThreadExecutor().execute(() -> {
            int totalTurmas = mTurmaDao.countTurmas();
            int totalDisciplinas = mDisciplinaDao.countDisciplinas();
            int totalProfessores = mProfessorDao.countProfessores();

            runOnUiThread(() -> {
                tvTotalTurmas.setText(String.valueOf(totalTurmas));
                tvTotalDisciplinas.setText(String.valueOf(totalDisciplinas));
                tvTotalProfessores.setText(String.valueOf(totalProfessores));
            });
        });
    }

    private void showConfigDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_config_grade, null);
        
        TextInputEditText etHora = dialogView.findViewById(R.id.et_config_hora_inicio);
        TextInputEditText etDuracao = dialogView.findViewById(R.id.et_config_duracao_aula);
        TextInputEditText etAntesRecreio = dialogView.findViewById(R.id.et_config_antes_recreio);
        TextInputEditText etRecreio = dialogView.findViewById(R.id.et_config_duracao_recreio);
        TextInputEditText etTotal = dialogView.findViewById(R.id.et_config_total_aulas);

        new MaterialAlertDialogBuilder(this)
                .setTitle("Configurar Geração de Grade")
                .setView(dialogView)
                .setPositiveButton("Gerar", (dialog, which) -> {
                    try {
                        String hora = Objects.requireNonNull(etHora.getText()).toString();
                        int duracao = Integer.parseInt(Objects.requireNonNull(etDuracao.getText()).toString());
                        int antes = Integer.parseInt(Objects.requireNonNull(etAntesRecreio.getText()).toString());
                        int recreio = Integer.parseInt(Objects.requireNonNull(etRecreio.getText()).toString());
                        int total = Integer.parseInt(Objects.requireNonNull(etTotal.getText()).toString());

                        if (mTurmas.isEmpty() || mDisciplinas.isEmpty() || mProfessores.isEmpty()) {
                            Toast.makeText(this, "Cadastre professores, disciplinas e turmas primeiro!", Toast.LENGTH_LONG).show();
                            return;
                        }

                        mGradeViewModel.deleteAll();
                        List<Grade> novaGrade = GeradorGradeHelper.gerarGrade(mTurmas, mDisciplinas, mProfessores, 
                                                                             hora, duracao, antes, recreio, total);
                        
                        mGradeViewModel.insertAll(novaGrade);
                        
                        Toast.makeText(this, "Grade gerada com sucesso!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, VisualizarGradeActivity.class));

                    } catch (Exception e) {
                        Toast.makeText(this, "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
