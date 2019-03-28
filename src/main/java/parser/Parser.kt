package parser

import parser.ast.expressions.*
import parser.ast.statements.*

class Parser(private val tokens: List<Token>) {
    private var pos: Int = 0
    private val size: Int = tokens.size
    var errors: MutableList<String> = CustomArrayList()

    fun parse(): Statement {
        val result = BlockStatement()

        while (!match(TokenType.EOF)) result.add(statement())

        return result
    }

    private fun block(): Statement {
        val blockStatement = BlockStatement()
        consume(TokenType.LBRACE, "block")
        while (!match(TokenType.RBRACE)) blockStatement.add(statement())

        return blockStatement
    }

    private fun statementOrBlock(): Statement = when {
        get(0).type === TokenType.LBRACE -> block()
        else -> statement()
    }

    private fun statement(): Statement {
        when {
            match(KeywordTokenType.OUT) -> return PrintStatement(expression())
            match(KeywordTokenType.IN) -> return inputStatement()
            match(KeywordTokenType.IF) -> return ifElse()
            match(KeywordTokenType.MATCH) -> return matchStatement()
            match(KeywordTokenType.LOOP) -> return loopStatement()
            match(KeywordTokenType.BREAK) -> return BreakStatement()
            match(KeywordTokenType.CONTINUE) -> return ContinueStatement()
            match(KeywordTokenType.RET) -> return ReturnStatement(expression())
            match(KeywordTokenType.FOR) -> return forStatement()
            match(KeywordTokenType.FUNC) -> return functionDeclaration()
            match(KeywordTokenType.CASE) -> return caseStatement()
            else -> return when {
                get(0).type === TokenType.WORD && get(1).type === TokenType.LPAREN -> FunctionalStatement(function())
                else -> assignmentStatement()
            }
        }


    }

    private fun inputStatement(): Statement {
        if (get(0).type === TokenType.WORD) {
            val variable = consume(TokenType.WORD, "input statement")!!.text
            return InputStatement(variable)
        }

        throw RuntimeException("Unknown statement in input statement")
    }

    private fun assignmentStatement(): Statement {
        val tok = get(0)

        if (tok.type === TokenType.WORD && get(1).type === TokenType.EQ) {
            val variable = consume(TokenType.WORD, "assignment statement")!!.text
            consume(TokenType.EQ, "assignment statement")
            return AssignmentStatement(variable, expression())
        }

        if (tok.type === TokenType.WORD && get(1).type === TokenType.LBRACKET) {
            val array = element()
            consume(TokenType.EQ, "assignment statement")
            return ArrayAssignmentStatement(array, expression())
        }

        when {
            tok.type === TokenType.WORD -> if (get(1).type === TokenType.RPAREN) {
                errors.add(String.format("%d:%d: Function \"%s\" doesn't have open paren\n", tok.posfile, tok.posstr, tok.text))
                match(TokenType.WORD)
                match(TokenType.RPAREN)
                return ErrorStatement()
            }
        }

        if (tok.type === TokenType.NUMBER && get(1).type === TokenType.EQ) {
            errors.add(String.format("%d:%d: Unable to assign value to value\n", tok.posfile, tok.posstr))
            match(TokenType.NUMBER)
            match(TokenType.EQ)
            match(TokenType.NUMBER)
            return ErrorStatement()
        }

        if (get(0).type === TokenType.LBRACE) {
            return statementOrBlock()
        }

        throw RuntimeException("Unknown statement near!")
    }

    private fun caseStatement(): Statement {
        val conditionVariant = expression()
        consume(TokenType.ARROW, "case statement")
        val body = statementOrBlock()
        return CaseStatement(conditionVariant, body)
    }

    private fun matchStatement(): Statement {
        val condition = expression()
        val matchBody = statementOrBlock()
        return MatchStatement(condition, matchBody)
    }


    private fun ifElse(): Statement {
        val condition = expression()
        val ifStatement = statementOrBlock()
        val elseStatement: Statement?

        when {
            match(KeywordTokenType.ELSE) -> elseStatement = statementOrBlock()
            else -> elseStatement = null
        }

        return IfStatement(condition, ifStatement, elseStatement)
    }

    private fun loopStatement(): Statement {
        val condition = expression()
        val statement = statementOrBlock()
        return LoopStatement(condition, statement)
    }

    private fun forStatement(): Statement {
        val init = assignmentStatement()
        consume(TokenType.SEMI_COLON, "for statement")
        val term = expression()
        consume(TokenType.SEMI_COLON, "for statement")
        val inc = assignmentStatement()
        val stat = statementOrBlock()
        return ForStatement(init, term, inc, stat)
    }


    private fun functionDeclaration(): FunctionDeclarationStatement {
        val name = consume(TokenType.WORD, "function declaration")!!.text
        consume(TokenType.LPAREN, "function declaration")
        val argNames = CustomArrayList<String>()
        while (!match(TokenType.RPAREN)) {
            argNames.add(consume(TokenType.WORD, "function declaration")!!.text)
            match(TokenType.COMMA)
        }
        val instructions = statementOrBlock()
        return FunctionDeclarationStatement(name, argNames, instructions)
    }


    private fun function(): FunctionalExpression {
        val name = consume(TokenType.WORD, "function expression")!!.text
        consume(TokenType.LPAREN, "function expression")
        val function = FunctionalExpression(name)
        while (!match(TokenType.RPAREN)) {
            val tok = get(0)
            if (tok.type is KeywordTokenType) {
                errors.add(String.format("%d:%d: Function \"%s\" doesn't have close paren\n", tok.posfile, tok.posstr, name))
                break
            }
            function.addArgument(expression())
            match(TokenType.COMMA)
        }
        return function
    }

