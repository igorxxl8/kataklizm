package parser.ast.expressions

import java.util.ArrayList

class CustomArrayList<T> : ArrayList<T>() {

    override fun toString(): String {
        val sb = StringBuilder()
        for (o in this) {
            sb.append(o)
        }
        return sb.toString()
    }
}
