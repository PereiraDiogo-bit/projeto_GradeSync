package com.example.projeto_gradesync;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class CadastroTurmaActivity extends AppCompatActivity {

    private TurmaViewModel mTurmaViewModel;
    private TextInputEditText etNome;
    private int turmaIdParaEdicao = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_turma);

        // Inicializa o ViewModel
        mTurmaViewModel = new ViewModelProvider(this).get(TurmaViewModel.class);

        // Inicializa os componentes
        etNome = findViewById(R.id.et_nome_turma);
        MaterialButton btnSalvar = findViewById(R.id.btn_salvar_turma);

        // Resgata o ID para verificar se é edição
        turmaIdParaEdicao = getIntent().getIntExtra("TURMA_ID", -1);

        if (turmaIdParaEdicao != -1) {
            btnSalvar.setText("Atualizar Turma");
            preencherDadosTurma();
        }

        btnSalvar.setOnClickListener(v -> salvarTurma());
    }

    private void preencherDadosTurma() {
        mTurmaViewModel.getAllTurmas().observe(this, turmas -> {
            for (Turma t : turmas) {
                if (t.getId() == turmaIdParaEdicao) {
                    etNome.setText(t.getNome());
                    break;
                }
            }
        });
    }

    private void salvarTurma() {
        String nome = etNome.getText() != null ? etNome.getText().toString().trim() : "";

        if (nome.isEmpty()) {
            etNome.setError("Insira o nome da turma");
            return;
        }

        if (turmaIdParaEdicao != -1) {
            // Modo de Edição (Update)
            Turma turmaEditada = new Turma(nome);
            turmaEditada.setId(turmaIdParaEdicao);
            mTurmaViewModel.update(turmaEditada);
            Toast.makeText(this, "Turma atualizada com sucesso!", Toast.LENGTH_SHORT).show();
        } else {
            // Modo de Cadastro (Insert)
            Turma novaTurma = new Turma(nome);
            mTurmaViewModel.insert(novaTurma);
            Toast.makeText(this, "Turma salva com sucesso!", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
