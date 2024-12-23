import lombok.Getter;

@Getter
public enum ChGKTableType {
    BASIC("Basic");

    ChGKTableType(String name) {
        this.name = name;
    }

    private String name;

    public static ChGKTableType getByName(String name) {
        for (ChGKTableType type : ChGKTableType.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }
}
