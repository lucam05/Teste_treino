package interfaces;

import model.Consulta;
import model.Especialidade;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Centraliza todas as interfaces funcionais obrigatórias do sistema.
 */
public class ConsultaFuncoes {

    // ─── PREDICATES ────────────────────────────────────────────────────────────

    /** Filtra consultas por especialidade */
    public static Predicate<Consulta> porEspecialidade(Especialidade especialidade) {
        return consulta -> consulta.getEspecialidade() == especialidade;
    }

    /** Filtra consultas com valor acima do informado */
    public static Predicate<Consulta> valorAcimaDe(double valorMinimo) {
        return consulta -> consulta.getValorConsulta() > valorMinimo;
    }

    /** Filtra consulta por código */
    public static Predicate<Consulta> porCodigo(int codigo) {
        return consulta -> consulta.getCodigo() == codigo;
    }

    /** Filtra consulta por data e hora (para verificar disponibilidade de horário) */
    public static Predicate<Consulta> porDataHora(LocalDateTime dataHora) {
        return consulta -> consulta.getDataConsulta().equals(dataHora);
    }

    // ─── COMPARATORS ───────────────────────────────────────────────────────────

    /** Ordena consultas por nome do paciente (A → Z) */
    public static final Comparator<Consulta> POR_NOME =
            Comparator.comparing(Consulta::getNomePaciente);

    /** Ordena consultas por data da consulta (mais antiga → mais recente) */
    public static final Comparator<Consulta> POR_DATA =
            Comparator.comparing(Consulta::getDataConsulta);

    // ─── FUNCTION ──────────────────────────────────────────────────────────────

    /** Converte uma consulta para String resumida: Nome - Especialidade - Data HH:mm */
    public static final Function<Consulta, String> PARA_RESUMO = consulta ->
            String.format("%s - %s - %s",
                    consulta.getNomePaciente(),
                    consulta.getEspecialidade(),
                    consulta.getDataConsulta()
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

    // ─── CONSUMER ──────────────────────────────────────────────────────────────

    /** Exibe os dados completos de uma consulta no console */
    public static final Consumer<Consulta> EXIBIR = consulta ->
            System.out.println(consulta.toString());
}
