public class PlanoBasico extends Plano implements ControladorPresenca {

    public PlanoBasico(String nome) {
        super(nome);
    }

    @Override
    public void associarAtividade(Atividade atividade) {
        // Neste escopo simplificado e sem validações profundas de negócio,
        // estamos apenas associando a atividade repassada.
        this.atividadesAssociadas.add(atividade);
    }

    @Override
    public void registrarPresenca(String nomeParticipante) {
        System.out.println("Controle de acesso [Catraca]: Presença diária registrada no Plano Básico para " + nomeParticipante + ".");
    }
}