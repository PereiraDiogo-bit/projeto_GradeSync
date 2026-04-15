package com.example.projeto_gradesync;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VisualizarGradeActivity extends AppCompatActivity {

    private GradeViewModel mGradeViewModel;
    private TurmaViewModel mTurmaViewModel;
    private DisciplinaViewModel mDisciplinaViewModel;
    private ProfessorViewModel mProfessorViewModel;

    private TableLayout tabelaGeral;
    private AppCompatSpinner spinnerTurmas;
    
    private List<Grade> mGrades = new ArrayList<>();
    private List<Turma> mTurmas = new ArrayList<>();
    private List<Disciplina> mDisciplinas = new ArrayList<>();
    private List<Professor> mProfessores = new ArrayList<>();

    private final ActivityResultLauncher<Intent> pdfLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        gerarPdf(uri);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_grade);

        tabelaGeral = findViewById(R.id.tabela_geral);
        spinnerTurmas = findViewById(R.id.spinner_turmas);
        MaterialButton btnVoltar = findViewById(R.id.btn_voltar);
        ExtendedFloatingActionButton btnExportarPdf = findViewById(R.id.fab_exportar_pdf);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btnVoltar.setOnClickListener(v -> finish());

        if (btnExportarPdf != null) {
            btnExportarPdf.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/pdf");
                intent.putExtra(Intent.EXTRA_TITLE, "Quadro_Horarios.pdf");
                pdfLauncher.launch(intent);
            });
        }

        mGradeViewModel = new ViewModelProvider(this).get(GradeViewModel.class);
        mTurmaViewModel = new ViewModelProvider(this).get(TurmaViewModel.class);
        mDisciplinaViewModel = new ViewModelProvider(this).get(DisciplinaViewModel.class);
        mProfessorViewModel = new ViewModelProvider(this).get(ProfessorViewModel.class);

        carregarDados();

        spinnerTurmas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selecionada = (String) parent.getItemAtPosition(position);
                montarTabela(selecionada);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void gerarPdf(Uri uri) {
        PdfDocument document = new PdfDocument();
        int width = tabelaGeral.getWidth();
        int height = tabelaGeral.getHeight();

        if (width <= 0 || height <= 0) {
            width = 1000;
            height = 2000;
        }

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(width, height, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        tabelaGeral.draw(page.getCanvas());
        document.finishPage(page);

        try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {
            if (outputStream != null) {
                document.writeTo(outputStream);
                Toast.makeText(this, "PDF exportado com sucesso!", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(this, "Erro ao salvar PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            document.close();
        }
    }

    private void carregarDados() {
        mTurmaViewModel.getAllTurmas().observe(this, turmas -> {
            mTurmas = turmas;
            configurarSpinner();
            mDisciplinaViewModel.getAllDisciplinas().observe(this, disciplinas -> {
                mDisciplinas = disciplinas;
                mProfessorViewModel.getAllProfessores().observe(this, professores -> {
                    mProfessores = professores;
                    mGradeViewModel.getAllGrades().observe(this, grades -> {
                        mGrades = grades;
                        montarTabela("Todas as Turmas");
                    });
                });
            });
        });
    }

    private void configurarSpinner() {
        List<String> nomesTurmas = new ArrayList<>();
        nomesTurmas.add("Todas as Turmas");
        for (Turma t : mTurmas) {
            nomesTurmas.add(t.getNome());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
                android.R.layout.simple_spinner_item, nomesTurmas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTurmas.setAdapter(adapter);
    }

    private void montarTabela(String filtroTurma) {
        tabelaGeral.removeAllViews();

        if (mGrades.isEmpty()) return;

        Set<String> setHorarios = new HashSet<>();
        for (Grade g : mGrades) setHorarios.add(g.getHorario());
        List<String> horariosUnicos = new ArrayList<>(setHorarios);
        Collections.sort(horariosUnicos);

        boolean mostrarTodas = filtroTurma.equals("Todas as Turmas");

        if (mostrarTodas) {
            // Cabeçalho para visão geral
            TableRow header = new TableRow(this);
            String[] colunas = {"Horário", "Turma", "Segunda", "Terça", "Quarta", "Quinta", "Sexta"};
            for (String col : colunas) {
                header.addView(criarCelula(col, true));
            }
            tabelaGeral.addView(header);

            for (String horario : horariosUnicos) {
                for (Turma turma : mTurmas) {
                    TableRow row = new TableRow(this);
                    row.addView(criarCelula(horario, false));
                    row.addView(criarCelula(turma.getNome(), false));

                    String[] diasBusca = {"Segunda", "Terça", "Quarta", "Quinta", "Sexta"};
                    for (String dia : diasBusca) {
                        Grade gradeAchada = buscarGrade(turma.getId(), dia, horario);
                        adicionarCelulaAula(row, gradeAchada);
                    }
                    tabelaGeral.addView(row);
                }
            }
        } else {
            // Cabeçalho para visão por turma
            TableRow header = new TableRow(this);
            String[] colunas = {"Horário", "Segunda", "Terça", "Quarta", "Quinta", "Sexta"};
            for (String col : colunas) {
                header.addView(criarCelula(col, true));
            }
            tabelaGeral.addView(header);

            int turmaId = -1;
            for (Turma t : mTurmas) {
                if (t.getNome().equals(filtroTurma)) {
                    turmaId = t.getId();
                    break;
                }
            }

            for (String horario : horariosUnicos) {
                TableRow row = new TableRow(this);
                row.addView(criarCelula(horario, false));

                String[] diasBusca = {"Segunda", "Terça", "Quarta", "Quinta", "Sexta"};
                for (String dia : diasBusca) {
                    Grade gradeAchada = buscarGrade(turmaId, dia, horario);
                    adicionarCelulaAula(row, gradeAchada);
                }
                tabelaGeral.addView(row);
            }
        }
    }

    private void adicionarCelulaAula(TableRow row, Grade gradeAchada) {
        if (gradeAchada != null && gradeAchada.getDisciplinaId() != -1) {
            String discNome = "";
            for (Disciplina d : mDisciplinas) if (d.getId() == gradeAchada.getDisciplinaId()) discNome = d.getNome();
            
            String profNome = "";
            for (Professor p : mProfessores) if (p.getId() == gradeAchada.getProfessorId()) profNome = p.getNome();
            
            row.addView(criarCelula(discNome + "\n" + profNome, false));
        } else {
            TextView vago = criarCelula("VAGO", false);
            vago.setTextColor(Color.RED);
            row.addView(vago);
        }
    }

    private TextView criarCelula(String texto, boolean isHeader) {
        TextView tv = new TextView(this);
        tv.setText(texto);
        tv.setGravity(Gravity.CENTER);
        tv.setBackgroundResource(isHeader ? R.drawable.bg_table_header : R.drawable.bg_table_cell);
        if (isHeader) {
            tv.setTextColor(Color.WHITE);
            tv.setTypeface(null, Typeface.BOLD);
        } else {
            tv.setTextColor(Color.BLACK);
        }
        return tv;
    }

    private Grade buscarGrade(int turmaId, String dia, String horario) {
        for (Grade g : mGrades) {
            if (g.getTurmaId() == turmaId && g.getDiaSemana().equals(dia) && g.getHorario().equals(horario)) {
                return g;
            }
        }
        return null;
    }
}
