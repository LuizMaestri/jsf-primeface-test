package call.domain;

/**
 * @author luiz
 * @version 1
 * @since 03/05/16
 */
public enum CallType {
    INCIDENT("Incidente"),
    SOLICITATION("Solicitação"),
    REQUIREMENT("Requisito"),
    NEW_IMPLEMENTATION("Nova Implementação"),
    BUG("Bug"),
    IMPROVEMENT("Melhoria"),
    ASSIGNMENT("Tarefa"),
    TEST("Teste de software"),
    MANAGEMENT("Gerencial");

    private String label;

    CallType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static CallType getType(int type) {
        for (CallType value: values()) if (value.ordinal() == type) return value;
        return getType(0);
    }
}
