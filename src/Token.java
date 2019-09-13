public class Token {

    String value;
    String type;
    int line;
    int place;

    Token(String type, String value, int line){
        this.value=value;
        this.type=type;
        this.line=line;
    }
}
