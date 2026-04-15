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

public class ListaTurmasActivity extends AppCompatActivity {

    private TurmaViewModel mTurmaViewModel;
    private TurmaAdapter mAdapter;
    private TextView tvEmptyState;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_turmas);

        // Inicializa Views
        recyclerView = findViewById(R.id.rv_turmas);
        tvEmptyState = findViewById(R.id.tv_empty_state);

        // Configura o RecyclerView
        mAdapter = new TurmaAdapter();
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializa o ViewModel e observa os dados com lógica de Empty State
        mTurmaViewModel = new ViewModelProvider(this).get(TurmaViewModel.class);
        mTurmaViewModel.getAllTurmas().observe(this, turmas -> {
            if (turmas == null || turmas.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                tvEmptyState.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                tvEmptyState.setVisibility(View.GONE);
                mAdapter.setTurmas(turmas);
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
                Turma turma = mAdapter.getTurmaAt(position);
                if (turma != null) {
                    mTurmaViewModel.delete(turma);
                    Toast.makeText(ListaTurmasActivity.this, R.string.class_deleted, Toast.LENGTH_SHORT).show();
                }
            }
        }).attachToRecyclerView(recyclerView);

        // FAB para novo cadastro
        ExtendedFloatingActionButton fab = findViewById(R.id.fab_add_turma);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(ListaTurmasActivity.this, CadastroTurmaActivity.class);
            startActivity(intent);
        });
    }
}
