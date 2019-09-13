import java.io.*;
import java.util.ArrayList;
import java.util.Stack;


public class TokenDetector {

    private static ArrayList<Token> tokens = new ArrayList();

    public static void main(String[] args) {

        try (FileReader reader = new FileReader("code.txt")) {
            // читаем посимвольно

            int c;
            int line = 1;
            String lexem = "";

            String buffer1 = "", buffer2 = "";

            buffer1 = fillBuffer(reader);

            int cursor = 0;
            int forward = cursor;
            char nextChar = 'c';
            int currentBuffer = 1;
            boolean breakedInLoop = false;
            while (!(buffer1.equals("") && buffer2.equals(""))) {
                if (currentBuffer == 1) {
                    if (buffer1.charAt(cursor) == '\n') {
                        line++;
                        if (cursor < buffer1.length() - 1)
                            cursor++;
                        else {
                            cursor = 0;
                            currentBuffer = 2;
                            buffer2 = fillBuffer(reader);
                            if (buffer2.length() == 0)
                                break;
                        }
                        forward = cursor;
                    } else if (buffer1.charAt(cursor) == ' ') {
                        if (cursor < buffer1.length() - 1)
                            cursor++;
                        else {
                            cursor = 0;
                            currentBuffer = 2;
                            buffer2 = fillBuffer(reader);
                            if (buffer2.length() == 0)
                                break;
                        }
                        forward = cursor;
                    } else {
                        if (!isDelimiters(buffer1.charAt(cursor), line)) { // inside a function token created if true
                            if (isOperator("" + buffer1.charAt(cursor))) { // return boolean and do not create token
                                lexem = "" + buffer1.charAt(cursor); // with 1 symbol

                                if (cursor != buffer1.length() - 1) {
                                    forward++;
                                    nextChar = buffer1.charAt(forward);
                                } else {
                                    forward = 0;
                                    currentBuffer = 2;
                                    buffer2 = fillBuffer(reader);
                                    if (buffer2.length() == 0) {
                                        tokenCreation(lexem, "Operator", line);
                                        break;
                                    } else
                                        nextChar = buffer2.charAt(forward);
                                }
                                lexem += nextChar; // plus second char
                                if (isOperator(lexem)) { // already minimum 2 symbol operator
                                    if (cursor != buffer1.length() - 1) {
                                        forward++;
                                        nextChar = buffer1.charAt(forward);
                                    } else {
                                        forward = 0;
                                        currentBuffer = 2;
                                        buffer2 = fillBuffer(reader);
                                        if (buffer2.length() == 0) {
                                            tokenCreation(lexem, "Operator", line);
                                            break;
                                        } else
                                            nextChar = buffer2.charAt(forward);
                                    } //third char added

                                    if (nextChar == '=') {
                                        if (lexem.equals(">>")) {
                                            tokenCreation(">>=", "Operator", line);
                                        } else if (lexem.equals("<<")) {
                                            tokenCreation("<<=", "Operator", line);
                                        } else if (lexem.equals("&^")) {
                                            tokenCreation("&^=", "Operator", line);
                                        }

                                        if (cursor + 3 < buffer1.length() - 1)
                                            cursor += 3;
                                        else {
                                            cursor = 3 - (buffer1.length() - 1 - cursor) - 1;
                                            currentBuffer = 2;
                                            buffer2 = fillBuffer(reader);
                                            if (buffer2.length() == 0)
                                                break;
                                        }

                                    } else {
                                        tokenCreation(lexem, "Operator", line);

                                        if (cursor + 2 < buffer1.length() - 1)
                                            cursor += 2;
                                        else {
                                            cursor = 2 - (buffer1.length() - 1 - cursor) - 1;
                                            currentBuffer = 2;
                                            buffer2 = fillBuffer(reader);
                                            if (buffer2.length() == 0)
                                                break;
                                        }
                                    }
                                } else {
                                    tokenCreation(lexem, "Operator", line);

                                    if (cursor + 1 < buffer1.length() - 1)
                                        cursor++;
                                    else {
                                        cursor = 0;
                                        currentBuffer = 2;
                                        buffer2 = fillBuffer(reader);
                                        if (buffer2.length() == 0)
                                            break;
                                    }
                                }
                                forward = cursor;
                            } else { // not operator and not delimiter
                                lexem = "";
                                nextChar = buffer1.charAt(cursor);
                                breakedInLoop = false;
                                while (Character.isDigit(nextChar)) {
                                    lexem += nextChar;
                                    if (currentBuffer == 1) {
                                        if (forward != buffer1.length() - 1) {
                                            forward++;
                                            nextChar = buffer1.charAt(forward);
                                        } else {
                                            forward = 0;
                                            currentBuffer = 2;
                                            buffer2 = fillBuffer(reader);
                                            if (buffer2.length() == 0) {
                                                tokenCreation(lexem, "Integer literal", line);
                                                breakedInLoop = true;
                                                break;
                                            }
                                            nextChar = buffer2.charAt(forward);

                                        }
                                    } else {
                                        if (forward != buffer2.length() - 1) {
                                            forward++;
                                            nextChar = buffer2.charAt(forward);
                                        } else {
                                            forward = 0;
                                            currentBuffer = 1;
                                            buffer1 = fillBuffer(reader);
                                            if (buffer1.length() == 0) {
                                                tokenCreation(lexem, "Integer literal", line);
                                                breakedInLoop = true;
                                                break;
                                            }
                                            nextChar = buffer1.charAt(forward);
                                        }
                                    }

                                }
                                if (breakedInLoop)
                                    break;
                                if (!lexem.equals("")) {
                                    cursor = forward;
                                    tokenCreation(lexem, "Integer literal", line); // + proverka na dvoi4nie cifri
                                } else {
                                    breakedInLoop = false;
                                    if (buffer1.charAt(cursor) == '_') {
                                        lexem = "_";

                                        if (forward < buffer1.length() - 1) {
                                            forward++;
                                            nextChar = buffer1.charAt(forward);
                                        } else {
                                            forward = 0;
                                            currentBuffer = 2;
                                            buffer2 = fillBuffer(reader);
                                            if (buffer2.length() == 0) {
                                                tokenCreation(lexem, "Identifier", line);
                                                break;
                                            }
                                            nextChar = buffer2.charAt(forward);
                                        }

                                        while (Character.isLetterOrDigit(nextChar) || nextChar == '_') {
                                            lexem += nextChar;
                                            if (currentBuffer == 1) {
                                                if (forward < buffer1.length() - 1) {
                                                    forward++;
                                                    nextChar = buffer1.charAt(forward);
                                                } else {
                                                    forward = 0;
                                                    currentBuffer = 2;
                                                    buffer2 = fillBuffer(reader);
                                                    if (buffer2.length() == 0) {
                                                        tokenCreation(lexem, "Identifier", line);
                                                        breakedInLoop = true;
                                                        break;
                                                    }
                                                    nextChar = buffer2.charAt(forward);
                                                }
                                            } else {
                                                if (forward < buffer2.length() - 1) {
                                                    forward++;
                                                    nextChar = buffer2.charAt(forward);
                                                } else {
                                                    forward = 0;
                                                    currentBuffer = 1;
                                                    buffer1 = fillBuffer(reader);
                                                    if (buffer1.length() == 0) {
                                                        tokenCreation(lexem, "Identifier", line);
                                                        breakedInLoop = true;
                                                        break;
                                                    }
                                                    nextChar = buffer1.charAt(forward);
                                                }
                                            }

                                        }
                                        if (breakedInLoop)
                                            break;
                                        tokenCreation(lexem, "Identifier", line);

                                        cursor = forward;

                                    } else {
                                        breakedInLoop = false;
                                        if (Character.isLetter(buffer1.charAt(cursor))) {
                                            lexem = "" + buffer1.charAt(cursor);

                                            if (forward < buffer1.length() - 1) {
                                                forward++;
                                                nextChar = buffer1.charAt(forward);
                                            } else {
                                                forward = 0;
                                                currentBuffer = 2;
                                                buffer2 = fillBuffer(reader);
                                                if (buffer2.length() == 0) {
                                                    breakedInLoop = true;
                                                    break;
                                                }
                                                nextChar = buffer2.charAt(forward);
                                            }

                                            while (Character.isLetterOrDigit(nextChar) || nextChar == '_') {
                                                lexem += nextChar;
                                                if (forward < buffer1.length() - 1) {
                                                    forward++;
                                                    nextChar = buffer1.charAt(forward);
                                                } else {
                                                    forward = 0;
                                                    currentBuffer = 2;
                                                    buffer2 = fillBuffer(reader);
                                                    if (buffer2.length() == 0) {
                                                        breakedInLoop = true;
                                                        break;
                                                    }
                                                    nextChar = buffer2.charAt(forward);
                                                }
                                            }
                                            if (isKeyword(lexem, line)) {
                                                tokenCreation(lexem, "keyWord", line);
                                            } else {
                                                tokenCreation(lexem, "Identifier", line);
                                            }
                                            if (breakedInLoop)
                                                break;
                                        }

                                        cursor = forward;
                                    }
                                }
                            }
                        } else {
                            tokenCreation(buffer1.charAt(cursor) + "", "Delimiter", line);
                            if (cursor < buffer1.length() - 1)
                                cursor++;
                            else {
                                cursor = 0;
                                currentBuffer = 2;
                                buffer2 = fillBuffer(reader);
                                if (buffer2.length() == 0)
                                    break;
                            }
                            forward = cursor;
                        }
                    }
                }

            }
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }

    }

