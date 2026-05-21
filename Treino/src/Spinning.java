public class Spinning extends Atividade implements ControladorPresenca {
    private int quantidadeBicicletas;

    public Spinning(String nome, String horario, int duracaoAula, int maxParticipantes, int quantidadeBicicletas) {
        super(nome, horario, duracaoAula, maxParticipantes);
        this.quantidadeBicicletas = quantidadeBicicletas;
    }

    @Override
    public void registrarPresenca(String nomeParticipante) {
        System.out.println("Presença registrada na aula de Spinning para: " + nomeParticipante);
    }
}