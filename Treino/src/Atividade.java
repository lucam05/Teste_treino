public abstract class Atividade {
    protected String nome;
    protected String horario;
    protected int duracaoAula;
    protected int maxParticipantes;
    protected boolean ativa; // Utilizada para a funcionalidade de remoção

    public Atividade(String nome, String horario, int duracaoAula, int maxParticipantes) {
        this.nome = nome;
        this.horario = horario;
        this.duracaoAula = duracaoAula;
        this.maxParticipantes = maxParticipantes;
        this.ativa = true;
    }

    public String getNome() {
        return nome;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    @Override
    public String toString() {
        return nome + " (Horário: " + horario + ", Duração: " + duracaoAula + "min, Máx: " + maxParticipantes + " alunos, Ativa: " + ativa + ")";
    }
}