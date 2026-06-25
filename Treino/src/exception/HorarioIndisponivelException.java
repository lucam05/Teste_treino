package exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HorarioIndisponivelException extends Exception {

    public HorarioIndisponivelException(LocalDateTime dataHora) {
        super("Horário " + dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                + " já está ocupado por outra consulta.");
    }

    public HorarioIndisponivelException(String mensagem) {
        super(mensagem);
    }
}
