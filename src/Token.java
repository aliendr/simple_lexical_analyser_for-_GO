public class Token {

    String value;
    String type;
    int line;
    int place;

    Token(String value, String type, int line, int place){
        this.value=value;
        this.type=type;
        this.line=line;
        this.place=place;
    }
}
