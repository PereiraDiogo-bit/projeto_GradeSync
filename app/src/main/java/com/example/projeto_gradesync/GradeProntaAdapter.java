package com.example.projeto_gradesync;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GradeProntaAdapter extends RecyclerView.Adapter<GradeProntaAdapter.GradeViewHolder> {

    private List<Grade> mGrades = new ArrayList<>();
    private List<Turma> mTurmas;
    private List<Disciplina> mDisciplinas;
    private List<Professor> mProfessores;

    public GradeProntaAdapter(List<Turma> turmas, List<Disciplina> disciplinas, List<Professor> professores) {
        this.mTurmas = turmas;
        this.mDisciplinas = disciplinas;
        this.mProfessores = professores;
    }

    @NonNull
    @Override
    public GradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grade_pronta, parent, false);
        return new GradeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GradeViewHolder holder, int position) {
        Grade current = mGrades.get(position);
        
        holder.tvDiaHora.setText(current.getDiaSemana() + " - " + current.getHorario());
        
        // Busca nomes pelos IDs
        String nomeTurma = "Turma " + current.getTurmaId();
        if (mTurmas != null) {
            for (Turma t : mTurmas) if (t.getId() == current.getTurmaId()) nomeTurma = t.getNome();
        }

        String nomeDisciplina = "Disciplina " + current.getDisciplinaId();
        if (mDisciplinas != null) {
            for (Disciplina d : mDisciplinas) if (d.getId() == current.getDisciplinaId()) nomeDisciplina = d.getNome();
        }

        String nomeProfessor = "Professor " + current.getProfessorId();
        if (mProfessores != null) {
            for (Professor p : mProfessores) if (p.getId() == current.getProfessorId()) nomeProfessor = p.getNome();
        }

        holder.tvTurma.setText(nomeTurma);
        holder.tvDisciplina.setText(nomeDisciplina);
        holder.tvProfessor.setText(nomeProfessor);
    }

    @Override
    public int getItemCount() {
        return mGrades.size();
    }

    public void setGrades(List<Grade> grades) {
        this.mGrades = grades;
        notifyDataSetChanged();
    }

    static class GradeViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDiaHora, tvTurma, tvDisciplina, tvProfessor;

        public GradeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDiaHora = itemView.findViewById(R.id.tv_item_dia_hora);
            tvTurma = itemView.findViewById(R.id.tv_item_turma);
            tvDisciplina = itemView.findViewById(R.id.tv_item_disciplina);
            tvProfessor = itemView.findViewById(R.id.tv_item_professor);
        }
    }
}
