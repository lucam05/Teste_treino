public class Natacao extends Atividade implements Certificavel, ControladorPresenca {
    private double profundidadePiscina;

    public Natacao(String nome, String horario, int duracaoAula, int maxParticipantes, double profundidadePiscina) {
        super(nome, horario, duracaoAula, maxParticipantes);
        this.profundidadePiscina = profundidadePiscina;
    }

    @Override
    public void emitirCertificado(String nomeParticipante) {
        System.out.println("Certificado de Natação emitido para o aluno(a): " + nomeParticipante);
    }

    @Override
    public void registrarPresenca(String nomeParticipante) {
        System.out.println("Presença registrada na aula de Natação para: " + nomeParticipante);
    }
}