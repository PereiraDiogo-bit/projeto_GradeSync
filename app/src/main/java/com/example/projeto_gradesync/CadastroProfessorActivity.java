package com.example.projeto_gradesync;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class CadastroProfessorActivity extends AppCompatActivity {

    private ProfessorViewModel mProfessorViewModel;
    private DisciplinaViewModel mDisciplinaViewModel;
    private TextInputEditText etNome, etCarga, etPreferencia;
    private AutoCompleteTextView actvDisciplina;
    private MaterialCheckBox cbMonday, cbTuesday, cbWednesday, cbThursday, cbFriday;
    private List<Disciplina> mDisciplinas = new ArrayList<>();
    
    // Variável para controle do modo de edição
    private int professorIdParaEdicao = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_professor);

        // Inicializa os ViewModels
        mProfessorViewModel = new ViewModelProvider(this).get(ProfessorViewModel.class);
        mDisciplinaViewModel = new ViewModelProvider(this).get(DisciplinaViewModel.class);

        // Inicializa os componentes da View
        etNome = findViewById(R.id.et_nome_professor);
        etCarga = findViewById(R.id.et_carga_professor);
        etPreferencia = findViewById(R.id.et_preferencia_professor);
        actvDisciplina = findViewById(R.id.actv_disciplina_professor);
        cbMonday = findViewById(R.id.cb_monday);
        cbTuesday = findViewById(R.id.cb_tuesday);
        cbWednesday = findViewById(R.id.cb_wednesday);
        cbThursday = findViewById(R.id.cb_thursday);
        cbFriday = findViewById(R.id.cb_friday);

        MaterialButton btnSalvar = findViewById(R.id.btn_salvar_professor);

        // Resgata o ID para verificar se é edição
        professorIdParaEdicao = getIntent().getIntExtra("PROFESSOR_ID", -1);
        
        setupDisciplinaSpinner();

        // Se for Modo Edição, preenche os campos
        if (professorIdParaEdicao != -1) {
            btnSalvar.setText("Atualizar Professor");
            preencherDadosProfessor();
        }

        btnSalvar.setOnClickListener(v -> salvarProfessor());
    }

    private void preencherDadosProfessor() {
        mProfessorViewModel.getAllProfessores().observe(this, professores -> {
            for (Professor p : professores) {
                if (p.getId() == professorIdParaEdicao) {
                    etNome.setText(p.getNome());
                    etCarga.setText(String.valueOf(p.getCargaHoraria()));
                    etPreferencia.setText(p.getTurmasPreferencia());
                    setDiasSelecionados(p.getDiasDisponiveis());
                    
                    // Preencher a disciplina (precisa esperar o carregamento das disciplinas)
                    mDisciplinaViewModel.getAllDisciplinas().observe(this, disciplinas -> {
                        for (Disciplina d : disciplinas) {
                            if (d.getId() == p.getDisciplinaId()) {
                                actvDisciplina.setText(d.getNome(), false);
                                break;
                            }
                        }
                    });
                    break;
                }
            }
        });
    }

    private void setupDisciplinaSpinner() {
        mDisciplinaViewModel.getAllDisciplinas().observe(this, disciplinas -> {
            mDisciplinas = disciplinas;
            List<String> nomes = new ArrayList<>();
            for (Disciplina d : disciplinas) {
                nomes.add(d.getNome());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
                    android.R.layout.simple_dropdown_item_1line, nomes);
            actvDisciplina.setAdapter(adapter);
        });
    }

    private void salvarProfessor() {
        String nome = etNome.getText() != null ? etNome.getText().toString().trim() : "";
        String disciplinaSelecionada = actvDisciplina.getText().toString();
        String cargaStr = etCarga.getText() != null ? etCarga.getText().toString().trim() : "";
        String preferencia = etPreferencia.getText() != null ? etPreferencia.getText().toString().trim() : "";

        if (nome.isEmpty()) {
            etNome.setError("Insira o nome");
            return;
        }

        if (disciplinaSelecionada.isEmpty()) {
            Toast.makeText(this, "Selecione uma disciplina", Toast.LENGTH_SHORT).show();
            return;
        }

        int carga = 0;
        if (!cargaStr.isEmpty()) {
            try {
                carga = Integer.parseInt(cargaStr);
            } catch (NumberFormatException e) {
                etCarga.setError("Valor inválido");
                return;
            }
        }

        int disciplinaId = -1;
        for (Disciplina d : mDisciplinas) {
            if (d.getNome().equals(disciplinaSelecionada)) {
                disciplinaId = d.getId();
                break;
            }
        }

        String diasSelecionados = getDiasSelecionados();

        if (professorIdParaEdicao != -1) {
            // Modo de Edição (Update)
            Professor professorEditado = new Professor(nome, diasSelecionados, disciplinaId, carga, preferencia);
            professorEditado.setId(professorIdParaEdicao);
            mProfessorViewModel.update(professorEditado);
            Toast.makeText(this, "Professor atualizado com sucesso!", Toast.LENGTH_SHORT).show();
        } else {
            // Modo de Cadastro (Insert)
            Professor novoProfessor = new Professor(nome, diasSelecionados, disciplinaId, carga, preferencia);
            mProfessorViewModel.insert(novoProfessor);
            Toast.makeText(this, "Professor salvo com sucesso!", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    private void setDiasSelecionados(String diasString) {
        if (diasString == null) return;
        cbMonday.setChecked(diasString.contains("Segunda"));
        cbTuesday.setChecked(diasString.contains("Terça"));
        cbWednesday.setChecked(diasString.contains("Quarta"));
        cbThursday.setChecked(diasString.contains("Quinta"));
        cbFriday.setChecked(diasString.contains("Sexta"));
    }

    private String getDiasSelecionados() {
        List<String> dias = new ArrayList<>();
        if (cbMonday != null && cbMonday.isChecked()) dias.add("Segunda");
        if (cbTuesday != null && cbTuesday.isChecked()) dias.add("Terça");
        if (cbWednesday != null && cbWednesday.isChecked()) dias.add("Quarta");
        if (cbThursday != null && cbThursday.isChecked()) dias.add("Quinta");
        if (cbFriday != null && cbFriday.isChecked()) dias.add("Sexta");

        return TextUtils.join(", ", dias);
    }
}
