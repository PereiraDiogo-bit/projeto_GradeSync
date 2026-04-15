package com.example.projeto_gradesync;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class CadastroDisciplinaActivity extends AppCompatActivity {

    private DisciplinaViewModel mDisciplinaViewModel;
    private TextInputEditText etNome, etCargaHoraria;
    private int disciplinaIdParaEdicao = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_disciplina);

        // Inicializa o ViewModel
        mDisciplinaViewModel = new ViewModelProvider(this).get(DisciplinaViewModel.class);

        // Inicializa os componentes
        etNome = findViewById(R.id.et_nome_disciplina);
        etCargaHoraria = findViewById(R.id.et_carga_horaria);
        MaterialButton btnSalvar = findViewById(R.id.btn_salvar_disciplina);

        // Resgata o ID para verificar se é edição
        disciplinaIdParaEdicao = getIntent().getIntExtra("DISCIPLINA_ID", -1);

        if (disciplinaIdParaEdicao != -1) {
            btnSalvar.setText("Atualizar Disciplina");
            preencherDadosDisciplina();
        }

        btnSalvar.setOnClickListener(v -> salvarDisciplina());
    }

    private void preencherDadosDisciplina() {
        mDisciplinaViewModel.getAllDisciplinas().observe(this, disciplinas -> {
            for (Disciplina d : disciplinas) {
                if (d.getId() == disciplinaIdParaEdicao) {
                    etNome.setText(d.getNome());
                    etCargaHoraria.setText(String.valueOf(d.getCargaHoraria()));
                    break;
                }
            }
        });
    }

    private void salvarDisciplina() {
        String nome = etNome.getText() != null ? etNome.getText().toString().trim() : "";
        String cargaStr = etCargaHoraria.getText() != null ? etCargaHoraria.getText().toString().trim() : "";

        if (nome.isEmpty()) {
            etNome.setError("Insira o nome da disciplina");
            return;
        }

        if (cargaStr.isEmpty()) {
            etCargaHoraria.setError("Insira a carga horária");
            return;
        }

        try {
            int cargaHoraria = Integer.parseInt(cargaStr);
            
            if (disciplinaIdParaEdicao != -1) {
                // Modo de Edição (Update)
                Disciplina disciplinaEditada = new Disciplina(nome, cargaHoraria);
                disciplinaEditada.setId(disciplinaIdParaEdicao);
                mDisciplinaViewModel.update(disciplinaEditada);
                Toast.makeText(this, "Disciplina atualizada com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                // Modo de Cadastro (Insert)
                Disciplina novaDisciplina = new Disciplina(nome, cargaHoraria);
                mDisciplinaViewModel.insert(novaDisciplina);
                Toast.makeText(this, "Disciplina salva com sucesso!", Toast.LENGTH_SHORT).show();
            }

            finish();
            
        } catch (NumberFormatException e) {
            etCargaHoraria.setError("Valor inválido");
        }
    }
}
