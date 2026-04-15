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

public class ListaProfessoresActivity extends AppCompatActivity {

    private ProfessorViewModel mProfessorViewModel;
    private ProfessorAdapter mAdapter;
    private TextView tvEmptyState;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_professores);

        // Inicializa Views
        recyclerView = findViewById(R.id.rv_professores);
        tvEmptyState = findViewById(R.id.tv_empty_state);
        
        // Configura o RecyclerView
        mAdapter = new ProfessorAdapter();
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializa o ViewModel e observa os dados com lógica de Empty State
        mProfessorViewModel = new ViewModelProvider(this).get(ProfessorViewModel.class);
        mProfessorViewModel.getAllProfessores().observe(this, professores -> {
            if (professores == null || professores.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                tvEmptyState.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                tvEmptyState.setVisibility(View.GONE);
                mAdapter.setProfessores(professores);
            }
        });

        // Implementação do Swipe para Excluir
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Professor professor = mAdapter.getProfessorAt(position);
                
                if (professor != null) {
                    mProfessorViewModel.delete(professor);
                    Toast.makeText(ListaProfessoresActivity.this, 
                            "Professor " + professor.getNome() + " excluído", 
                            Toast.LENGTH_SHORT).show();
                }
            }
        }).attachToRecyclerView(recyclerView);

        // Configura o FAB para abrir a tela de cadastro
        ExtendedFloatingActionButton fab = findViewById(R.id.fab_add_professor);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(ListaProfessoresActivity.this, CadastroProfessorActivity.class);
            startActivity(intent);
        });
    }
}