    private static boolean isDelimiters(char c, int strN) {
        switch (c) {
            case '(':
                //token
                return true;
            case ')':
                //token
                return true;
            case '[':
                //token
                return true;
            case ']':
                //token
                return true;
            case '{':
                //token
                return true;
            case '}':
                //token
                return true;
            case '.':
                //token
                return true;
            case ',':
                //token
                return true;
            case ';':
                //token
                return true;
            default:
                return false;
        }
    }

    //create and write tokens to output in each case
    private static boolean isKeyword(String str, int strN) {
        if (str.length() == 2) {
            switch (str) {
                case "go":
                    System.out.println("go token");
                    return true;
                case "if":
                    System.out.println("if token");
                    return true;
                default:
                    return false;
            }
        } else if (str.length() == 3) {
            switch (str) {
                case "for":
                    System.out.println("FOR token");
                    return true;
                case "var":
                    System.out.println("VAR token");
                    return true;
                case "map":
                    System.out.println("MAP token");
                    return true;
                default:
                    return false;
            }
        } else if (str.length() == 4) {
            switch (str) {
                case "case":
                    System.out.println("CASE token");
                    return true;
                case "chan":
                    System.out.println("CHAN token");
                    return true;
                case "else":
                    System.out.println("ELSE token");
                    return true;
                case "func":
                    System.out.println("FUNC token");
                    return true;
                case "goto":
                    System.out.println("GOTO token");
                    return true;
                case "type":
                    System.out.println("TYPE token");
                    return true;
                default:
                    return false;
            }
        } else if (str.length() == 5) {
            switch (str) {
                case "break":
                    System.out.println("BREAK token");
                    return true;
                case "const":
                    System.out.println("CONST token");
                    return true;
                case "defer":
                    System.out.println("DEFER token");
                    return true;
                case "range":
                    System.out.println("RANGE token");
                    return true;
                default:
                    return false;
            }
        } else if (str.length() == 6) {
            switch (str) {
                case "import":
                    System.out.println("IMPORT token");
                    return true;
                case "return":
                    System.out.println("RETURN token");
                    return true;
                case "select":
                    System.out.println("SELECT token");
                    return true;
                case "struct":
                    System.out.println("STRUCT token");
                    return true;
                case "switch":
                    System.out.println("SWITCH token");
                    return true;
                default:
                    return false;
            }
        }
        if (str.length() == 7) {
            switch (str) {
                case "package":
                    System.out.println("PACKAGE token");
                    return true;
                case "default":
                    System.out.println("DEFAULT token");
                    return true;
                default:
                    return false;
            }
        }
        if (str.equals("continue")) {
            System.out.println("CONTINUE token");
            return true;
        }
        if (str.equals("interface")) {
            System.out.println("INTERFACE token");
            return true;
        }
        if (str.equals("fallthrough")) {
            System.out.println("FALLTHROUGH token");
            return true;
        } else return false;
    }

