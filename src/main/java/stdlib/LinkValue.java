package stdlib;

public class LinkValue implements Value {

    private final Object value;

    LinkValue(Object value) {
        this.value = value;
    }

    @Override
    public double asNumber() {
        return 0;
    }

    @Override
    public String asString() {
        return value.toString();
    }

    @Override
    public Object asLink() {
        return value;
    }

    @Override
    public String toString() {
        return asString();
    }
}