    private fun expression(): Expression {
        return logicalOr()
    }

    private fun logicalOr(): Expression {
        var result = logicalAnd()

        while (true) {
            if (match(TokenType.BARBAR)) {
                result = ConditionalExpression(ConditionalExpression.Operator.OR, result, logicalAnd())
                continue
            }
            break
        }

        return result
    }

    private fun logicalAnd(): Expression {
        var result = equality()
        while (true) {
            if (match(TokenType.AMPAMP)) {
                result = ConditionalExpression(ConditionalExpression.Operator.AND, result, equality())
                continue
            }
            break
        }
        return result
    }

    private fun equality(): Expression {
        val result = conditional()

        return when {
            match(ComparisonTokenType.EQEQ) -> ConditionalExpression(ConditionalExpression.Operator.EQUALS, result, conditional())
            match(ComparisonTokenType.EXCLEQ) -> {
                println("Da")
                ConditionalExpression(ConditionalExpression.Operator.NOT_EQUALS, result, conditional())
            }
            else -> result
        }

    }

    private fun conditional(): Expression {
        var result = additive()
        while (true) {
            if (match(ComparisonTokenType.LT)) {
                result = ConditionalExpression(ConditionalExpression.Operator.LT, result, additive())
                continue
            }

            if (match(ComparisonTokenType.LTEQ)) {
                result = ConditionalExpression(ConditionalExpression.Operator.LTEQ, result, additive())
                continue
            }

            if (match(ComparisonTokenType.GT)) {
                result = ConditionalExpression(ConditionalExpression.Operator.GT, result, additive())
                continue
            }

            if (match(ComparisonTokenType.GTEQ)) {
                result = ConditionalExpression(ConditionalExpression.Operator.GTEQ, result, additive())
                continue
            }

            break
        }

        return result
    }

    private fun additive(): Expression {
        var result = multiplicative()
        while (true) {
            val token = get(0)
            if (match(ArithmeticTokenType.PLUS)) {
                result = BinaryExpression('+', result, multiplicative(), token.posstr, token.posfile)
                continue
            }

            if (match(ArithmeticTokenType.MINUS)) {
                result = BinaryExpression('-', result, multiplicative(), token.posstr, token.posfile)
                continue
            }
            break
        }

        return result
    }

    private fun multiplicative(): Expression {
        var result = unary()
        while (true) {
            val token = get(0)
            if (match(ArithmeticTokenType.STAR)) {
                result = BinaryExpression('*', result, unary(), token.posstr, token.posfile)
                continue
            }

            if (match(ArithmeticTokenType.SLASH)) {
                result = BinaryExpression('/', result, unary(), token.posstr, token.posfile)
                continue
            }
            break
        }

        return result
    }

    private fun unary(): Expression {
        return when {
            match(ArithmeticTokenType.MINUS) -> UnaryExpression('-', primary())
            else -> when {
                match(ArithmeticTokenType.PLUS) -> primary()
                else -> primary()
            }
        }

    }

    private fun primary(): Expression {
        val current = get(0)
        if (match(TokenType.NUMBER)) {
            return ValueExpression(java.lang.Double.parseDouble(current.text))
        }

        if (get(0).type === TokenType.WORD && get(1).type === TokenType.LPAREN) {
            return function()
        }

        if (get(0).type === TokenType.WORD && get(1).type === TokenType.LBRACKET) {
            return element()
        }

        if (get(0).type === TokenType.LBRACKET) {
            return array()
        }

        if (match(TokenType.WORD)) {
            return VariableExpression(current.text)
        }

        if (match(TokenType.TEXT)) {
            return ValueExpression(current.text)
        }

        if (match(TokenType.LPAREN)) {
            val result = expression()
            if (match(TokenType.RPAREN)) {
                return result
            }
        }

        errors.add(String.format("%d:%d: Don't assign the value near \"%s\" \n", current.posfile, current.posstr, current.text))
        return ErrorExpression()
    }

    private fun array(): Expression {
        consume(TokenType.LBRACKET, "array expression")
        val elements = CustomArrayList<Expression>()
        while (!match(TokenType.RBRACKET)) {
            elements.add(expression())
            match(TokenType.COMMA)
        }
        return ArrayExpression(elements)
    }

    private fun element(): ArrayAccessExpression {
        val variable = consume(TokenType.WORD, "array access expression")!!.text
        val indexes = CustomArrayList<Expression>()
        do {
            consume(TokenType.LBRACKET, "array access expression")
            indexes.add(expression())
            consume(TokenType.RBRACKET, "array access expression")
        } while (get(0).type === TokenType.LBRACKET)

        val prevToken = get(-2)
        return ArrayAccessExpression(variable, indexes, prevToken.posstr, prevToken.posfile)
    }

    private fun consume(type: TokenType, place: String): Token? {
        val current = get(0)
        if (type !== current.type) {
            errors.add(String.format("%d:%d: %s doesn't match type %s in %s", current.posfile, current.posstr, current, type, place))
            return null
        }
        pos++
        return current
    }

    private fun match(type: ITokenType): Boolean {
        val current = get(0)
        if (type !== current.type)
            return false

        pos++
        return true
    }

    private operator fun get(relativePosition: Int): Token {
        val position = pos + relativePosition

        return if (position >= size) EOF else tokens[position]

    }

    companion object {

        private val EOF = Token(TokenType.EOF, "", 0, 0)
    }
}
