package parser

import org.w3c.dom.Document
import parser.ast.expressions.CustomArrayList

class SemanticError(private val _tree: Document) {

    val errors = CustomArrayList<String>()

    fun getErrors(): List<String> {
        return errors
    }

    fun analyze() {
        // 1 check access to array element
        CheckArrayAccessStatements()
        // 2 check division on zero
        checkZeroDivision()
        // 3 check operations with different types
        checkOperationsWithDifferentTypes()
    }

    private fun CheckArrayAccessStatements() {
        val arrayAssignmentStatement = _tree.getElementsByTagName("ArrayAssignmentStatement")
        val length = arrayAssignmentStatement.length
        for (i in 0 until length) {
            val childNodes = arrayAssignmentStatement.item(i).childNodes.item(0).childNodes.item(0)
            val attrs = childNodes.attributes
            val name = attrs.item(1).nodeValue
            val line = attrs.item(0).nodeValue
            val str = attrs.item(2).nodeValue
            val indexes = childNodes.childNodes.item(0).childNodes
            for (j in 0 until indexes.length) {
                val value = indexes.item(j).attributes.item(1).textContent
                try {
                    Integer.parseInt(value)
                } catch (ex: Exception) {

                    errors.add(String.format("%s:%s: SemanticError: Array index must be integer or string which can convert to integer: %s[%s]!\n", line, str, name, value))
                }

            }
        }
    }


    private fun checkZeroDivision() {
        val binaryOperations = _tree.getElementsByTagName("BinaryExpression")
        for (i in 0 until binaryOperations.length) {
            val binaryOperation = binaryOperations.item(i).childNodes
            val name = binaryOperation.item(0).attributes.item(1).nodeValue
            val operationAttrs = binaryOperation.item(1).attributes
            val operation = operationAttrs.item(1).nodeValue
            val line = operationAttrs.item(0).nodeValue
            val str = operationAttrs.item(2).nodeValue
            val value = binaryOperation.item(2).attributes.item(1).nodeValue
            when (operation) {
                "/" -> if (value == "0.0") {
                    errors.add(String.format("%s:%s: SemanticError: Unable to divide on zero: %s %s %s!\n", line, str, name, operation, value))
                }
            }
        }
    }

    private fun checkOperationsWithDifferentTypes() {
        val binaryOperations = _tree.getElementsByTagName("BinaryExpression")
        for (i in 0 until binaryOperations.length) {
            val binaryOperation = binaryOperations.item(i).childNodes
            val name1 = binaryOperation.item(0).attributes.item(1).nodeValue
            val name2 = binaryOperation.item(2).attributes.item(1).nodeValue
            val operationAttrs = binaryOperation.item(1).attributes
            val operation = operationAttrs.item(1).nodeValue
            val line = operationAttrs.item(0).nodeValue
            val str = operationAttrs.item(2).nodeValue
            val type1 = binaryOperation.item(0).attributes.item(0).nodeValue
            val type2 = binaryOperation.item(2).attributes.item(0).nodeValue
            if (type1 != type2) {
                errors.add(String.format("%s:%s: SemanticError: Unable to perform operation with operands which have different types: %s(%s) %s %s(%s)!\n", line, str, name1, type1, operation, name2, type2))
            }
        }
    }
}
