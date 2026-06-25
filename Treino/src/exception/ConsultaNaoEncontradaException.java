package exception;

public class ConsultaNaoEncontradaException extends Exception {

    public ConsultaNaoEncontradaException(int codigo) {
        super("Consulta com código " + codigo + " não encontrada.");
    }

    public ConsultaNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}
