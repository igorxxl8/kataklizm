package parser;

import parser.ast.expressions.*;
import parser.ast.statements.*;
import stdlib.Variables;

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

    private Statement block() {
        final var blockStatement = new BlockStatement();
        consume(TokenType.LBRACE);
        while (!match(TokenType.RBRACE)) {
            blockStatement.add(statement());
        }

        return blockStatement;
    }

    private Statement statementOrBlock() {
        if (get(0).getType() == TokenType.LBRACE) {
            return block();
        }

        return statement();
    }

    private Statement statement() {
        if (match(KeywordTokenType.OUT)) {
            return new PrintStatement(expression());
        }

        if (match(KeywordTokenType.IN)) {
            return inputStatement();
        }

        if (match(KeywordTokenType.IF)) {
            return ifElse();
        }

        if (match(KeywordTokenType.MATCH)) {
            return matchStatement();
        }

        if (match(KeywordTokenType.LOOP)) {
            return loopStatement();
        }

        if (match(KeywordTokenType.BREAK)) {
            return new BreakStatement();
        }

        if (match(KeywordTokenType.CONTINUE)) {
            return new ContinueStatement();
        }

        if (match(KeywordTokenType.RET)) {
            return new ReturnStatement(expression());
        }

        if (match(KeywordTokenType.FOR)) {
            return forStatement();
        }

        if (match(KeywordTokenType.FUNC)) {
            return functionDeclaration();
        }

        if (match(KeywordTokenType.CASE)) {
            return caseStatement();
        }

        if (get(0).getType() == TokenType.WORD && get(1).getType() == TokenType.LPAREN) {
            return new FunctionalStatement(function());
        }


        return assignmentStatement();
    }

    private Statement inputStatement() {
        if (get(0).getType() == TokenType.WORD) {
            final var variable = consume(TokenType.WORD).getText();
            return new InputStatement(variable);
        }

        throw new RuntimeException("Unknown statement in input statement");
    }

    private Statement assignmentStatement() {
        if (get(0).getType() == TokenType.WORD && get(1).getType() == TokenType.EQ) {
            final var variable = consume(TokenType.WORD).getText();
            consume(TokenType.EQ);
            return new AssignmentStatement(variable, expression());
        }

        if (get(0).getType() == TokenType.WORD && get(1).getType() == TokenType.LBRACKET) {
            var array = element();
            consume(TokenType.EQ);
            return new ArrayAssignmentStatement(array, expression());
        }

        if (get(0).getType() == TokenType.LBRACE) {
            return statementOrBlock();
        }

        throw new RuntimeException("Unknown statement near!");
    }

    private Statement caseStatement(){
        final var conditionVariant = expression();
        consume(TokenType.ARROW);
        final var body = statementOrBlock();
        return new CaseStatement(conditionVariant, body);
    }

    private Statement matchStatement() {
        final var condition = expression();
        final var matchBody = statementOrBlock();
        return new MatchStatement(condition, matchBody);
    }



    private Statement ifElse() {
        final var condition = expression();
        final var ifStatement = statementOrBlock();
        final Statement elseStatement;

        if (match(KeywordTokenType.ELSE)) {
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
        while (!match(TokenType.RPAREN)) {
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
        while (!match(TokenType.RPAREN)) {
            function.addArgument(expression());
            match(TokenType.COMMA);
        }
        return function;
    }

    private Expression expression() {
        return logicalOr();
    }

    private Expression logicalOr() {
        var result = logicalAnd();

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
        var result = equality();
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
        var result = conditional();

        if (match(ComparisonTokenType.EQEQ)) {
            return new ConditionalExpression(ConditionalExpression.Operator.EQUALS, result, conditional());
        }

        if (match(ComparisonTokenType.EXCLEQ)) {
            System.out.println("Da");
            return new ConditionalExpression(ConditionalExpression.Operator.NOT_EQUALS, result, conditional());
        }

        return result;
    }

    private Expression conditional() {
        var result = additive();
        while (true) {
            if (match(ComparisonTokenType.LT)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.LT, result, additive());
                continue;
            }

            if (match(ComparisonTokenType.LTEQ)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.LTEQ, result, additive());
                continue;
            }

            if (match(ComparisonTokenType.GT)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.GT, result, additive());
                continue;
            }

            if (match(ComparisonTokenType.GTEQ)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.GTEQ, result, additive());
                continue;
            }

            break;
        }

        return result;
    }

    private Expression additive() {
        var result = multiplicative();
        while (true) {
            if (match(ArithmeticTokenType.PLUS)) {
                result = new BinaryExpression('+', result, multiplicative());
                continue;
            }

            if (match(ArithmeticTokenType.MINUS)) {
                result = new BinaryExpression('-', result, multiplicative());
                continue;
            }
            break;
        }

        return result;
    }

    private Expression multiplicative() {
        var result = unary();
        while (true) {
            if (match(ArithmeticTokenType.STAR)) {
                result = new BinaryExpression('*', result, unary());
                continue;
            }

            if (match(ArithmeticTokenType.SLASH)) {
                result = new BinaryExpression('/', result, unary());
                continue;
            }
            break;
        }

        return result;
    }

    private Expression unary() {
        if (match(ArithmeticTokenType.MINUS)) {
            return new UnaryExpression('-', primary());
        }

        if (match(ArithmeticTokenType.PLUS)) {
            return primary();
        }

        return primary();
    }

    private Expression primary() {
        final var current = get(0);
        if (match(TokenType.NUMBER)) {
            return new ValueExpression(Double.parseDouble(current.getText()));
        }

        if (get(0).getType() == TokenType.WORD && get(1).getType() == TokenType.LPAREN) {
            return function();
        }

        if (get(0).getType() == TokenType.WORD && get(1).getType() == TokenType.LBRACKET) {
            return element();
        }

        if (get(0).getType() == TokenType.LBRACKET) {
            return array();
        }

        if (match(TokenType.WORD)) {
            return new VariableExpression(current.getText());
        }

        if (match(TokenType.TEXT)) {
            return new ValueExpression(current.getText());
        }

        if (match(TokenType.LPAREN)) {
            var result = expression();
            match(TokenType.RPAREN);
            return result;
        }

        throw new RuntimeException(String.format("Unknown expression %s", current));

    }

    private Expression array() {
        consume(TokenType.LBRACKET);
        final List<Expression> elements = new ArrayList<>();
        while (!match(TokenType.RBRACKET)) {
            elements.add(expression());
            match(TokenType.COMMA);
        }
        return new ArrayExpression(elements);
    }

    private ArrayAccessExpression element() {
        final var variable = consume(TokenType.WORD).getText();
        List<Expression> indexes = new ArrayList<>();
        do {
            consume(TokenType.LBRACKET);
            indexes.add(expression());
            consume(TokenType.RBRACKET);
        } while (get(0).getType() == TokenType.LBRACKET);

        return new ArrayAccessExpression(variable, indexes);
    }

    private Token consume(TokenType type) {
        final var current = get(0);
        if (type != current.getType()) {
            throw new RuntimeException("Token " + current + " doesn't match " + type);
        }
        pos++;
        return current;
    }

    private boolean match(ITokenType type) {
        final var current = get(0);
        if (type != current.getType())
            return false;

        pos++;
        return true;
    }

    private Token get(int relativePosition) {
        final var position = pos + relativePosition;

        if (position >= size)
            return EOF;

        return tokens.get(position);
    }
}
