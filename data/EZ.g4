grammar EZ;

@header {
package com.ez.compiler;
}

program : statement_list;
statement_list : statement statement_list
| ;

statement : decl_statement
| assign_statement
| read_statement
| write_statement
| if_statement
| loop_statement
| function_call_statement ';'
| function_statement;

decl_statement : 'variable' identifier ';' ;

assign_statement : identifier '=' expression ';';

read_statement : 'read' identifier ';';

write_statement : 'write' expression ';'
| 'write' '"'(.)*?'";';

if_statement : 'if' '(' cond_expression ')' 'then' '{' statement_list RPARA else_statement?;

else_statement : 'else' '{' statement_list RPARA;

loop_statement : 'repeat_when' '(' cond_expression ')' '{' statement_list RPARA;

function_statement : 'function' identifier '('(identifier | (identifier (',' identifier)*))?')' '{' statement_list (return_statement)? RPARA;

return_statement :'return' (expression)';';

function_call_statement : identifier '('(expression | (expression (',' expression)*))?')' ;

expression : exp1 '+' expression  #addition |
exp1 '-' expression #subtraction |
exp1 #expPrecedence;

exp1 : factor '*' exp1 #multiplication
| factor '/' exp1 #division
| factor '%' exp1 #mod
| factor #fact ;


factor : identifier
| number
| function_call_statement ;


cond_expression : expression cond_operators expression
| bool_val;

boolean_expression : cond_expression boolean_operations boolean_expression
| cond_expression


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
RPARA : '}';

Comment_statement : '/#' (.)*? '#/' -> skip;
WS: [ \t\r\n]+ -> skip ;