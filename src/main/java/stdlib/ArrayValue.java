package stdlib;

import java.util.Arrays;

public class ArrayValue implements Value {

    private final Value[] elements;

    public ArrayValue(int size) {
        this.elements = new Value[size];
    }

    ArrayValue(Value[] elements) {
        final var size = elements.length;
        this.elements = new Value[size];
        System.arraycopy(elements, 0, this.elements, 0, size);
    }

    public ArrayValue(ArrayValue array) {
        this(array.elements);
    }

    public Value get(int index) {
        return elements[index];
    }

    public void set(int index, Value value) {
        elements[index] = value;
    }

    @Override
    public double asNumber() {
        throw new RuntimeException("Cannot not cast array to number");
    }

    @Override
    public String asString() {
        return Arrays.toString(elements);
    }

    @Override
    public Object asLink() {
        return elements;
    }

    @Override
    public String toString() {
        return asString();
    }
}
