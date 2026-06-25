package service;

import exception.ConsultaNaoEncontradaException;
import exception.HorarioIndisponivelException;
import interfaces.ConsultaFuncoes;
import model.Consulta;
import model.Especialidade;
import repository.Repositorio;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class ConsultaService {

    private final Repositorio<Consulta> repositorio;
    private int proximoCodigo;

    public ConsultaService() {
        this.repositorio = new Repositorio<>();
        this.proximoCodigo = 1;
    }

    // ─── 1. Cadastrar consulta ──────────────────────────────────────────────────

    public Consulta cadastrar(String nomePaciente, Especialidade especialidade,
                              LocalDateTime dataConsulta, double valorConsulta)
            throws HorarioIndisponivelException {

        verificarDisponibilidade(dataConsulta, -1);

        Consulta consulta = new Consulta(
                proximoCodigo++, nomePaciente, especialidade, dataConsulta, valorConsulta
        );
        repositorio.adicionar(consulta);
        return consulta;
    }

    // ─── 2. Remover consulta ────────────────────────────────────────────────────

    public void remover(int codigo) throws ConsultaNaoEncontradaException {
        Consulta consulta = buscarPorCodigo(codigo);
        repositorio.remover(consulta);
    }

    // ─── 3. Filtrar por especialidade (usa Predicate) ───────────────────────────

    public List<Consulta> filtrarPorEspecialidade(Especialidade especialidade) {
        return repositorio.buscar(ConsultaFuncoes.porEspecialidade(especialidade));
    }

    // ─── 4. Filtrar por valor acima de (usa Predicate) ─────────────────────────

    public List<Consulta> filtrarPorValorAcimaDe(double valor) {
        return repositorio.buscar(ConsultaFuncoes.valorAcimaDe(valor));
    }

    // ─── 5. Reagendar consulta ──────────────────────────────────────────────────

    public void reagendar(int codigo, LocalDateTime novaData)
            throws ConsultaNaoEncontradaException, HorarioIndisponivelException {

        Consulta consulta = buscarPorCodigo(codigo);
        verificarDisponibilidade(novaData, codigo);
        consulta.setDataConsulta(novaData);
    }

    // ─── 6. Ordenar por data (usa Comparator) ───────────────────────────────────

    public List<Consulta> ordenarPorData() {
        List<Consulta> lista = repositorio.listar();
        lista.sort(ConsultaFuncoes.POR_DATA);
        return lista;
    }

    // ─── 7. Gerar relatório resumido (usa Function) ─────────────────────────────

    public void gerarRelatorio() {
        List<Consulta> lista = repositorio.listar();
        lista.sort(ConsultaFuncoes.POR_DATA);

        System.out.println("\n╔══════════════════════════════════════════════════");
        System.out.println("║         RELATÓRIO RESUMIDO DE CONSULTAS");
        System.out.println("╠══════════════════════════════════════════════════");

        if (lista.isEmpty()) {
            System.out.println("║  Nenhuma consulta cadastrada.");
        } else {
            for (Consulta c : lista) {
                // Uso obrigatório da Function
                String resumo = ConsultaFuncoes.PARA_RESUMO.apply(c);
                System.out.println("║  [" + c.getCodigo() + "] " + resumo);
            }
        }

        System.out.println("╠══════════════════════════════════════════════════");
        System.out.printf("║  Total de consultas: %d%n", lista.size());
        double totalValor = lista.stream().mapToDouble(Consulta::getValorConsulta).sum();
        System.out.printf("║  Valor total:        R$ %.2f%n", totalValor);
        System.out.println("╚══════════════════════════════════════════════════\n");
    }

    // ─── 8. Cancelar consulta (alias de remover com feedback) ───────────────────

    public void cancelar(int codigo) throws ConsultaNaoEncontradaException {
        Consulta consulta = buscarPorCodigo(codigo);
        repositorio.remover(consulta);
        System.out.println("Consulta de " + consulta.getNomePaciente() + " cancelada com sucesso.");
    }

    // ─── 9. Exibir todas as consultas (usa Consumer) ────────────────────────────

    public void exibirTodas() {
        List<Consulta> lista = repositorio.listar();
        lista.sort(ConsultaFuncoes.POR_DATA);

        if (lista.isEmpty()) {
            System.out.println("\n  Nenhuma consulta cadastrada.\n");
            return;
        }

        System.out.println("\n=== TODAS AS CONSULTAS (" + lista.size() + ") ===\n");
        // Uso obrigatório do Consumer
        lista.forEach(ConsultaFuncoes.EXIBIR);
    }

    // ─── Ordenar por nome (usa Comparator) ─────────────────────────────────────

    public List<Consulta> ordenarPorNome() {
        List<Consulta> lista = repositorio.listar();
        lista.sort(ConsultaFuncoes.POR_NOME);
        return lista;
    }

    // ─── Helpers internos ───────────────────────────────────────────────────────

    public Consulta buscarPorCodigo(int codigo) throws ConsultaNaoEncontradaException {
        return repositorio.buscarUm(ConsultaFuncoes.porCodigo(codigo))
                .orElseThrow(() -> new ConsultaNaoEncontradaException(codigo));
    }

    private void verificarDisponibilidade(LocalDateTime dataHora, int codigoIgnorar)
            throws HorarioIndisponivelException {

        List<Consulta> conflitos = repositorio.buscar(ConsultaFuncoes.porDataHora(dataHora));
        for (Consulta c : conflitos) {
            if (c.getCodigo() != codigoIgnorar) {
                throw new HorarioIndisponivelException(dataHora);
            }
        }
    }

    public boolean repositorioVazio() {
        return repositorio.isEmpty();
    }
}
