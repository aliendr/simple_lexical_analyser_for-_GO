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
                    if ((buffer1.charAt(cursor) == '\r') || (buffer1.charAt(cursor) == ' ') || (buffer1.charAt(cursor) == '\n')) {
                        if (buffer1.charAt(cursor) == '\n')
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
                                if (isOperator(lexem)) { // already minimum 2 symbol operator if true

                                    if (currentBuffer == 1) {
                                        if (forward != buffer1.length() - 1) {
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
                                    } else {
                                        if (forward != buffer2.length() - 1) {
                                            forward++;
                                            nextChar = buffer2.charAt(forward);
                                        } else {
                                            forward = 0;
                                            currentBuffer = 1;
                                            buffer1 = fillBuffer(reader);
                                            if (buffer1.length() == 0) {
                                                tokenCreation(lexem, "Operator", line);
                                                break;
                                            } else
                                                nextChar = buffer1.charAt(forward);
                                        }
                                    }
                                    //third char added

                                    if ((nextChar == '=') && (lexem.equals(">>") || lexem.equals("<<") || lexem.equals("&^"))) {
                                        if (lexem.equals(">>")) {
                                            tokenCreation(">>=", "Operator", line);
                                        } else if (lexem.equals("<<")) {
                                            tokenCreation("<<=", "Operator", line);
                                        } else if (lexem.equals("&^")) {
                                            tokenCreation("&^=", "Operator", line);
                                        }

                                        if (currentBuffer == 1) {
                                            if (cursor + 3 < buffer1.length())
                                                cursor += 3;
                                            else {
                                                cursor = 3 - (buffer1.length() - 1 - cursor) - 1;
                                                currentBuffer = 2;
                                                buffer2 = fillBuffer(reader);
                                                if (buffer2.length() == 0)
                                                    break;
                                            }
                                        } else {
                                            if (cursor + 3 < buffer2.length())
                                                cursor += 3;
                                            else {
                                                cursor = 3 - (buffer2.length() - 1 - cursor) - 1;
                                                currentBuffer = 1;
                                                buffer1 = fillBuffer(reader);
                                                if (buffer1.length() == 0)
                                                    break;
                                            }
                                        }

                                    } else { //operator is double
                                        tokenCreation(lexem, "Operator", line);

                                        cursor = forward;
                                    }
                                } else { // operator is single
                                    tokenCreation(buffer1.charAt(cursor) + "", "Operator", line);

                                    cursor = forward;
                                }
                                forward = cursor;
                            } else {

                                // not operator and not delimiter

                                lexem = "";
                                nextChar = buffer1.charAt(cursor); // first digit in success
                                breakedInLoop = false;

                                if (Character.isDigit(nextChar)) {
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


                                    // binary literal
                                    if (lexem.equals("0") && (nextChar == 'b' || nextChar == 'B')) {
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

                                        while (nextChar == '0' || nextChar == '1') {
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

                                    } else if (lexem.equals("0") && (nextChar == 'o' || nextChar == 'O')) { // octal literal
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

                                        while (nextChar == '0' || nextChar == '1' || nextChar == '2' || nextChar == '3' || nextChar == '4' || nextChar == '5' || nextChar == '6' || nextChar == '7') {
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
                                        //hex literal
                                    } else if (lexem.equals("0") && (nextChar == 'x' || nextChar == 'X')) {
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

                                        while (Character.isDigit(nextChar) || ((int) nextChar >= 65 && (int) nextChar <= 70) || ((int) nextChar >= 97 && (int) nextChar <= 102)) {
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
                                        // decimal
                                    } else {
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
                                    }


                                }

                                if (breakedInLoop)
                                    break;
                                if (!lexem.equals("")) {
                                    cursor = forward;
                                    tokenCreation(lexem, "Integer literal", line);
                                } else {
                                    breakedInLoop = false;
                                    if (buffer1.charAt(cursor) == '_') {
                                        lexem = "_";

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
                                                    break;
                                                }
                                                nextChar = buffer1.charAt(forward);
                                            }
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
                                                if (currentBuffer == 1) {
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
                                                } else {
                                                    if (forward < buffer2.length() - 1) {
                                                        forward++;
                                                        nextChar = buffer2.charAt(forward);
                                                    } else {
                                                        forward = 0;
                                                        currentBuffer = 1;
                                                        buffer1 = fillBuffer(reader);
                                                        if (buffer1.length() == 0) {
                                                            breakedInLoop = true;
                                                            break;
                                                        }
                                                        nextChar = buffer1.charAt(forward);
                                                    }
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
                } else if (currentBuffer == 2) {
                    if ((buffer2.charAt(cursor) == '\r') || (buffer2.charAt(cursor) == ' ') || (buffer2.charAt(cursor) == '\n')) {
                        if (buffer2.charAt(cursor) == '\n')
                            line++;
                        if (cursor < buffer2.length() - 1)
                            cursor++;
                        else {
                            cursor = 0;
                            currentBuffer = 1;
                            buffer1 = fillBuffer(reader);
                            if (buffer1.length() == 0)
                                break;
                        }
                        forward = cursor;
                    } else {
                        if (!isDelimiters(buffer2.charAt(cursor), line)) { // inside a function token created if true
                            if (isOperator("" + buffer2.charAt(cursor))) { // return boolean and do not create token
                                lexem = "" + buffer2.charAt(cursor); // with 1 symbol

                                if (cursor != buffer2.length() - 1) {
                                    forward++;
                                    nextChar = buffer2.charAt(forward);
                                } else {
                                    forward = 0;
                                    currentBuffer = 1;
                                    buffer1 = fillBuffer(reader);
                                    if (buffer1.length() == 0) {
                                        tokenCreation(lexem, "Operator", line);
                                        break;
                                    } else
                                        nextChar = buffer1.charAt(forward);
                                }
                                lexem += nextChar; // plus second char
                                if (isOperator(lexem)) { // already minimum 2 symbol operator

                                    if (currentBuffer == 1) {
                                        if (forward != buffer1.length() - 1) {
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
                                    } else {
                                        if (forward != buffer2.length() - 1) {
                                            forward++;
                                            nextChar = buffer2.charAt(forward);
                                        } else {
                                            forward = 0;
                                            currentBuffer = 1;
                                            buffer1 = fillBuffer(reader);
                                            if (buffer1.length() == 0) {
                                                tokenCreation(lexem, "Operator", line);
                                                break;
                                            } else
                                                nextChar = buffer1.charAt(forward);
                                        }
                                    }
                                    //third char added

                                    if ((nextChar == '=') && (lexem.equals(">>") || lexem.equals("<<") || lexem.equals("&^"))) {
                                        if (lexem.equals(">>")) {
                                            tokenCreation(">>=", "Operator", line);
                                        } else if (lexem.equals("<<")) {
                                            tokenCreation("<<=", "Operator", line);
                                        } else if (lexem.equals("&^")) {
                                            tokenCreation("&^=", "Operator", line);
                                        }

                                        if (currentBuffer == 1) {
                                            if (cursor + 3 < buffer1.length())
                                                cursor += 3;
                                            else {
                                                cursor = 3 - (buffer1.length() - 1 - cursor) - 1;
                                                currentBuffer = 2;
                                                buffer2 = fillBuffer(reader);
                                                if (buffer2.length() == 0)
                                                    break;
                                            }
                                        } else {
                                            if (cursor + 3 < buffer2.length())
                                                cursor += 3;
                                            else {
                                                cursor = 3 - (buffer2.length() - 1 - cursor) - 1;
                                                currentBuffer = 1;
                                                buffer1 = fillBuffer(reader);
                                                if (buffer1.length() == 0)
                                                    break;
                                            }
                                        }

                                    } else {
                                        tokenCreation(lexem, "Operator", line);

                                        cursor = forward;
                                    }
                                } else {
                                    tokenCreation(buffer2.charAt(cursor) + "", "Operator", line);

                                    cursor = forward;
                                }
                                forward = cursor;
                            } else { // not operator and not delimiter
                                lexem = "";
                                nextChar = buffer2.charAt(cursor);
                                breakedInLoop = false;
                                while (Character.isDigit(nextChar) || nextChar == 'b' || nextChar == 'B' || nextChar == 'o' || nextChar == 'O' || nextChar == 'x' || nextChar == 'X' || nextChar == '_') {
                                    lexem += nextChar;
                                    if (currentBuffer == 2) {
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
                                    } else {
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
                                    }

                                }
                                if (breakedInLoop)
                                    break;
                                if (!lexem.equals("")) {
                                    cursor = forward;
                                    tokenCreation(lexem, "Integer literal", line); // + proverka na dvoi4nie cifri
                                } else {
                                    breakedInLoop = false;
                                    if (buffer2.charAt(cursor) == '_') {
                                        lexem = "_";

                                        if (forward < buffer2.length() - 1) {
                                            forward++;
                                            nextChar = buffer2.charAt(forward);
                                        } else {
                                            forward = 0;
                                            currentBuffer = 1;
                                            buffer1 = fillBuffer(reader);
                                            if (buffer1.length() == 0) {
                                                tokenCreation(lexem, "Identifier", line);
                                                break;
                                            }
                                            nextChar = buffer1.charAt(forward);
                                        }

                                        while (Character.isLetterOrDigit(nextChar) || nextChar == '_') {
                                            lexem += nextChar;
                                            if (currentBuffer == 2) {
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
                                            } else {
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
                                            }

                                        }
                                        if (breakedInLoop)
                                            break;
                                        tokenCreation(lexem, "Identifier", line);

                                        cursor = forward;

                                    } else {
                                        breakedInLoop = false;
                                        if (Character.isLetter(buffer2.charAt(cursor))) {
                                            lexem = "" + buffer2.charAt(cursor);

                                            if (forward < buffer2.length() - 1) {
                                                forward++;
                                                nextChar = buffer2.charAt(forward);
                                            } else {
                                                forward = 0;
                                                currentBuffer = 1;
                                                buffer1 = fillBuffer(reader);
                                                if (buffer1.length() == 0) {
                                                    breakedInLoop = true;
                                                    break;
                                                }
                                                nextChar = buffer1.charAt(forward);
                                            }

                                            while (Character.isLetterOrDigit(nextChar) || nextChar == '_') {
                                                lexem += nextChar;
                                                if (forward < buffer2.length() - 1) {
                                                    forward++;
                                                    nextChar = buffer2.charAt(forward);
                                                } else {
                                                    forward = 0;
                                                    currentBuffer = 1;
                                                    buffer1 = fillBuffer(reader);
                                                    if (buffer1.length() == 0) {
                                                        breakedInLoop = true;
                                                        break;
                                                    }
                                                    nextChar = buffer1.charAt(forward);
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
                            tokenCreation(buffer2.charAt(cursor) + "", "Delimiter", line);
                            if (cursor < buffer2.length() - 1)
                                cursor++;
                            else {
                                cursor = 0;
                                currentBuffer = 1;
                                buffer1 = fillBuffer(reader);
                                if (buffer1.length() == 0)
                                    break;
                            }
                            forward = cursor;
                        }
                    }
                }

            } //end

            for (int i = 0; i < tokens.size(); i++) {
                System.out.println(tokens.get(i).line + " " + tokens.get(i).value + " " + tokens.get(i).type);
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
                    return true;
                case "if":
                    return true;
                default:
                    return false;
            }
        } else if (str.length() == 3) {
            switch (str) {
                case "for":
                    return true;
                case "var":
                    return true;
                case "map":
                    return true;
                default:
                    return false;
            }
        } else if (str.length() == 4) {
            switch (str) {
                case "case":
                    return true;
                case "chan":
                    return true;
                case "else":
                    return true;
                case "func":
                    return true;
                case "goto":
                    return true;
                case "type":
                    return true;
                default:
                    return false;
            }
        } else if (str.length() == 5) {
            switch (str) {
                case "break":
                    return true;
                case "const":
                    return true;
                case "defer":
                    return true;
                case "range":
                    return true;
                default:
                    return false;
            }
        } else if (str.length() == 6) {
            switch (str) {
                case "import":
                    return true;
                case "return":
                    return true;
                case "select":
                    return true;
                case "struct":
                    return true;
                case "switch":
                    return true;
                default:
                    return false;
            }
        }
        if (str.length() == 7) {
            switch (str) {
                case "package":
                    return true;
                case "default":
                    return true;
                default:
                    return false;
            }
        }
        if (str.equals("continue")) {
            return true;
        }
        if (str.equals("interface")) {
            return true;
        }
        if (str.equals("fallthrough")) {
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
        boolean error = true;
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) != '_') error = false;
        }
        if (error) type = "ERROR";
        if (type.equals("Integer literal") && value.charAt(value.length() - 1) == '_'){
            while(value.charAt(value.length() - 1) == '_')
                value = value.substring(0, value.length() - 1);
        }
        Token token = new Token(value, type, line);
        tokens.add(token);
    }

    private static String fillBuffer(FileReader reader) throws IOException {
        int i = 0;
        String s = "";
        int c;
        while (i < 10 && (c = reader.read()) != -1) {
            s += (char) c;
            i++;
        }
        return s;
    }
}