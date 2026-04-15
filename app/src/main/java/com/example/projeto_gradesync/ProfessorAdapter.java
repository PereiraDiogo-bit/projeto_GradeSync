package com.example.projeto_gradesync;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProfessorAdapter extends RecyclerView.Adapter<ProfessorAdapter.ProfessorViewHolder> {

    private List<Professor> mProfessores = new ArrayList<>();

    @NonNull
    @Override
    public ProfessorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_professor, parent, false);
        return new ProfessorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfessorViewHolder holder, int position) {
        if (mProfessores != null && position < mProfessores.size()) {
            Professor current = mProfessores.get(position);
            holder.tvNome.setText(current.getNome());
            holder.tvDias.setText(current.getDiasDisponiveis());

            // Implementação do Clique para Edição
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), CadastroProfessorActivity.class);
                intent.putExtra("PROFESSOR_ID", current.getId());
                v.getContext().startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return mProfessores != null ? mProfessores.size() : 0;
    }

    public void setProfessores(List<Professor> professores) {
        this.mProfessores = professores;
        notifyDataSetChanged();
    }

    /**
     * Retorna o professor em uma posição específica.
     * Útil para operações de deslizar (swipe) para excluir.
     */
    public Professor getProfessorAt(int position) {
        if (mProfessores != null && position >= 0 && position < mProfessores.size()) {
            return mProfessores.get(position);
        }
        return null;
    }

    static class ProfessorViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNome;
        private final TextView tvDias;

        public ProfessorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tv_item_nome);
            tvDias = itemView.findViewById(R.id.tv_item_dias);
        }
    }
}
