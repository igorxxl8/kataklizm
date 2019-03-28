package parser

import java.util.HashMap
import java.util.HashSet

class LexicalError(private val tokens: List<Token>, private val filename: String) {

    fun Analyze() {
        println("\nTokens:")
        val keywordList = HashSet<IKeywordTokenType>()
        val variableList = HashSet<String>()
        val valueList = HashMap<String, Token>()
        val operatorsList = HashSet<String>()
        val constantsList = HashSet<String>()
        val constantsValueList = HashMap<String, String>()
        var previousToken: Token?
        var currentToken: Token? = null


        for ((i, token) in tokens.withIndex()) {
            previousToken = currentToken
            currentToken = token

            when {
                currentToken.type === TokenType.EQ -> if (previousToken != null && previousToken.type === TokenType.WORD) {
                    variableList.add(previousToken.text)
                    valueList[previousToken.text] = tokens[i + 1]
                }
            }

            when {
                currentToken.type === TokenType.NUMBER -> if (previousToken != null && previousToken.type !== TokenType.EQ) {
                    val value = currentToken.text
                    constantsList.add(value)
                    var t: String
                    t = when {
                        value.contains(".") -> "double"
                        else -> "integer"
                    }
                    constantsValueList[value] = t

                }
            }

            val tokenType = token.type
            when (tokenType) {
                is IKeywordTokenType -> keywordList.add(tokenType)
                is IArithmeticTokenType -> operatorsList.add("$tokenType | is arithmetic operator")
                is IComparisonTokenType -> operatorsList.add("$tokenType | is comparison operator")
            }

            println(String.format("\t%s", token))
        }

        println("\n\nVARIABLES TABLE")
        for (vt in variableList) {
            val token = valueList[vt]
            val type = token!!.type
            var t = ""
            val value = valueList[vt]?.text
            when {
                type === TokenType.TEXT -> t = "string"
                type === TokenType.NUMBER -> t = when {
                    value!!.contains(".") -> "double"
                    else -> "integer"
                }
            }
            println("$vt = $value | $t")
        }

        println("\n\nKEY WORDS TABLE")
        for (tt in keywordList) {
            print(String.format("%s\n", tt))
        }

        println("\n\nOPERATORS TABLE")
        for (ol in operatorsList) {
            println(ol)
        }

        println("\n\nCONSTANTS TABLE")
        for (cl in constantsList) {
            println(cl + " | " + constantsValueList[cl])
        }

        for (error in Lexer.Errors) {
            System.err.println(String.format("%s:%s", filename, error))
        }
    }
}
