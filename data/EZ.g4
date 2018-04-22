grammar EZ;

program : statement_list;
statement_list : statement (spaceChar)*  statement_list
| statement;

statement : decl_statement
| assign_statement
| read_statement
| write_statement
| if_statement
| loop_statement
| comment_statement;

decl_statement : 'variable ' identifier ';' ;

assign_statement : identifier ' = ' expression ';';

read_statement : 'read ' identifier ';';

write_statement : 'write ' expression ';';

if_statement : 'if' '(' cond_expression ')' 'then' '{' statement_list '}'
| 'if' '(' cond_expression ')' 'then' '{' statement_list '}' 'else' '{statement_list}';

loop_statement : 'repeat_when' '(' cond_expression ')' '{' statement_list '}';

comment_statement : '/#' .* '#/';

expression : exp1 ' + ' expression |
exp1 '-' expression |
exp1;

exp1 :      identifier '*'  exp1
| number '*' exp1
| identifier '/' exp1
| number '/' exp1
| identifier '%' exp1
| number '%' exp1
| identifier
| number;

cond_expression : expression cond_operators expression | bool_val;
cond_operators :  '=='
| '<'
| '>'
| '<='
| '>='
| '!=';

identifier :  lowerChar (letters | underscore | digit)* ;
letters : (lowerChar | upperChar)+;
underscore : '_';
bool_val : 'true'
| 'false';

number : '-'? digit+;
digit : '0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9' ;
lowerChar : 'a' | 'b' | 'c' | 'd' | 'e' | 'f' | 'g' | 'h' | 'i' | 'j' | 'k' | 'l' | 'm' | 'n' | 'o' | 'p' | 'q' | 'r' | 's' | 't' | 'u' | 'v' | 'w' | 'x' | 'y' | 'z' ;
upperChar : 'A' | 'B' | 'C' | 'D' | 'E' | 'F' | 'G' | 'H' | 'I' | 'J' | 'K' | 'L' | 'M' | 'N' | 'O' | 'P' | 'Q' | 'R' | 'S' | 'T' | 'U' | 'V' | 'W' | 'X' | 'Y' | 'Z' ;
spaceChar   : '\r' '\n' | '\n' | '\r';