import exception.HorarioIndisponivelException;
import model.Especialidade;
import service.ConsultaService;
import ui.Menu;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ConsultaService service = new ConsultaService();
        Scanner scanner = new Scanner(System.in);
        Menu menu = new Menu(service, scanner);

        // Cria automaticamente 5 consultas ao iniciar o sistema
        popularConsultasIniciais(service);

        System.out.println("  [✓] Sistema iniciado com 5 consultas pré-cadastradas.\n");

        int opcao = -1;
        do {
            menu.exibir();
            try {
                opcao = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                opcao = -1;
            }
            menu.processarOpcao(opcao);
        } while (opcao != 0);

        scanner.close();
    }

    /**
     * Cria automaticamente 5 consultas ao iniciar o sistema,
     * conforme solicitado no enunciado.
     */
    private static void popularConsultasIniciais(ConsultaService service) {
        try {
            service.cadastrar(
                    "João Silva",
                    Especialidade.CARDIOLOGIA,
                    LocalDateTime.of(2026, 7, 10, 9, 0),
                    350.00
            );

            service.cadastrar(
                    "Maria Oliveira",
                    Especialidade.PEDIATRIA,
                    LocalDateTime.of(2026, 7, 10, 10, 30),
                    200.00
            );

            service.cadastrar(
                    "Carlos Souza",
                    Especialidade.ORTOPEDIA,
                    LocalDateTime.of(2026, 7, 11, 14, 0),
                    420.00
            );

            service.cadastrar(
                    "Ana Lima",
                    Especialidade.DERMATOLOGIA,
                    LocalDateTime.of(2026, 7, 12, 8, 0),
                    280.00
            );

            service.cadastrar(
                    "Pedro Costa",
                    Especialidade.CARDIOLOGIA,
                    LocalDateTime.of(2026, 7, 12, 11, 0),
                    500.00
            );

        } catch (HorarioIndisponivelException e) {
            System.out.println("[!] Erro ao popular dados iniciais: " + e.getMessage());
        }
    }
}