    private static boolean isOperator(String str) {
        switch (str) {
            case "+":
                return true;
            case "-":
                return true;
            case "*":
                return true;
            case "/":
                return true;
            case "%":
                return true;
            case "&":
                return true;
            case "|":
                return true;
            case "^":
                return true;
            case ">":
                return true;
            case "<":
                return true;
            case "=":
                return true;
            case "!":
                return true;
            case "+=":
                return true;
            case "++":
                return true;
            case "-=":
                return true;
            case "--":
                return true;
            case "*=":
                return true;
            case "/=":
                return true;
            case "%=":
                return true;
            case "%^":
                return true;
            case "&&":
                return true;
            case "&=":
                return true;
            case "|=":
                return true;
            case "||":
                return true;
            case "^=":
                return true;
            case ">>":
                return true;
            case "<<":
                return true;
            case "<-":
                return true;
            case "<=":
                return true;
            case "==":
                return true;
            case "!=":
                return true;
            case "&^=":
                return true;
            case ">>=":
                return true;
            case "<<=":
                return true;
            default:
                return false;
        }
    }

    private static void tokenCreation(String value, String type, int line) {
        Token token = new Token(value, type, line);
        tokens.add(token);
    }

    private static String fillBuffer(FileReader reader) throws IOException {
        int i = 0;
        String s = "";
        int c;
        while (i < 500 && (c = reader.read()) != -1) {
            s += (char) c;
            i++;
        }
        return s;
    }
}