public class Token {

    String value;
    String type;
    int line;

    Token(String type, String value, int line){
        this.value=value;
        this.type=type;
        this.line=line;
    }
}
