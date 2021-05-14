grammar UnnamedLanguage;
				
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
        : COMMA compoundType identifier moreFormals* 
        ;

functionBody
        : OPEN_BRACE varDecl* statement* CLOSE_BRACE
	;

varDecl
        : compoundType identifier SEMICOLON
        ;

compoundType
        : type 
        | type OPEN_BRACE INTEGER_CONSTANT CLOSE_BRACE
        ;

statement
        : SEMICOLON
        | expression SEMICOLON
        | IF OPEN_PAREN expression CLOSE_PAREN block
        | IF OPEN_PAREN expression CLOSE_PAREN block ELSE block
        | WHILE OPEN_PAREN expression CLOSE_PAREN block
        | PRINT expression
        | PRINTLN expression
        | RETURN expression
        | identifier EQUAL_ASSIGNMENT expression SEMICOLON
        | identifier OPEN_BRACKET expression CLOSE_BRACKET EQUAL_ASSIGNMENT expression SEMICOLON
        ;

block
        : OPEN_BRACE expression CLOSE_BRACE
        ;

// TODO refactor expression and operator to deal with left-recursion and operator precedence 
expression
        : expression operator expression
        | identifier OPEN_BRACKET expression CLOSE_BRACKET
        | identifier OPEN_PAREN expressionList CLOSE_PAREN
        | identifier
        | literal
        | OPEN_PAREN expression CLOSE_PAREN
        ;

operator
        : 
        | EQUAL_COMPARISON
        | LESS_THAN
        | PLUS
        | MINUS
        | MULTIPLY
        ;

expressionList
        : expression exprMore*
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
        : ('1'..'9')('0'..'9')*
        ;

FLOAT_CONSTANT
        : ('1'..'9')('0'..'9')* '.' ('0'..'9')+
        ;

 // TODO allow empty strings?
STRING_CONSTANT
        : '"' ('a'..'z' | 'A'..'Z' | '0'..'9' | '!' | '_' | ',' | '.' | ':' | '{' | '}')* '"'
        ;

CHARACTER_CONSTANT
        : '\'' ('a'..'z' | 'A'..'Z' | '0'..'9' | '!' | '_' | ',' | '.' | ':' | '{' | '}') '\''
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
