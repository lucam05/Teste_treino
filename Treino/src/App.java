import java.util.ArrayList;
import java.util.List;

public class App {

    private static List<Atividade> atividades = new ArrayList<>();
    private static List<Plano> planos = new ArrayList<>();

    public static void main(String[] args) {
        // Criar 3 atividades ao iniciar
        Atividade musculacao = new Musculacao("Musculação Livre", "08:00", 90, 30, 20);
        Atividade natacao = new Natacao("Natação Iniciante", "10:00", 45, 10, 2.0);
        Atividade spinning = new Spinning("Spinning Avançado", "18:00", 50, 15, 15);
        
        atividades.add(musculacao);
        atividades.add(natacao);
        atividades.add(spinning);

        // Criar 2 planos ao iniciar
        Plano basico = new PlanoBasico("Plano Básico");
        Plano premium = new PlanoPremium("Plano Premium");
        
        planos.add(basico);
        planos.add(premium);

        System.out.println("--- 1. Associar atividades a um plano ---");
        basico.associarAtividade(musculacao); // Exemplo de uso para o básico
        premium.associarAtividade(natacao);
        premium.associarAtividade(spinning);
        System.out.println("Atividades associadas aos planos com sucesso.");

        System.out.println("\n--- 2. Exibir todas as atividades cadastradas ---");
        atividades.forEach(a -> System.out.println(a.toString()));

        System.out.println("\n--- 3. Exibir todos os planos cadastrados ---");
        planos.forEach(p -> System.out.println(p.toString()));

        System.out.println("\n--- 4. Exibir apenas planos ativos ---");
        // Utilizando a API de Streams para filtrar os que estiverem ativos
        planos.stream().filter(Plano::isAtivo).forEach(p -> System.out.println(p.toString()));

        System.out.println("\n--- 5. Emitir certificado para objetos Certificáveis ---");
        emitirCertificadoPara(natacao, "Luca");

        System.out.println("\n--- 6. Registrar presença em ControladorPresenca ---");
        // Aqui usamos o polimorfismo, pois tanto uma atividade quanto um plano podem controlar presença!
        registrarPresencaEm((ControladorPresenca) spinning, "Luca");
        registrarPresencaEm((ControladorPresenca) basico, "Luca");

        System.out.println("\n--- 7. Remover atividades inativas do sistema ---");
        musculacao.setAtiva(false); // Simulando a inativação da musculação
        System.out.println("Total de atividades antes da remoção: " + atividades.size());
        atividades.removeIf(a -> !a.isAtiva());
        System.out.println("Total de atividades após remoção das inativas: " + atividades.size());
        atividades.forEach(a -> System.out.println(a.toString()));
    }

    // Métodos utilitários que executam validação de implementação de Interface (Polimorfismo)
    private static void emitirCertificadoPara(Object obj, String nomeParticipante) {
        if (obj instanceof Certificavel) {
            ((Certificavel) obj).emitirCertificado(nomeParticipante);
        } else {
            System.out.println("Este objeto não é capaz de emitir certificados.");
        }
    }

    private static void registrarPresencaEm(ControladorPresenca controlador, String nomeParticipante) {
        controlador.registrarPresenca(nomeParticipante);
    }
}
