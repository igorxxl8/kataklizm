package stdlib;

import java.util.HashMap;
import java.util.Map;

public final class Functions {

    private static final NumberValue ZERO = new NumberValue(0);
    private static Map<String, Function> functions;

    static {
        functions = new HashMap<>();
        functions.put("sin", args -> {
            if (args.length != 1) {
                throw new RuntimeException("One arg expected!");
            }

            return new NumberValue(Math.sin(args[0].asNumber()));
        });

        functions.put("cos", args -> {
            if (args.length != 1) {
                throw new RuntimeException("One arg expected!");
            }
            return new NumberValue(Math.cos(args[0].asNumber()));
        });

        functions.put("stdout", args -> {
            for (var arg : args){
                System.out.println(arg.asString());
            }
            return ZERO;
        });
    }

    public static boolean isExists(String name){
        return functions.containsKey(name);
    }

    public static Function get(String name) {
        if (!isExists(name)){
            throw new RuntimeException("Unknown function " + name);
        }

        return functions.get(name);

    }

    public static void set(String name, Function function) {
        functions.put(name, function);
    }

}
