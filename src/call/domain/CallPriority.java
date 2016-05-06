package call.domain;

/**
 * @author luiz
 * @version 1
 * @since 03/05/16
 */
public enum CallPriority {
    LOW("Baixa"),
	MEDIAN("Média"),
	HIGH("Alta"),
	URGENT("Imediato/urgente");

    private String label;

    CallPriority(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static CallPriority getPriority(int priority) {
        for (CallPriority value:values()) if (value.ordinal() == priority) return value;
        return getPriority(0);
    }
}
