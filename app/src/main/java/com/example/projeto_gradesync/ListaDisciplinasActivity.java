package com.example.projeto_gradesync;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class ListaDisciplinasActivity extends AppCompatActivity {

    private DisciplinaViewModel mDisciplinaViewModel;
    private DisciplinaAdapter mAdapter;
    private TextView tvEmptyState;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_disciplinas);

        // Inicializa Views
        recyclerView = findViewById(R.id.rv_disciplinas);
        tvEmptyState = findViewById(R.id.tv_empty_state);

        // Configura o RecyclerView
        mAdapter = new DisciplinaAdapter();
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializa o ViewModel e observa os dados com lógica de Empty State
        mDisciplinaViewModel = new ViewModelProvider(this).get(DisciplinaViewModel.class);
        mDisciplinaViewModel.getAllDisciplinas().observe(this, disciplinas -> {
            if (disciplinas == null || disciplinas.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                tvEmptyState.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                tvEmptyState.setVisibility(View.GONE);
                mAdapter.setDisciplinas(disciplinas);
            }
        });

        // Swipe to Delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Disciplina disciplina = mAdapter.getDisciplinaAt(position);
                if (disciplina != null) {
                    mDisciplinaViewModel.delete(disciplina);
                    Toast.makeText(ListaDisciplinasActivity.this, "Disciplina excluída", Toast.LENGTH_SHORT).show();
                }
            }
        }).attachToRecyclerView(recyclerView);

        // FAB para novo cadastro
        ExtendedFloatingActionButton fab = findViewById(R.id.fab_add_disciplina);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(ListaDisciplinasActivity.this, CadastroDisciplinaActivity.class);
            startActivity(intent);
        });
    }
}
