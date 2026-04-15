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

public class TurmaAdapter extends RecyclerView.Adapter<TurmaAdapter.TurmaViewHolder> {

    private List<Turma> mTurmas = new ArrayList<>();

    @NonNull
    @Override
    public TurmaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_turma, parent, false);
        return new TurmaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TurmaViewHolder holder, int position) {
        if (mTurmas != null && position < mTurmas.size()) {
            Turma current = mTurmas.get(position);
            holder.tvNome.setText(current.getNome());

            // Implementação do Clique para Edição
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), CadastroTurmaActivity.class);
                intent.putExtra("TURMA_ID", current.getId());
                v.getContext().startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return mTurmas != null ? mTurmas.size() : 0;
    }

    public void setTurmas(List<Turma> turmas) {
        this.mTurmas = turmas;
        notifyDataSetChanged();
    }

    public Turma getTurmaAt(int position) {
        if (mTurmas != null && position >= 0 && position < mTurmas.size()) {
            return mTurmas.get(position);
        }
        return null;
    }

    static class TurmaViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNome;

        public TurmaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tv_item_nome_turma);
        }
    }
}
