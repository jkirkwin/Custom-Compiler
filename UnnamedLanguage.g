grammar UnnamedLanguage;

@header 
{
        import ast.*;
}

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
program returns [Program programNode] 
@init {
    System.out.println("Creating list of functions");    
    List<Function> functions = new ArrayList<Function>();
}
        : (f = function { functions.add(f); })+
        { 
                
                programNode = new Program(functions);
        } 
        EOF
	;

function returns [Function functionNode] 
        : decl=functionDecl body=functionBody 
        { 
            functionNode = new Function(decl, body); 
        }
	;

functionDecl returns [FunctionDecl declNode]
        : typeNode = compoundType 
          idNode = identifier 
          OPEN_PAREN 
          formalsList = formalParameters 
          CLOSE_PAREN 
        { declNode = new FunctionDecl(typeNode, idNode, formalsList); } // TODO Update type param?
	;

formalParameters returns [List<FormalParameter> formalNodes]
@init {
    System.out.println("Creating formals List<>.");
    formalNodes = new ArrayList<FormalParameter>();            
}
        : firstParamNode = formalParameter 
            { formalNodes.add(firstParamNode); System.out.println("Added first param to formals list"); }
          moreResult = moreFormals* // TODO Add the results of this to the list. We have the same problem here as we did with the list of functions.
        | // Can be empty
        ;

moreFormals returns [FormalParameter paramNode]
        : COMMA node = formalParameter 
            { 
                paramNode = node; 
            }
        ;

formalParameter returns [FormalParameter formalParamNode]
        : compoundTypeNode = compoundType
          idNode = identifier
            {
                formalParamNode = new FormalParameter(compoundTypeNode, idNode);
            }
        ; 

functionBody returns [FunctionBody b]
        : OPEN_BRACE varDecl* statement* CLOSE_BRACE 
        {
            b = new FunctionBody(); 
        } // TODO Add parameters for the function body
	;

varDecl
        : compoundType identifier SEMICOLON
        ;

compoundType returns [TypeNode typeNode]
        : node = type { typeNode = node; }
        | simpleType = type OPEN_BRACKET size = intLiteral CLOSE_BRACKET {
            // TODO Should construct a typenode here for the full compound type
            return simpleType;
          } 
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
        : stringLiteral
        | intLiteral
        | floatLiteral
        | charLiteral
        | booleanLiteral
        ;

stringLiteral
        : STRING_CONSTANT
        ;


intLiteral
        : INTEGER_CONSTANT
        ;

floatLiteral
        : FLOAT_CONSTANT
        ;


charLiteral
        : CHARACTER_CONSTANT
        ;

booleanLiteral
        : TRUE
        | FALSE
        ;

identifier returns [Identifier idNode]
        : ID { idNode = new Identifier($ID.text, $ID.line, $ID.pos); }
	;

type returns [TypeNode t]
        : TYPE 
        {
            // TODO Create a TypeNode instance with the proper Type parameter instead of the string 
            String typeString = $TYPE.text;
            t = new TypeNode(typeString, $TYPE.line, $TYPE.pos);
        }
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
