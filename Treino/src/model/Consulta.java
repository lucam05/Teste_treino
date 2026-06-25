package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Consulta {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private int codigo;
    private String nomePaciente;
    private Especialidade especialidade;
    private LocalDateTime dataConsulta;
    private double valorConsulta;

    public Consulta(int codigo, String nomePaciente, Especialidade especialidade,
                    LocalDateTime dataConsulta, double valorConsulta) {
        this.codigo = codigo;
        this.nomePaciente = nomePaciente;
        this.especialidade = especialidade;
        this.dataConsulta = dataConsulta;
        this.valorConsulta = valorConsulta;
    }

    // Getters e Setters
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
    }

    public LocalDateTime getDataConsulta() {
        return dataConsulta;
    }

    public void setDataConsulta(LocalDateTime dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    public double getValorConsulta() {
        return valorConsulta;
    }

    public void setValorConsulta(double valorConsulta) {
        this.valorConsulta = valorConsulta;
    }

    public String getDataFormatada() {
        return dataConsulta.format(FORMATTER);
    }

    @Override
    public String toString() {
        return String.format(
            "┌─────────────────────────────────────────\n" +
            "│ Código:       %d\n" +
            "│ Paciente:     %s\n" +
            "│ Especialidade:%s\n" +
            "│ Data/Hora:    %s\n" +
            "│ Valor:        R$ %.2f\n" +
            "└─────────────────────────────────────────",
            codigo, nomePaciente, especialidade, getDataFormatada(), valorConsulta
        );
    }
}
