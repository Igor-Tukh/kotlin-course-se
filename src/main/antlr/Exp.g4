grammar Exp;

file
    :    block
    ;

block
    :    (statement)*
    ;

blockWithBraces
    :    '{' block '}'
    ;

statement
    :       function
        |   variable
        |   expression
        |   whileLoop
        |   ifStatement
        |   assigment
        |   returnStatement
    ;

function
    :    'fun' Indetefier '(' parameterNames ')' blockWithBraces
    ;

variable
    :    'var' Indetefier  ('=' expression)?
    ;

parameterNames
    :    (Indetefier (',' Indetefier)*)?
    ;

whileLoop
    :    'while' '(' expression ')' blockWithBraces
    ;

ifStatement
    :    'if' '(' expression ')' blockWithBraces ('else' blockWithBraces)?
    ;

assigment
    :    Indetefier '=' expression
    ;

returnStatement
    :    'return' expression
    ;

expression
    :       functionCall
        |   indetefier=Indetefier
        |   literal=Literal
        |   '(' inner=expression ')'
        |   left=expression (operation=MULT | operation=DIVIDE) right=expression
        |   left=expression (operation=MODULO) right=expression
        |   left=expression (operation=PLUS | operation=MINUS) right=expression
        |   left=expression (operation=GREATER | operation=LOWER | operation=GEQ | operation=LEQ) right=expression
        |   left=expression (operation=EQ | operation=NEQ) right=expression
        |   left=expression (operation=AND) right=expression
        |   left=expression (operation=OR) right=expression
    ;

functionCall
    :    Indetefier '(' arguments ')'
    ;

arguments
    :    (expression (',' expression)*)?
    ;

Indetefier
    :     ('_' | ('a'..'z') | ('A'..'Z')) ('_' | ('a'..'z') | ('A'..'Z') | ('0'..'9'))*
    ;

Literal
    :    (('-'?)('1'..'9')('0'..'9')*)|'0'
    ;

PLUS
    :    '+'
    ;

MINUS
    :    '-'
    ;

MULT
    :    '*'
    ;

DIVIDE
    :    '/'
    ;

MODULO
    :    '%'
    ;

GREATER
    :    '>'
    ;

LOWER
    :    '<'
    ;

GEQ
    :    '>='
    ;

LEQ
    :    '<='
    ;

EQ
    :    '=='
    ;

NEQ
    :    '!='
    ;

OR
    :    '||'
    ;

AND
    :    '&&'
    ;

WS
    : (' ' | '\t' | '\r'| '\n' | '//' (~[\n])* '\n') -> skip
    ;
