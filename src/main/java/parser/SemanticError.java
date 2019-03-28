package parser;

import org.w3c.dom.Document;
import parser.ast.expressions.CustomArrayList;

import java.util.List;

public class SemanticError {
    private Document _tree;

    public List<String> getErrors() {
        return errors;
    }

    private List<String> errors = new CustomArrayList<>();

    public SemanticError(Document tree) {
        _tree = tree;
    }

    public void Analyze() {
        // 1 check access to array element
        CheckArrayAccessStatements();
        // 2 check division on zero
        CheckZeroDivision();
        // 3 check operations with different types
        CheckOperationsWithDifferentTypes();
    }

    private void CheckArrayAccessStatements() {
        final var arrayAssignmentStatement = _tree.getElementsByTagName("ArrayAssignmentStatement");
        final var length = arrayAssignmentStatement.getLength();
        for (int i = 0; i < length; i++) {
            final var childNodes = arrayAssignmentStatement.item(i).getChildNodes().item(0).getChildNodes().item(0);
            final var attrs = childNodes.getAttributes();
            final var name = attrs.item(1).getNodeValue();
            final var line = attrs.item(0).getNodeValue();
            final var str = attrs.item(2).getNodeValue();
            final var indexes = childNodes.getChildNodes().item(0).getChildNodes();
            for (int j = 0; j < indexes.getLength(); j++) {
                final var value = indexes.item(j).getAttributes().item(1).getTextContent();
                try {
                    final var v = Integer.parseInt(value);
                } catch (Exception ex) {

                    errors.add(String.format("%s:%s: SemanticError: Array index must be integer or string which can convert to integer: %s[%s]!\n", line, str, name, value));
                }
            }
        }
    }


    private void CheckZeroDivision() {
        final var binaryOperations = _tree.getElementsByTagName("BinaryExpression");
        for (int i = 0; i < binaryOperations.getLength(); i++) {
            final var binaryOperation = binaryOperations.item(i).getChildNodes();
            final var name = binaryOperation.item(0).getAttributes().item(1).getNodeValue();
            final var operationAttrs = binaryOperation.item(1).getAttributes();
            final var operation = operationAttrs.item(1).getNodeValue();
            final var line = operationAttrs.item(0).getNodeValue();
            final var str = operationAttrs.item(2).getNodeValue();
            final var value = binaryOperation.item(2).getAttributes().item(1).getNodeValue();
            if (operation.equals("/")) {
                if (value.equals("0.0")) {
                    errors.add(String.format("%s:%s: SemanticError: Unable to divide on zero: %s %s %s!\n", line, str, name, operation, value));
                }
            }
        }
    }

    private void CheckOperationsWithDifferentTypes() {
        final var binaryOperations = _tree.getElementsByTagName("BinaryExpression");
        for (int i = 0; i < binaryOperations.getLength(); i++) {
            final var binaryOperation = binaryOperations.item(i).getChildNodes();
            final var name1 = binaryOperation.item(0).getAttributes().item(1).getNodeValue();
            final var name2 = binaryOperation.item(2).getAttributes().item(1).getNodeValue();
            final var operationAttrs = binaryOperation.item(1).getAttributes();
            final var operation = operationAttrs.item(1).getNodeValue();
            final var line = operationAttrs.item(0).getNodeValue();
            final var str = operationAttrs.item(2).getNodeValue();
            final var type1 = binaryOperation.item(0).getAttributes().item(0).getNodeValue();
            final var type2 = binaryOperation.item(2).getAttributes().item(0).getNodeValue();
            if (!type1.equals(type2)) {
                errors.add(String.format("%s:%s: SemanticError: Unable to perform operation with operands which have different types: %s(%s) %s %s(%s)!\n", line, str, name1, type1, operation, name2, type2));
            }
        }
    }
}
