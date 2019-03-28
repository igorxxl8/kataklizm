package parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class LexicalError {
    private List<Token> tokens;
    private String filename;

    public LexicalError(List<Token> tokens, String filname) {
        this.tokens = tokens;
        this.filename = filname;
    }

    public void Analyze() {
        System.out.println("\nTokens:");
        var keywordList = new HashSet<IKeywordTokenType>();
        var variableList = new HashSet<String>();
        var valueList = new HashMap<String, Token>();
        var operatorsList = new HashSet<String>();
        var constantsList = new HashSet<String>();
        var constantsValueList = new HashMap<String, String>();
        Token previousToken;
        Token currentToken = null;
        int i = 0;


        for (var token : tokens) {
            previousToken = currentToken;
            currentToken = token;

            if (currentToken != null && currentToken.getType() == TokenType.EQ) {
                if (previousToken != null && previousToken.getType() == TokenType.WORD) {
                    variableList.add(previousToken.getText());
                    valueList.put(previousToken.getText(), tokens.get(i + 1));
                }
            }

            if (currentToken != null && currentToken.getType() == TokenType.LPAREN) {
                if (previousToken != null && previousToken.getType() == TokenType.WORD) {
                    for (var a : KeywordTokenType.values()) {
                        var prevText = previousToken.getText();
                        var kwtext = a.getName();
                        if (kwtext.contains(prevText) || prevText.contains(kwtext)) {
                            //var b = Lexer.WordPosition.get(prevText);
                            //Lexer.Errors.add(String.format("%d:%d: error: implicit declaration of function: \'%s\'", b.a, b.b, prevText));
                        }
                    }
                }

            } else if (currentToken != null && currentToken.getType() == TokenType.NUMBER) {
                if (previousToken != null && previousToken.getType() != TokenType.EQ) {
                    var value = currentToken.getText();
                    constantsList.add(value);
                    var t = "";
                    if (value.contains(".")) {
                        t = "double";
                    } else {
                        t = "integer";
                    }
                    constantsValueList.put(value, t);

                }
            }

            var tokenType = token.getType();
            if (tokenType instanceof IKeywordTokenType) {
                keywordList.add((IKeywordTokenType) tokenType);
            } else if (tokenType instanceof IArithmeticTokenType) {
                operatorsList.add((tokenType + " | is arithmetic operator"));
            } else if (tokenType instanceof IComparisonTokenType) {
                operatorsList.add((tokenType + " | is comparison operator"));
            }

            System.out.println(String.format("\t%s", token));
            i++;
        }

        System.out.println("\n\nVARIABLES TABLE");
        for (var vt :
                variableList) {
            var token = valueList.get(vt);
            var type = token.getType();
            var t = "";
            var value = valueList.get(vt).getText();
            if (type == TokenType.TEXT) {
                t = "string";
            } else if (type == TokenType.NUMBER) {
                if (value.contains(".")) {
                    t = "double";
                } else {
                    t = "integer";
                }
            }
            System.out.println(vt + " = " + value + " | " + t);
        }

        System.out.println("\n\nKEY WORDS TABLE");
        for (var tt : keywordList) {
            System.out.print(String.format("%s\n", tt));
        }

        System.out.println("\n\nOPERATORS TABLE");
        for (var ol : operatorsList) {
            System.out.println(ol);
        }

        System.out.println("\n\nCONSTANTS TABLE");
        for (var cl : constantsList) {
            System.out.println(cl + " | " + constantsValueList.get(cl));
        }

        for (var error :
                Lexer.Errors) {
            System.err.println(String.format("%s:%s", filename, error));
        }
    }
}
