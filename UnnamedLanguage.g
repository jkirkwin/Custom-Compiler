grammar UnnamedLanguage;

// options { backtrack=true; } // TODO needed?

@members
{
protected void mismatch (IntStream input, int ttype, BitSet follow)
        throws RecognitionException
{
        throw new MismatchedTokenException(ttype, input);
}
public Object recoverFromMismatchedSet (IntStream input,
                                      RecognitionException e,
                                      BitSet follow)
        throws RecognitionException
{
        reportError(e);
        throw e;
}
}

@rulecatch {
        catch (RecognitionException ex) {
                reportError(ex);
                throw ex;
        }
}

// TODO remove this comment
/* 
 * This is a subset of the ul grammar to show you how to make new
 * production rules.
 * 
 * You will need to:
 *  - change type to be compoundType and include appropriate productions
 *  - introduce optional formalParameters
 *  - change functionBody to include variable declarations and statements 
 */

program : function+ EOF
	;

function: functionDecl functionBody
	;

functionDecl
        : compoundType identifier OPEN_PAREN formalParameters CLOSE_PAREN
	;

formalParameters
        : compoundType identifier moreFormals*
        | // Can be empty
        ;

moreFormals
        : COMMA compoundType identifier
        ;

functionBody
        : OPEN_BRACE varDecl* statement* CLOSE_BRACE
	;

varDecl
        : compoundType identifier SEMICOLON
        ;

compoundType
        : type 
        | type OPEN_BRACKET INTEGER_CONSTANT CLOSE_BRACKET
        ;

statement options {backtrack=true;} // TODO is it okay to have this here? It resolves a recursion error.
        : SEMICOLON
        | expression SEMICOLON
        | ifStatement
        | ifElseStatement
        | whileStatement
        | printStatement
        | printlnStatement
        | returnStatement
        | assignmentStatement
        | arrayAssignmentStatement
        ;

ifStatement
        : IF OPEN_PAREN expression CLOSE_PAREN block
        ;

ifElseStatement
        : ifStatement ELSE block
        ;

whileStatement
        : WHILE OPEN_PAREN expression CLOSE_PAREN block
        ;

printStatement
        : PRINT expression SEMICOLON
        ;

printlnStatement
        : PRINTLN expression SEMICOLON
        ;

returnStatement
        : RETURN expression SEMICOLON
        ;

assignmentStatement
        : identifier EQUAL_ASSIGNMENT expression SEMICOLON
        ;

arrayAssignmentStatement
        : arrayReference EQUAL_ASSIGNMENT expression SEMICOLON
        ;

block
        : OPEN_BRACE statement* CLOSE_BRACE
        ;

// TODO Add operators with precedence.
expression
        : arrayReference
        | functionCall
        | identifier
        | literal
        | OPEN_PAREN expression CLOSE_PAREN
        ;

// lessThanExpression
//         : plusMinusExpression ( LESS_THAN plusMinusExpression)*
//         ;

// plusMinusExpression
//         : multExpression ( (PLUS | MINUS) multExpression)*
//         ;

// multExpression
//         : expressionAtom (MULTIPLY expressionAtom)*
//         ;

// expressionAtom
// // options { backtrack=true; } // TODO is this required?
//         : identifier
//         | literal
//         | functionCall
//         | arrayReference
//         | OPEN_PAREN expression CLOSE_PAREN
//         ;

functionCall
        : identifier OPEN_PAREN expressionList CLOSE_PAREN
        ;

expressionList
        : expression exprMore*
        ;

arrayReference
        : identifier OPEN_BRACKET expression CLOSE_BRACKET
        ;

exprMore
        : COMMA expression
        ;

literal
        : STRING_CONSTANT
        | INTEGER_CONSTANT
        | FLOAT_CONSTANT
        | CHARACTER_CONSTANT
        | TRUE
        | FALSE
        ;
        
identifier 
        : ID
	;

type
        : TYPE
	;

/* Lexer */

// Punctuation
OPEN_PAREN
        : '('
        ;

CLOSE_PAREN
        : ')'
        ;

OPEN_BRACE
        : '{'
        ;

CLOSE_BRACE
        : '}'
        ;

OPEN_BRACKET
        : '['
        ;

CLOSE_BRACKET
        : ']'
        ;

COMMA  
        : ','
        ;

SEMICOLON  
        : ';'
        ;

EQUAL_ASSIGNMENT
        : '='
        ;

// Operators
EQUAL_COMPARISON
        : '=='
        ;

LESS_THAN
        : '<'
        ;

PLUS
        : '+'
        ;

MINUS
        : '-'
        ;

MULTIPLY
        : '*'
        ;

// Keywords
IF	: 'if'
	;

ELSE    : 'else'
        ;

WHILE   : 'while'
        ;

PRINT   : 'print'
        ;

PRINTLN : 'println'
        ;

RETURN : 'return'
        ;

TRUE    : 'true'
        ;

FALSE   : 'false'
        ;

// Type definitions
TYPE	: 
        'int' |
        'float' |
        'char' |
        'string' |
        'boolean' |
        'void'
	;

// Literal values
INTEGER_CONSTANT
        : DIGIT_FRAGMENT DIGIT_FRAGMENT*
        ;

FLOAT_CONSTANT
        : DIGIT_FRAGMENT DIGIT_FRAGMENT* '.' DIGIT_FRAGMENT+
        ;

fragment DIGIT_FRAGMENT
        : '1'..'9'
        ;

 // Do not allow empty strings.
STRING_CONSTANT
        : '"' CHARACTER_FRAGMENT+ '"'
        ;

CHARACTER_CONSTANT
        : '\'' CHARACTER_FRAGMENT '\''
        ;

fragment CHARACTER_FRAGMENT 
        : ('a'..'z' | 'A'..'Z' | '0'..'9' | '!' | '_' | ',' | '.' | ':' | '{' | '}')
        ;
/*
 * An identifier, shown as id in the grammar, is a sequence
 * of letters, digits and the underscore character. An 
 * identifier cannot start with a digit.
 */
ID	: ('a'..'z' | 'A'..'Z' | '_')('a'..'z' | 'A'..'Z' | '_' | '0'..'9')* 
	;

 // These lines match whitespace and comments (and ignore them).
 // Ensure they come after all other lexical rules.
WS      : ( '\t' | ' ' | ('\r' | '\n') )+ { $channel = HIDDEN;}
        ;

COMMENT : '//' ~('\r' | '\n')* ('\r' | '\n') { $channel = HIDDEN;}
        ;
