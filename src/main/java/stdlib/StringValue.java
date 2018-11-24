package stdlib;

public class StringValue implements Value {

    private final String value;

    public StringValue(String value) {
        this.value = value;
    }

    @Override
    public double asDouble() {

        try {
            return Double.parseDouble(value);
        }catch (NumberFormatException e){
            return 0;
        }
    }

    @Override
    public String asString() {
        return value;
    }

    @Override
    public String toString() {
        return asString();
    }
}
