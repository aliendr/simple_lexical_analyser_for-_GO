import java.util.Scanner;
import java.util.Stack;


public class TokenDetector {

    Stack<String> stack = new Stack<>();

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        System.out.print("Input a string ");
        String s = in.next();
        System.out.println(isKeyword(s));

    }

    //create and write tokens to output in each case
    private static boolean isKeyword(String str){
        if (str.length()==2){
            switch (str){
                case "go":
                    System.out.println("go token");
                    return true;
                case "if":
                    System.out.println("if token");
                    return true;
                default: return false;
            }
        } else if (str.length()==3){
            switch (str){
                case "for":
                    System.out.println("FOR token");
                    return true;
                case "var":
                    System.out.println("VAR token");
                    return true;
                case "map":
                    System.out.println("MAP token");
                    return true;
                default: return false;
            }
        } else if (str.length()==4){
            switch (str){
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
                default: return false;
            }
        }  else if (str.length()==5){
            switch (str){
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
                default: return false;
            }
        } else if (str.length()==6){
            switch (str){
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
                default: return false;
            }
        } if (str.length()==7){
            switch (str){
                case "package":
                    System.out.println("PACKAGE token");
                    return true;
                case "default":
                    System.out.println("DEFAULT token");
                    return true;
                default: return false;
            }
        } if (str.equals("continue")){
                System.out.println("CONTINUE token");
                return true;
        } if (str.equals("interface")){
                System.out.println("INTERFACE token");
                return true;
        } if (str.equals("fallthrough")){
                System.out.println("FALLTHROUGH token");
                return true;
        }
        else return false;
    }

    private static boolean isOperator(String str){

        return false;
    }

    // isKeyword must be checked before calling this function
    private static boolean isIdentifier(String str){
        if(Character.isLetter(str.charAt(0)) || str.charAt(0)=='_'){
            for (int i=1;i<str.length();i++) {
                    if(!(Character.isLetterOrDigit(str.charAt(i)) || str.charAt(i)=='_'))
                        return false;
            }
            //create and write token to output
            return true;
        } else return false;
    }
}
