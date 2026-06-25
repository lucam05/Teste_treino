package ui;

import exception.ConsultaNaoEncontradaException;
import exception.HorarioIndisponivelException;
import interfaces.ConsultaFuncoes;
import model.Consulta;
import model.Especialidade;
import service.ConsultaService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final ConsultaService service;
    private final Scanner scanner;

    public Menu(ConsultaService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    public void exibir() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║       SISTEMA DE GERENCIAMENTO DE CONSULTAS      ║");
        System.out.println("╠══════════════════════════════════════════════════╣");
        System.out.println("║  1. Cadastrar consulta                           ║");
        System.out.println("║  2. Remover consulta                             ║");
        System.out.println("║  3. Filtrar por especialidade                    ║");
        System.out.println("║  4. Filtrar por valor acima de...                ║");
        System.out.println("║  5. Reagendar consulta                           ║");
        System.out.println("║  6. Ordenar por data                             ║");
        System.out.println("║  7. Gerar relatório resumido                     ║");
        System.out.println("║  8. Cancelar consulta                            ║");
        System.out.println("║  9. Exibir todas as consultas                    ║");
        System.out.println("║  0. Sair                                         ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.print("  Escolha uma opção: ");
    }

    public void processarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> cadastrarConsulta();
            case 2 -> removerConsulta();
            case 3 -> filtrarPorEspecialidade();
            case 4 -> filtrarPorValor();
            case 5 -> reagendarConsulta();
            case 6 -> ordenarPorData();
            case 7 -> service.gerarRelatorio();
            case 8 -> cancelarConsulta();
            case 9 -> service.exibirTodas();
            case 0 -> System.out.println("\n  Sistema encerrado. Até logo!\n");
            default -> System.out.println("\n  [!] Opção inválida. Tente novamente.\n");
        }
    }

    // ─── 1. Cadastrar ──────────────────────────────────────────────────────────

    private void cadastrarConsulta() {
        System.out.println("\n--- CADASTRAR CONSULTA ---");

        System.out.print("  Nome do paciente: ");
        String nome = scanner.nextLine().trim();
        if (nome.isEmpty()) {
            System.out.println("  [!] Nome não pode ser vazio.");
            return;
        }

        Especialidade especialidade = lerEspecialidade();
        if (especialidade == null) return;

        LocalDateTime dataHora = lerDataHora("  Data e hora (dd/MM/yyyy HH:mm): ");
        if (dataHora == null) return;

        double valor = lerValor("  Valor da consulta (R$): ");
        if (valor < 0) return;

        try {
            Consulta nova = service.cadastrar(nome, especialidade, dataHora, valor);
            System.out.println("\n  [✓] Consulta cadastrada com sucesso! Código: " + nova.getCodigo());
        } catch (HorarioIndisponivelException e) {
            System.out.println("\n  [!] " + e.getMessage());
        }
    }

    // ─── 2. Remover ────────────────────────────────────────────────────────────

    private void removerConsulta() {
        System.out.println("\n--- REMOVER CONSULTA ---");
        int codigo = lerCodigo();
        if (codigo < 0) return;

        try {
            service.remover(codigo);
            System.out.println("  [✓] Consulta removida com sucesso!");
        } catch (ConsultaNaoEncontradaException e) {
            System.out.println("  [!] " + e.getMessage());
        }
    }

    // ─── 3. Filtrar por especialidade ──────────────────────────────────────────

    private void filtrarPorEspecialidade() {
        System.out.println("\n--- FILTRAR POR ESPECIALIDADE ---");

        Especialidade especialidade = lerEspecialidade();
        if (especialidade == null) return;

        // Uso obrigatório do Predicate via service
        List<Consulta> resultado = service.filtrarPorEspecialidade(especialidade);

        if (resultado.isEmpty()) {
            System.out.println("  Nenhuma consulta encontrada para " + especialidade + ".");
        } else {
            System.out.println("\n  Consultas de " + especialidade + " (" + resultado.size() + "):\n");
            // Uso obrigatório do Consumer
            resultado.forEach(ConsultaFuncoes.EXIBIR);
        }
    }

    // ─── 4. Filtrar por valor ──────────────────────────────────────────────────

    private void filtrarPorValor() {
        System.out.println("\n--- FILTRAR POR VALOR ---");
        double valor = lerValor("  Valor mínimo (R$): ");
        if (valor < 0) return;

        List<Consulta> resultado = service.filtrarPorValorAcimaDe(valor);

        if (resultado.isEmpty()) {
            System.out.printf("  Nenhuma consulta com valor acima de R$ %.2f.%n", valor);
        } else {
            System.out.printf("%n  Consultas acima de R$ %.2f (%d):%n%n", valor, resultado.size());
            resultado.forEach(ConsultaFuncoes.EXIBIR);
        }
    }

    // ─── 5. Reagendar ──────────────────────────────────────────────────────────

    private void reagendarConsulta() {
        System.out.println("\n--- REAGENDAR CONSULTA ---");
        int codigo = lerCodigo();
        if (codigo < 0) return;

        try {
            Consulta atual = service.buscarPorCodigo(codigo);
            System.out.println("  Data atual: " + atual.getDataFormatada());
        } catch (ConsultaNaoEncontradaException e) {
            System.out.println("  [!] " + e.getMessage());
            return;
        }

        LocalDateTime novaData = lerDataHora("  Nova data e hora (dd/MM/yyyy HH:mm): ");
        if (novaData == null) return;

        try {
            service.reagendar(codigo, novaData);
            System.out.println("  [✓] Consulta reagendada para " + novaData.format(FORMATTER));
        } catch (ConsultaNaoEncontradaException | HorarioIndisponivelException e) {
            System.out.println("  [!] " + e.getMessage());
        }
    }

    // ─── 6. Ordenar por data ───────────────────────────────────────────────────

    private void ordenarPorData() {
        System.out.println("\n--- CONSULTAS ORDENADAS POR DATA ---\n");
        List<Consulta> lista = service.ordenarPorData();

        if (lista.isEmpty()) {
            System.out.println("  Nenhuma consulta cadastrada.");
            return;
        }

        // Uso obrigatório do Consumer
        lista.forEach(ConsultaFuncoes.EXIBIR);
        System.out.println("  Total: " + lista.size() + " consulta(s).\n");
    }

    // ─── 8. Cancelar ───────────────────────────────────────────────────────────

    private void cancelarConsulta() {
        System.out.println("\n--- CANCELAR CONSULTA ---");
        int codigo = lerCodigo();
        if (codigo < 0) return;

        System.out.print("  Confirmar cancelamento? (s/n): ");
        String confirmacao = scanner.nextLine().trim().toLowerCase();
        if (!confirmacao.equals("s")) {
            System.out.println("  Cancelamento abortado.");
            return;
        }

        try {
            service.cancelar(codigo);
        } catch (ConsultaNaoEncontradaException e) {
            System.out.println("  [!] " + e.getMessage());
        }
    }

    // ─── Helpers de leitura ────────────────────────────────────────────────────

    private Especialidade lerEspecialidade() {
        System.out.println("  Especialidades disponíveis:");
        Especialidade[] valores = Especialidade.values();
        for (int i = 0; i < valores.length; i++) {
            System.out.println("    " + (i + 1) + ". " + valores[i]);
        }
        System.out.print("  Escolha (1-" + valores.length + "): ");

        try {
            int escolha = Integer.parseInt(scanner.nextLine().trim());
            if (escolha < 1 || escolha > valores.length) {
                System.out.println("  [!] Opção inválida.");
                return null;
            }
            return valores[escolha - 1];
        } catch (NumberFormatException e) {
            System.out.println("  [!] Entrada inválida.");
            return null;
        }
    }

    private LocalDateTime lerDataHora(String prompt) {
        System.out.print(prompt);
        try {
            return LocalDateTime.parse(scanner.nextLine().trim(), FORMATTER);
        } catch (DateTimeParseException e) {
            System.out.println("  [!] Formato de data inválido. Use dd/MM/yyyy HH:mm");
            return null;
        }
    }

    private double lerValor(String prompt) {
        System.out.print(prompt);
        try {
            double valor = Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
            if (valor < 0) {
                System.out.println("  [!] Valor não pode ser negativo.");
                return -1;
            }
            return valor;
        } catch (NumberFormatException e) {
            System.out.println("  [!] Valor inválido.");
            return -1;
        }
    }

    private int lerCodigo() {
        System.out.print("  Código da consulta: ");
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("  [!] Código inválido.");
            return -1;
        }
    }
}
