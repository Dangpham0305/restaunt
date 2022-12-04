package gogitek.restaurentorder.constaint;

public enum Role {

    ADMIN("ADMIN", 0), WAITER("WAITER", 1), CHEF("CHEF", 2);
    private final String type;
    private final Integer value;

    Role(String type, int value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public Integer getValue() {
        return value;
    }
}
