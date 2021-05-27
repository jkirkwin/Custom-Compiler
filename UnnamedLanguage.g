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
    formalNodes = new ArrayList<FormalParameter>();            
}
        : firstParamNode = formalParameter { formalNodes.add(firstParamNode); }
          (paramNode = moreFormals { formalNodes.add(paramNode);  } ) * 
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

functionBody returns [FunctionBody bodyNode]
@init {
    List<VariableDeclaration> varDecls = new ArrayList<VariableDeclaration>();
    List<Statement> stmts = new ArrayList<Statement>();
}
        :   OPEN_BRACE 
            (declNode = varDecl {varDecls.add(declNode);} )* 
            (stmtNode = statement {stmts.add(stmtNode);} )* 
            CLOSE_BRACE 
        {
            bodyNode = new FunctionBody(varDecls, stmts); 
        } 
	;

varDecl returns [VariableDeclaration varDeclNode]
        : typeNode = compoundType idNode = identifier SEMICOLON {
                varDeclNode = new VariableDeclaration(typeNode, idNode);
            }
        ;

compoundType returns [TypeNode typeNode]
        : node = type { typeNode = node; }
        | simpleType = type OPEN_BRACKET size = intLiteral CLOSE_BRACKET {
            // TODO Should construct a typenode here for the array type
            return simpleType;
          } 
        ;

statement returns [Statement stmtNode]
options { // TODO pretty sure this should be at the top of the file.
        backtrack=true; // Necessary to prevent recursion errors
}
        : SEMICOLON // TODO Return null in this case and check for it wherever we're using statement rule on lhs?
        | expression SEMICOLON // TODO 
        | ifElseStatement // TODO
        | whileStatement // TODO
        | printNode = printStatement { stmtNode = printNode; }
        | printlnStatement // TODO
        | returnStmtNode = returnStatement { stmtNode = returnStmtNode; }
        | assignmentStatement // TODO
        | arrayAssignmentStatement // TODO
        ;

ifElseStatement // TODO Add return for if/else stmt
        : IF OPEN_PAREN expression CLOSE_PAREN block (ELSE block)?
        ;

whileStatement // TODO Add return for while stmt
        : WHILE OPEN_PAREN expression CLOSE_PAREN block
        ;

printStatement returns [PrintStatement printStmtNode]
        : PRINT expNode = expression SEMICOLON 
            {
                int line = $PRINT.line, offset = $PRINT.pos; 
                printStmtNode = new PrintStatement(line, offset, expNode); 
            }
        ;

printlnStatement // TODO Add return for prinln stmt
        : PRINTLN expression SEMICOLON
        ;

returnStatement returns [ReturnStatement returnStmtNode]
        : RETURN expNode = expression? SEMICOLON 
            { 
                int line = $RETURN.line, offset = $RETURN.pos;
                if (expNode == null) {
                    System.out.println("Return statement with no expression found");
                    returnStmtNode = new ReturnStatement(line, offset);
                }
                else {
                    System.out.println("Return statement with expression found");
                    returnStmtNode = new ReturnStatement(line, offset, expNode);
                }
            }
        ;

assignmentStatement // TODO add return for assignment stmt
        : identifier EQUAL_ASSIGNMENT expression SEMICOLON
        ;

arrayAssignmentStatement // TODO add return for array assignment stmt
        : arrayReference EQUAL_ASSIGNMENT expression SEMICOLON
        ;

block returns [Block blockNode] // TODO Actually create a block here
        : OPEN_BRACE statement* CLOSE_BRACE
        ;

// We nest expressions which use binary operators in increasing order
// of precedence to enforce precedence rules and prevent left-recursion
// issues.
expression returns [Expression exprNode] // TODO Actually return something here. Careful to only create a new object when you need to.
        : lessThanExpression (EQUAL_COMPARISON lessThanExpression)* { exprNode = new Expression(); } // TODO remove this dummy value.
        ;

lessThanExpression // TODO Return expression for lessthan expr
        : plusMinusExpression (LESS_THAN plusMinusExpression)*
        ;

plusMinusExpression // TODO Return expression for +/- expr
        : multExpression ((PLUS | MINUS) multExpression)*
        ;

multExpression // TODO Return expression for mult expr
        : expressionAtom (MULTIPLY expressionAtom)*
        ;

expressionAtom // TODO Return expression for atom expr?
        : arrayReference
        | functionCall
        | identifier
        | literal
        | OPEN_PAREN expression CLOSE_PAREN
        ;

functionCall // TODO Return expression for function call expr
        : identifier OPEN_PAREN expressionList CLOSE_PAREN
        ;

expressionList // TODO return list of expressions
        : expression exprMore*
        | // Can be empty
        ;

arrayReference // TODO return expression for array ref
        : identifier OPEN_BRACKET expression CLOSE_BRACKET
        ;

exprMore
        : COMMA expression
        ;

literal // TODO Should return expression for literal
        : stringLiteral
        | intLiteral
        | floatLiteral
        | charLiteral
        | booleanLiteral
        ;

stringLiteral // TODO string literal
        : STRING_CONSTANT
        ;


intLiteral // TODO int literal
        : INTEGER_CONSTANT
        ;

floatLiteral // TODO float literal
        : FLOAT_CONSTANT
        ;


charLiteral // TODO char literal
        : CHARACTER_CONSTANT
        ;

booleanLiteral // TODO bool literal
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
