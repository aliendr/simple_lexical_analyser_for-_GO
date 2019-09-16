# simple_lexical_analyser_for_GO

Examples:

First:

a = 10;
for(i = 0; i <= 10; i++){
    if(i > 5)
        a--;
}

output:

1 Identifier a
1 Operator =
1 Integer literal 10
1 Delimiter ;
2 keyWord for
2 Delimiter (
2 Identifier i
2 Operator =
2 Integer literal 0
2 Delimiter ;
2 Identifier i
2 Operator <=
2 Integer literal 10
2 Delimiter ;
2 Identifier i
2 Operator ++
2 Delimiter )
2 Delimiter {
3 keyWord if
3 Delimiter (
3 Identifier i
3 Operator >
3 Integer literal 5
3 Delimiter )
4 Identifier a
4 Operator --
4 Delimiter ;
5 Delimiter }

Second:

a = 228;
b = 0b10001;
c = 0xAFDBC;
d = 2 * 2 + (4 / 2);

output:

1 Identifier a
1 Operator =
1 Integer literal 228
1 Delimiter ;
2 Identifier b
2 Operator =
2 Integer literal 0b10001
2 Delimiter ;
3 Identifier c
3 Operator =
3 Integer literal 0xAFDBC
3 Delimiter ;
4 Identifier d
4 Operator =
4 Integer literal 2
4 Operator *
4 Integer literal 2
4 Operator +
4 Delimiter (
4 Integer literal 4
4 Operator /
4 Integer literal 2
4 Delimiter )
4 Delimiter ;
