package parser;

import parser.ast.expressions.*;
import parser.ast.statements.*;

import java.util.ArrayList;
import java.util.List;

public final class Parser {

    private static final Token EOF = new Token(TokenType.EOF, "");
    private final List<Token> tokens;
    private int pos;
    private final int size;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        size = tokens.size();
    }

    public Statement parse() {
        final var result = new BlockStatement();

        while (!match(TokenType.EOF)) {
            result.add(statement());
        }

        return result;
    }

    private Statement block(){
        final BlockStatement blockStatement = new BlockStatement();
        consume(TokenType.LBRACE);
        while (!match(TokenType.RBRACE)) {
            blockStatement.add(statement());
        }

        return blockStatement;
    }

    private Statement statementOrBlock(){
        if (get(0).getType() == TokenType.LBRACE){
            return block();
        }

        return statement();
    }

    private Statement statement() {
        if (match(TokenType.PRINT)) {
            return new PrintStatement(expression());
        }

        if (match(TokenType.IF)){
            return ifElse();
        }

        if (match(TokenType.LOOP)){
            return loopStatement();
        }

        if (match(TokenType.BREAK)){
            return new BreakStatement();
        }

        if (match(TokenType.CONTINUE)){
            return new ContinueStatement();
        }

        if (match(TokenType.RETURN)){
            return new ReturnStatement(expression());
        }

        if (match(TokenType.FOR)){
            return forStatement();
        }

        if (match(TokenType.FUNCTION)){
            return functionDeclaration();
        }

        if (get(0).getType() == TokenType.WORD && get(1).getType() == TokenType.LPAREN){
            return new FunctionalStatement(function());
        }

        return assignmentStatement();
    }



    private Statement assignmentStatement() {
        if (get(0).getType() == TokenType.WORD && get(1).getType() == TokenType.EQ) {
            final String variable = consume(TokenType.WORD).getText();
            consume(TokenType.EQ);
            return new AssignmentStatement(variable, expression());
        }

        if (get(0).getType() == TokenType.WORD && get(1).getType() == TokenType.LBRACKET) {
            final String variable = consume(TokenType.WORD).getText();
            consume(TokenType.LBRACKET);
            final Expression index = expression();
            consume(TokenType.RBRACKET);
            consume(TokenType.EQ);
            return new ArrayAssignmentStatement(variable, index, expression());
        }

        throw new RuntimeException("Unknown statement near!");
    }

    private Statement ifElse() {
        final Expression condition = expression();
        final Statement ifStatement = statementOrBlock();
        final Statement elseStatement;

        if (match(TokenType.ELSE)) {
            elseStatement = statementOrBlock();
        } else {
            elseStatement = null;
        }

        return new IfStatement(condition, ifStatement, elseStatement);
    }

    private Statement loopStatement() {
        final var condition = expression();
        final var statement = statementOrBlock();
        return new LoopStatement(condition, statement);
    }

    private Statement forStatement() {
        final var init = assignmentStatement();
        consume(TokenType.SEMI_COLON);
        final var term = expression();
        consume(TokenType.SEMI_COLON);
        final var inc = assignmentStatement();
        final var stat = statementOrBlock();
        return new ForStatement(init, term, inc, stat);
    }


    private FunctionDeclarationStatement functionDeclaration() {
        final var name = consume(TokenType.WORD).getText();
        consume(TokenType.LPAREN);
        final List<String> argNames = new ArrayList<>();
        while (!match(TokenType.RPAREN)){
            argNames.add(consume(TokenType.WORD).getText());
            match(TokenType.COMMA);
        }
        final var instructions = statementOrBlock();
        return new FunctionDeclarationStatement(name, argNames, instructions);
    }


    private FunctionalExpression function() {
        final var name = consume(TokenType.WORD).getText();
        consume(TokenType.LPAREN);
        final var function = new FunctionalExpression(name);
        while (!match(TokenType.RPAREN)){
            function.addArgument(expression());
            match(TokenType.COMMA);
        }
        return function;
    }

    private Expression expression() {
        return logicalOr();
    }

    private Expression logicalOr() {
        Expression result = logicalAnd();

        while (true) {
            if (match(TokenType.BARBAR)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.OR, result, logicalAnd());
                continue;
            }
            break;
        }

        return result;
    }

    private Expression logicalAnd() {
        Expression result = equality();
        while (true) {
            if (match(TokenType.AMPAMP)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.AND, result, equality());
                continue;
            }
            break;
        }
        return result;
    }

    private Expression equality() {
        Expression result = conditional();

        if (match(TokenType.EQEQ)) {
            return new ConditionalExpression(ConditionalExpression.Operator.EQUALS, result, conditional());
        }

        if (match(TokenType.EXCLEQ)) {
            System.out.println("Da");
            return new ConditionalExpression(ConditionalExpression.Operator.NOT_EQUALS, result, conditional());
        }

        return result;
    }

    private Expression conditional() {
        Expression result = additive();
        while (true) {
            if (match(TokenType.LT)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.LT, result, additive());
                continue;
            }

            if (match(TokenType.LTEQ)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.LTEQ, result, additive());
                continue;
            }

            if (match(TokenType.GT)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.GT, result, additive());
                continue;
            }

            if (match(TokenType.GTEQ)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.GTEQ, result, additive());
                continue;
            }

            break;
        }

        return result;
    }

    private Expression additive() {
        Expression result = multiplicative();
        while (true) {
            if (match(TokenType.PLUS)) {
                result = new BinaryExpression('+', result, multiplicative());
                continue;
            }

            if (match(TokenType.MINUS)) {
                result = new BinaryExpression('-', result, multiplicative());
                continue;
            }
            break;
        }

        return result;
    }

    private Expression multiplicative() {
        Expression result = unary();
        while (true) {
            if (match(TokenType.STAR)) {
                result = new BinaryExpression('*', result, unary());
                continue;
            }

            if (match(TokenType.SLASH)) {
                result = new BinaryExpression('/', result, unary());
                continue;
            }
            break;
        }

        return result;
    }

    private Expression unary() {
        if (match(TokenType.MINUS)) {
            return new UnaryExpression('-', primary());
        }

        if (match(TokenType.PLUS)) {
            return primary();
        }

        return primary();
    }

    private Expression primary() {
        final Token current = get(0);
        if (match(TokenType.NUMBER)) {
            return new ValueExpression(Double.parseDouble(current.getText()));
        }

        if (get(0).getType() == TokenType.WORD && get(1).getType() == TokenType.LPAREN){
            return function();
        }

        if (get(0).getType() == TokenType.WORD && get(1).getType() == TokenType.LBRACKET){
            return element();
        }

        if (get(0).getType() == TokenType.LBRACKET){
            return array();
        }

        if (match(TokenType.WORD)) {
            return new VariableExpression(current.getText());
        }

        if (match(TokenType.TEXT)) {
            return new ValueExpression(current.getText());
        }

        if (match(TokenType.LPAREN)) {
            Expression result = expression();
            match(TokenType.RPAREN);
            return result;
        }

        throw new RuntimeException("Unknown expression");

    }

    private Expression array() {
        consume(TokenType.LBRACKET);
        final List<Expression> elements = new ArrayList<>();
        while (!match(TokenType.RBRACKET)){
            elements.add(expression());
            match(TokenType.COMMA);
        }
        return new ArrayExpression(elements);
    }

    private Expression element() {
        final String variable = consume(TokenType.WORD).getText();
        consume(TokenType.LBRACKET);
        final Expression index = expression();
        consume(TokenType.RBRACKET);
        return new ArrayAccessExpression(variable, index);
    }

    private Token consume(TokenType type) {
        final Token current = get(0);
        if (type != current.getType()) {
            throw new RuntimeException("Token " + current + " doesn't match " + type);
        }

        pos++;
        return current;
    }

    private boolean match(TokenType type) {
        final Token current = get(0);
        if (type != current.getType())
            return false;

        pos++;
        return true;
    }

    private Token get(int relativePosition) {
        final int position = pos + relativePosition;

        if (position >= size)
            return EOF;

        return tokens.get(position);
    }
}
