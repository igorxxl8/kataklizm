package stdlib;

import org.jetbrains.annotations.NotNull;

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

    @NotNull
    @Override
    public String asString() {
        return Arrays.toString(elements);
    }

    @NotNull
    @Override
    public Object asLink() {
        return elements;
    }

    @Override
    public String toString() {
        return asString();
    }
}
