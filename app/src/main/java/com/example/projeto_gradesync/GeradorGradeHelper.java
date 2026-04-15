package com.example.projeto_gradesync;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Motor de geração de grade horária refinado com embaralhamento (Shuffle)
 * para garantir uma distribuição orgânica e justa de aulas e professores.
 */
public class GeradorGradeHelper {

    public static List<Grade> gerarGrade(List<Turma> turmas, List<Disciplina> disciplinas, List<Professor> professores,
                                         String horaInicio, int duracaoAula, int qtdAntesRecreio, int duracaoRecreio, int totalAulas) {

        List<Grade> gradeCompleta = new ArrayList<>();
        Set<String> professoresOcupados = new HashSet<>();
        Map<Integer, Integer> aulasLecionadasProfessor = new HashMap<>();

        // Array padronizado conforme seleção via interface (Botões/Checkboxes)
        String[] diasDaSemana = {"Segunda", "Terça", "Quarta", "Quinta", "Sexta"};
        List<String> horariosGerados = calcularHorarios(horaInicio, duracaoAula, qtdAntesRecreio, duracaoRecreio, totalAulas);

        for (Turma turma : turmas) {
            Map<Integer, Integer> cargasRestantes = new HashMap<>();
            for (Disciplina d : disciplinas) {
                cargasRestantes.put(d.getId(), d.getCargaHoraria());
            }

            Map<String, Integer> aulasDiariasMateria = new HashMap<>();

            for (String diaAtual : diasDaSemana) {
                for (String horarioAtual : horariosGerados) {
                    boolean aulaAlocada = false;

                    // 1. EMBARALHAR DISCIPLINAS: Garante que a ordem de avaliação das matérias seja aleatória a cada horário
                    Collections.shuffle(disciplinas);

                    for (Disciplina disciplina : disciplinas) {
                        String chaveDiaMateria = turma.getId() + "-" + diaAtual + "-" + disciplina.getId();
                        int aulasHoje = aulasDiariasMateria.getOrDefault(chaveDiaMateria, 0);
                        if (aulasHoje >= 2) continue;

                        Integer objCarga = cargasRestantes.get(disciplina.getId());
                        int carga = (objCarga != null) ? objCarga : 0;

                        if (carga > 0) {
                            // 2. EMBARALHAR PROFESSORES: Garante chances iguais para todos os especialistas aptos
                            Collections.shuffle(professores);

                            for (Professor p : professores) {
                                String chaveConflito = p.getId() + "-" + diaAtual + "-" + horarioAtual;

                                Integer objAulas = aulasLecionadasProfessor.get(p.getId());
                                int totalAulasDadas = (objAulas != null) ? objAulas : 0;

                                boolean ensinaMateria = p.getDisciplinaId() == disciplina.getId();
                                // Busca EXATA no dia disponível, eliminando lógicas defensivas
                                boolean disponivelHoje = p.getDiasDisponiveis() != null && p.getDiasDisponiveis().contains(diaAtual);
                                boolean horarioLivre = !professoresOcupados.contains(chaveConflito);
                                boolean cargaDisponivel = totalAulasDadas < p.getCargaHoraria();
                                boolean aceitaTurma = p.getTurmasPreferencia() == null || p.getTurmasPreferencia().isEmpty() || p.getTurmasPreferencia().contains(turma.getNome());

                                if (ensinaMateria && disponivelHoje && horarioLivre && cargaDisponivel && aceitaTurma) {

                                    gradeCompleta.add(new Grade(turma.getId(), disciplina.getId(), p.getId(), diaAtual, horarioAtual));

                                    professoresOcupados.add(chaveConflito);
                                    cargasRestantes.put(disciplina.getId(), carga - 1);
                                    aulasLecionadasProfessor.put(p.getId(), totalAulasDadas + 1);
                                    aulasDiariasMateria.put(chaveDiaMateria, aulasHoje + 1);

                                    aulaAlocada = true;
                                    break;
                                }
                            }
                            if (aulaAlocada) break;
                        }
                    }

                    if (!aulaAlocada) {
                        gradeCompleta.add(new Grade(turma.getId(), -1, -1, diaAtual, horarioAtual));
                    }
                }
            }
        }
        return gradeCompleta;
    }

    private static List<String> calcularHorarios(String horaInicio, int duracaoAula, int qtdAntesRecreio, int duracaoRecreio, int totalAulas) {
        List<String> lista = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Calendar cal = Calendar.getInstance();

        try {
            Date dataParsed = sdf.parse(horaInicio);
            if (dataParsed != null) {
                cal.setTime(dataParsed);
            } else {
                throw new ParseException("Falha ao analisar a hora de início", 0);
            }
        } catch (ParseException e) {
            cal.set(Calendar.HOUR_OF_DAY, 7);
            cal.set(Calendar.MINUTE, 0);
        }

        for (int i = 0; i < totalAulas; i++) {
            if (i == qtdAntesRecreio) {
                cal.add(Calendar.MINUTE, duracaoRecreio);
            }
            String inicio = sdf.format(cal.getTime());
            cal.add(Calendar.MINUTE, duracaoAula);
            String fim = sdf.format(cal.getTime());
            lista.add(inicio + " - " + fim);
        }
        return lista;
    }
}
