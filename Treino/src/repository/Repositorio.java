package repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Repositorio<T> {

    private final ArrayList<T> elementos;

    public Repositorio() {
        this.elementos = new ArrayList<>();
    }

    public void adicionar(T elemento) {
        elementos.add(elemento);
    }

    public boolean remover(T elemento) {
        return elementos.remove(elemento);
    }

    public List<T> listar() {
        return new ArrayList<>(elementos);
    }

    public List<T> buscar(Predicate<T> criterio) {
        List<T> resultado = new ArrayList<>();
        for (T elemento : elementos) {
            if (criterio.test(elemento)) {
                resultado.add(elemento);
            }
        }
        return resultado;
    }

    public Optional<T> buscarUm(Predicate<T> criterio) {
        for (T elemento : elementos) {
            if (criterio.test(elemento)) {
                return Optional.of(elemento);
            }
        }
        return Optional.empty();
    }

    public int tamanho() {
        return elementos.size();
    }

    public boolean isEmpty() {
        return elementos.isEmpty();
    }
}
