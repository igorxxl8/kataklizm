package stdlib;

import parser.ast.expressions.ArrayAccessExpression;

import java.util.HashMap;
import java.util.Map;

public final class Functions {


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
                System.out.print(arg.asString());
            }
            return NumberValue.ZERO;
        });

        functions.put("array", ArrayValue::new);

        functions.put("init_array", new Function() {
            @Override
            public Value execute(Value... args) {
                return createArray(args, 0);
            }

            private ArrayValue createArray(Value[] args, int index) {
                final int size = (int)args[index].asNumber();
                final int last = args.length - 1;
                ArrayValue array = new ArrayValue(size);
                if (index == last) {
                    for (int i = 0; i < size; i++) {
                        array.set(i, NumberValue.ZERO);
                    }
                }else if (index < last) {
                    for (int i = 0; i < size; i++) {
                        array.set(i, createArray(args, index+1));
                    }
                }
                return array;
            }
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
