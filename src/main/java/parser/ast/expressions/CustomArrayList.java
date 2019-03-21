package parser.ast.expressions;

import java.util.ArrayList;

public class CustomArrayList<T> extends ArrayList<T> {
    public CustomArrayList(){
        super();
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (T o : this) {
            sb.append(o);
        }
        return sb.toString();
    }
}
