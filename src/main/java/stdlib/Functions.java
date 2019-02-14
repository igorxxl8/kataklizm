package stdlib;

import parser.ast.expressions.ArrayAccessExpression;

import javax.swing.*;
import java.awt.*;
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
            for (var arg : args) {
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
                final var size = (int) args[index].asNumber();
                final var last = args.length - 1;
                var array = new ArrayValue(size);
                if (index == last) {
                    for (var i = 0; i < size; i++) {
                        array.set(i, NumberValue.ZERO);
                    }
                } else if (index < last) {
                    for (var i = 0; i < size; i++) {
                        array.set(i, createArray(args, index + 1));
                    }
                }
                return array;
            }
        });

        functions.put("window", args -> {
            var window = new KataklizmWindow(args[0].asString());
            window.setBounds((int) args[1].asNumber(), (int) args[2].asNumber(), (int) args[3].asNumber(), (int) args[4].asNumber());
            window.setVisible(true);
            //window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            return new LinkValue(window);
        });

        functions.put("ClearWindow", args -> {
            final var window = (KataklizmWindow) args[0].asLink();
            Integer[] arr = {0, 0, 0};
            window.drawRect(0, 0, 2000, 2000, (Value[]) new LinkValue(arr).asLink(), "fill");
            return NumberValue.ZERO;
        });

        functions.put("paint", args -> {

            final var window = (KataklizmWindow) args[0].asLink();
            System.out.println(window.getGraphics());
            final var paintingType = args[1].asString();
            if (paintingType.equals("Line")) {
                window.drawLine(
                        (int) args[2].asNumber(),
                        (int) args[3].asNumber(),
                        (int) args[4].asNumber(),
                        (int) args[5].asNumber(),
                        (Value[]) args[6].asLink()
                );
                return NumberValue.ZERO;
            }

            if (paintingType.equals("Rect")) {
                window.drawRect(
                        (int) args[2].asNumber(),
                        (int) args[3].asNumber(),
                        (int) args[4].asNumber(),
                        (int) args[5].asNumber(),
                        (Value[]) args[6].asLink(),
                        args[7].asString()
                );
                return NumberValue.ZERO;
            }

            if (paintingType.equals("Circle")) {
                window.drawCircle(
                        (int) args[2].asNumber(),
                        (int) args[3].asNumber(),
                        (int) args[4].asNumber(),
                        (int) args[5].asNumber(),
                        (Value[]) args[6].asLink(),
                        args[7].asString()
                );
                return NumberValue.ZERO;
            }

            if (paintingType.equals("Polygon")) {
                window.drawPolygon(
                        (Value[]) args[2].asLink(),
                        (Value[]) args[3].asLink(),
                        (Value[]) args[4].asLink()
                );
                return NumberValue.ZERO;
            }

            throw new RuntimeException(String.format("Unknown painting type - %s", paintingType));

        });

    }

    private static boolean isExists(String name) {
        return functions.containsKey(name);
    }

    public static Function get(String name) {
        if (!isExists(name)) {
            throw new RuntimeException("Unknown function " + name);
        }

        return functions.get(name);

    }

    public static void set(String name, Function function) {
        functions.put(name, function);
    }

}
