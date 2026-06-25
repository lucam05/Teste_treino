package model;

public enum Especialidade {
    CARDIOLOGIA,
    PEDIATRIA,
    ORTOPEDIA,
    DERMATOLOGIA;

    @Override
    public String toString() {
        String nome = this.name();
        return nome.charAt(0) + nome.substring(1).toLowerCase();
    }
}
