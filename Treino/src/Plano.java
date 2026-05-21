import java.util.ArrayList;
import java.util.List;

public abstract class Plano {
    protected String nome;
    protected List<Atividade> atividadesAssociadas;
    protected boolean ativo;

    public Plano(String nome) {
        this.nome = nome;
        this.atividadesAssociadas = new ArrayList<>();
        this.ativo = true;
    }

    public abstract void associarAtividade(Atividade atividade);

    public String getNome() {
        return nome;
    }

    public boolean isAtivo() {
        return ativo;
    }

    @Override
    public String toString() {
        return "Plano: " + nome + " (Ativo: " + ativo + ") - Total de atividades associadas: " + atividadesAssociadas.size();
    }
}