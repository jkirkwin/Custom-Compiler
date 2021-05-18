grammar UnnamedLanguage;

@members
{
        protected void mismatch (IntStream input, int ttype, BitSet follow) throws RecognitionException
        {
                throw new MismatchedTokenException(ttype, input);
        }
        public Object recoverFromMismatchedSet (IntStream input, RecognitionException e, BitSet follow) 
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

// Starting production
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

statement 
options {
        backtrack=true; // Necessary to prevent recursion errors
}
        : SEMICOLON
        | expression SEMICOLON
        | ifElseStatement
        | whileStatement
        | printStatement
        | printlnStatement
        | returnStatement
        | assignmentStatement
        | arrayAssignmentStatement
        ;

ifElseStatement
        : IF OPEN_PAREN expression CLOSE_PAREN block (ELSE block)?
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
        : RETURN expression? SEMICOLON
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

// We nest expressions which use binary operators in increasing order
// of precedence to enforce precedence rules and prevent left-recursion
// issues.
expression
        : lessThanExpression (EQUAL_COMPARISON lessThanExpression)*
        ;

lessThanExpression
        : plusMinusExpression (LESS_THAN plusMinusExpression)*
        ;

plusMinusExpression
        : multExpression ((PLUS | MINUS) multExpression)*
        ;

multExpression
        : expressionAtom (MULTIPLY expressionAtom)*
        ;

expressionAtom
        : arrayReference
        | functionCall
        | identifier
        | literal
        | OPEN_PAREN expression CLOSE_PAREN
        ;

functionCall
        : identifier OPEN_PAREN expressionList CLOSE_PAREN
        ;

expressionList
        : expression exprMore*
        | // Can be empty
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
        // Note that this allows leading 0's
        : DIGIT_FRAGMENT DIGIT_FRAGMENT* 
        ;

FLOAT_CONSTANT
        // Note that this allows leading 0's
        : DIGIT_FRAGMENT DIGIT_FRAGMENT* '.' DIGIT_FRAGMENT+ 
        ;

fragment DIGIT_FRAGMENT
        : '0'..'9'
        ;

STRING_CONSTANT
        : '"' CHARACTER_FRAGMENT+ '"' // We do not allow empty strings.
        ;

CHARACTER_CONSTANT
        : '\'' CHARACTER_FRAGMENT '\''
        ;

fragment CHARACTER_FRAGMENT 
        : ('a'..'z' | 'A'..'Z' | '0'..'9' | '!' | '_' | ',' | '.' | ':' | '{' | '}' | ' ')
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
