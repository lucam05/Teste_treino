public class PlanoPremium extends Plano {

    public PlanoPremium(String nome) {
        super(nome);
    }

    @Override
    public void associarAtividade(Atividade atividade) {
        // O plano premium aceita qualquer atividade sem restrições
        this.atividadesAssociadas.add(atividade);
    }
}