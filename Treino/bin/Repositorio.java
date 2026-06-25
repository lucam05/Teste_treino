package Treino.bin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Repositorio<T> {

    private final List<T> elementos = new ArrayList<>();

    public void adicionar(T elemento) {
        elementos.add(elemento);
    }

    public boolean remover(T elemento) {
        return elementos.remove(elemento);
    }

    public List<T> listarTodos() {
        return new ArrayList<>(elementos);
    }

    public Optional<T> buscar(Predicate<T> criterio) {
        return elementos.stream().filter(criterio).findFirst();
    }

    public List<T> filtrar(Predicate<T> criterio) {
        return elementos.stream().filter(criterio).collect(Collectors.toList());
    }
}