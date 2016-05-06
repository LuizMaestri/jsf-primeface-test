package call.domain;

/**
 * @author luiz
 * @version 1
 * @since 03/05/16
 */
public enum CallStatus {
	REGISTER("Aguardando"),
	OPEN("Em andamento"),
	CLOSE("Fechado"),
	CANCEL("Cancelado");

	private String label;

	CallStatus(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public static CallStatus getStatus(int status){
        for (CallStatus value : values()) if (value.ordinal() == status) return value;
        return getStatus(0);
	}

}
