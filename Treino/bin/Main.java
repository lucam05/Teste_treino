package Treino.bin;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Main {

    private static final Repositorio<Consulta> repositorioConsultas = new Repositorio<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final AtomicInteger proximoCodigo = new AtomicInteger(1);

    public static void main(String[] args) {
        criarConsultasIniciais();
        proximoCodigo.set(repositorioConsultas.listarTodos().size() + 1);

        int opcao;
        do {
            exibirMenu();
            opcao = lerOpcao();
            try {
                executarOpcao(opcao);
            } catch (ConsultaNaoEncontradaException | HorarioIndisponivelException | DateTimeParseException | IllegalArgumentException e) {
                System.out.println("\nErro: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("\nOcorreu um erro inesperado. Tente novamente.");
            }
        } while (opcao != 0);

        System.out.println("\nSistema finalizado.");
        scanner.close();
    }

    private static void criarConsultasIniciais() {
        repositorioConsultas.adicionar(new Consulta(proximoCodigo.getAndIncrement(), "Ana Silva", Especialidade.CARDIOLOGIA, LocalDateTime.of(2026, 7, 10, 9, 0), 250.0));
        repositorioConsultas.adicionar(new Consulta(proximoCodigo.getAndIncrement(), "Bruno Costa", Especialidade.ORTOPEDIA, LocalDateTime.of(2026, 7, 11, 14, 30), 300.0));
        repositorioConsultas.adicionar(new Consulta(proximoCodigo.getAndIncrement(), "Carla Dias", Especialidade.PEDIATRIA, LocalDateTime.of(2026, 7, 10, 10, 0), 200.0));
        repositorioConsultas.adicionar(new Consulta(proximoCodigo.getAndIncrement(), "Daniel Farias", Especialidade.DERMATOLOGIA, LocalDateTime.of(2026, 7, 12, 11, 0), 350.0));
        repositorioConsultas.adicionar(new Consulta(proximoCodigo.getAndIncrement(), "Elisa Borges", Especialidade.CARDIOLOGIA, LocalDateTime.of(2026, 7, 10, 9, 30), 250.0));
    }

    private static void exibirMenu() {
        System.out.println("\n--- Sistema de Gerenciamento de Consultas ---");
        System.out.println("1. Cadastrar consulta");
        System.out.println("2. Cancelar consulta");
        System.out.println("3. Reagendar consulta");
        System.out.println("4. Filtrar por especialidade");
        System.out.println("5. Filtrar por valor acima de");
        System.out.println("6. Ordenar consultas por data");
        System.out.println("7. Gerar relatório resumido");
        System.out.println("8. Exibir todas as consultas");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static int lerOpcao() {
        while (!scanner.hasNextInt()) {
            System.out.print("Opção inválida. Digite um número: ");
            scanner.next();
        }
        int opcao = scanner.nextInt();
        scanner.nextLine();
        return opcao;
    }

    private static void executarOpcao(int opcao) throws ConsultaNaoEncontradaException, HorarioIndisponivelException {
        switch (opcao) {
            case 1: cadastrarConsulta(); break;
            case 2: cancelarConsulta(); break;
            case 3: reagendarConsulta(); break;
            case 4: filtrarPorEspecialidade(); break;
            case 5: filtrarPorValor(); break;
            case 6: ordenarPorData(); break;
            case 7: gerarRelatorio(); break;
            case 8: exibirTodasConsultas(); break;
            case 0: break;
            default: System.out.println("\nOpção inválida. Tente novamente.");
        }
    }

    private static void cadastrarConsulta() throws HorarioIndisponivelException {
        System.out.print("Nome do paciente: ");
        String nome = scanner.nextLine();
        System.out.print("Especialidade (CARDIOLOGIA, PEDIATRIA, ORTOPEDIA, DERMATOLOGIA): ");
        Especialidade especialidade = Especialidade.valueOf(scanner.nextLine().toUpperCase());
        System.out.print("Data e hora (dd/MM/yyyy HH:mm): ");
        LocalDateTime data = LocalDateTime.parse(scanner.nextLine(), formatter);
        System.out.print("Valor da consulta: ");
        double valor = scanner.nextDouble();
        scanner.nextLine();

        validarHorario(data);

        Consulta novaConsulta = new Consulta(proximoCodigo.getAndIncrement(), nome, especialidade, data, valor);
        repositorioConsultas.adicionar(novaConsulta);
        System.out.println("\nConsulta cadastrada com sucesso! Código: " + novaConsulta.getCodigo());
    }

    private static void cancelarConsulta() throws ConsultaNaoEncontradaException {
        System.out.print("Digite o código da consulta a ser cancelada: ");
        int codigo = lerOpcao();
        
        Consulta consulta = buscarConsultaPorCodigo(codigo);
        if (repositorioConsultas.remover(consulta)) {
            System.out.println("\nConsulta cancelada com sucesso.");
        } else {
            System.out.println("\nNão foi possível cancelar a consulta.");
        }
    }

    private static void reagendarConsulta() throws ConsultaNaoEncontradaException, HorarioIndisponivelException {
        System.out.print("Digite o código da consulta a ser reagendada: ");
        int codigo = lerOpcao();

        Consulta consulta = buscarConsultaPorCodigo(codigo);

        System.out.print("Digite a nova data e hora (dd/MM/yyyy HH:mm): ");
        LocalDateTime novaData = LocalDateTime.parse(scanner.nextLine(), formatter);

        validarHorario(novaData, consulta.getCodigo());

        consulta.setDataConsulta(novaData);
        System.out.println("\nConsulta reagendada com sucesso.");
    }

    private static void filtrarPorEspecialidade() {
        System.out.print("Digite a especialidade para filtrar: ");
        Especialidade especialidade = Especialidade.valueOf(scanner.nextLine().toUpperCase());

        Predicate<Consulta> porEspecialidade = c -> c.getEspecialidade() == especialidade;
        List<Consulta> filtradas = repositorioConsultas.filtrar(porEspecialidade);

        System.out.println("\n--- Consultas de " + especialidade + " ---");
        exibirListaConsultas(filtradas);
    }

    private static void filtrarPorValor() {
        System.out.print("Filtrar consultas com valor acima de: ");
        double valor = scanner.nextDouble();
        scanner.nextLine();

        Predicate<Consulta> porValor = c -> c.getValorConsulta() > valor;
        List<Consulta> filtradas = repositorioConsultas.filtrar(porValor);

        System.out.println("\n--- Consultas com valor acima de R$" + String.format("%.2f", valor) + " ---");
        exibirListaConsultas(filtradas);
    }

    private static void ordenarPorData() {
        List<Consulta> consultas = repositorioConsultas.listarTodos();
        Comparator<Consulta> porData = Comparator.comparing(Consulta::getDataConsulta);
        consultas.sort(porData);

        System.out.println("\n--- Consultas ordenadas por data ---");
        exibirListaConsultas(consultas);
    }

    private static void gerarRelatorio() {
        System.out.println("\n--- Relatório Resumido de Consultas ---");
        Function<Consulta, String> formatadorRelatorio = c -> String.format("%s - %s - %s",
                c.getNomePaciente(), c.getEspecialidade(), c.getDataConsulta().format(formatter));

        repositorioConsultas.listarTodos().stream()
                .map(formatadorRelatorio)
                .forEach(System.out::println);
    }

    private static void exibirTodasConsultas() {
        System.out.println("\n--- Todas as Consultas Cadastradas ---");
        List<Consulta> todas = repositorioConsultas.listarTodos();
        exibirListaConsultas(todas);
    }

    private static void exibirListaConsultas(List<Consulta> consultas) {
        if (consultas.isEmpty()) {
            System.out.println("Nenhuma consulta encontrada.");
            return;
        }
        Consumer<Consulta> exibidor = c -> System.out.printf("Cód: %d | Paciente: %s | Especialidade: %s | Data: %s | Valor: R$%.2f%n",
                c.getCodigo(), c.getNomePaciente(), c.getEspecialidade(), c.getDataConsulta().format(formatter), c.getValorConsulta());

        consultas.forEach(exibidor);
    }

    private static Consulta buscarConsultaPorCodigo(int codigo) throws ConsultaNaoEncontradaException {
        Predicate<Consulta> porCodigo = c -> c.getCodigo() == codigo;
        return repositorioConsultas.buscar(porCodigo)
                .orElseThrow(() -> new ConsultaNaoEncontradaException("Consulta com código " + codigo + " não encontrada."));
    }

    private static void validarHorario(LocalDateTime data) throws HorarioIndisponivelException {
        validarHorario(data, -1);
    }

    private static void validarHorario(LocalDateTime data, int ignorarCodigoConsulta) throws HorarioIndisponivelException {
        Predicate<Consulta> noMesmoHorario = c -> c.getDataConsulta().equals(data) && c.getCodigo() != ignorarCodigoConsulta;
        if (repositorioConsultas.buscar(noMesmoHorario).isPresent()) {
            throw new HorarioIndisponivelException("Já existe uma consulta agendada para " + data.format(formatter));
        }
    }
}