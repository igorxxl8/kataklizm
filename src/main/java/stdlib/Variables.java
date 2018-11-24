package stdlib;

import java.util.HashMap;
import java.util.Map;

public final class Variables {

    private static final NumberValue ZERO = new NumberValue(0);
    private static Map<String, Value> variables;

    static {
        variables = new HashMap<>();
        variables.put("PI", new NumberValue(Math.PI));
        variables.put("E",  new NumberValue(Math.E));
    }

    public static boolean isExists(String name){
        return variables.containsKey(name);
    }

    public static Value get(String name) {
        if (!isExists(name)){
            return ZERO;
        }

        return variables.get(name);

    }

    public static void set(String name, Value value) {
        variables.put(name, value);
    }

}
