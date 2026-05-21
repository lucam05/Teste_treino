public class Musculacao extends Atividade {
    private int quantidadeAparelhos;

    public Musculacao(String nome, String horario, int duracaoAula, int maxParticipantes, int quantidadeAparelhos) {
        super(nome, horario, duracaoAula, maxParticipantes);
        this.quantidadeAparelhos = quantidadeAparelhos;
    }
}