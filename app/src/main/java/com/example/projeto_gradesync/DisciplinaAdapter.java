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

public class DisciplinaAdapter extends RecyclerView.Adapter<DisciplinaAdapter.DisciplinaViewHolder> {

    private List<Disciplina> mDisciplinas = new ArrayList<>();

    @NonNull
    @Override
    public DisciplinaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_disciplina, parent, false);
        return new DisciplinaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DisciplinaViewHolder holder, int position) {
        if (mDisciplinas != null && position < mDisciplinas.size()) {
            Disciplina current = mDisciplinas.get(position);
            holder.tvNome.setText(current.getNome());
            
            String cargaFormatada = holder.itemView.getContext().getString(
                    R.string.workload_format, current.getCargaHoraria());
            holder.tvCarga.setText(cargaFormatada);

            // Implementação do Clique para Edição
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), CadastroDisciplinaActivity.class);
                intent.putExtra("DISCIPLINA_ID", current.getId());
                v.getContext().startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDisciplinas != null ? mDisciplinas.size() : 0;
    }

    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.mDisciplinas = disciplinas;
        notifyDataSetChanged();
    }

    public Disciplina getDisciplinaAt(int position) {
        if (mDisciplinas != null && position >= 0 && position < mDisciplinas.size()) {
            return mDisciplinas.get(position);
        }
        return null;
    }

    static class DisciplinaViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNome;
        private final TextView tvCarga;

        public DisciplinaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tv_item_nome_disciplina);
            tvCarga = itemView.findViewById(R.id.tv_item_carga_horaria);
        }
    }
}
