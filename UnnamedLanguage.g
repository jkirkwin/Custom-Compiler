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
            (stmtNode = statement   
                {
                    if (stmtNode != null) {
                        stmts.add(stmtNode);
                    }
                } 
            )* 
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
options {
        backtrack=true; // Necessary to prevent recursion errors
}
        : SEMICOLON
        | expNode = expression SEMICOLON { stmtNode = new ExpressionStatement(expNode); }
        | ifStmtNode = ifElseStatement { stmtNode = ifStmtNode; }
        | whileNode = whileStatement { stmtNode = whileNode; }
        | printNode = printStatement { stmtNode = printNode; }
        | printlnNode = printlnStatement { stmtNode = printlnNode; }
        | returnStmtNode = returnStatement { stmtNode = returnStmtNode; }
        | assignStmtNode = assignmentStatement { stmtNode = assignStmtNode; }
        | arrayAssignStmtNode = arrayAssignmentStatement { stmtNode = arrayAssignStmtNode; } 
        ;

ifElseStatement returns [IfStatement ifStmtNode]
        : IF OPEN_PAREN expNode = expression CLOSE_PAREN ifBlockNode = block (ELSE elseBlockNode = block)?
            {
                int line = $IF.line, offset = $IF.pos;
                if (elseBlockNode != null) {
                    ifStmtNode = new IfStatement(line, offset, expNode, ifBlockNode, elseBlockNode);
                }
                else {
                    ifStmtNode = new IfStatement(line, offset, expNode, ifBlockNode);
                }
            }
        ;

whileStatement returns [WhileStatement whileNode]
        : WHILE OPEN_PAREN expNode = expression CLOSE_PAREN blockNode = block 
            {
                int line = $WHILE.line, offset = $WHILE.pos;
                whileNode = new WhileStatement(line, offset, expNode, blockNode);
            }
        ;

printStatement returns [PrintStatement printStmtNode]
        : PRINT expNode = expression SEMICOLON 
            {
                int line = $PRINT.line, offset = $PRINT.pos; 
                printStmtNode = new PrintStatement(line, offset, expNode); 
            }
        ;

printlnStatement returns [PrintlnStatement printlnNode]
        : PRINTLN expNode = expression SEMICOLON 
            {
                int line = $PRINTLN.line, offset = $PRINTLN.pos;
                printlnNode = new PrintlnStatement(line, offset, expNode);
            }
        ;

returnStatement returns [ReturnStatement returnStmtNode]
        : RETURN expNode = expression? SEMICOLON 
            { 
                int line = $RETURN.line, offset = $RETURN.pos;
                if (expNode == null) {
                    returnStmtNode = new ReturnStatement(line, offset);
                }
                else {
                    returnStmtNode = new ReturnStatement(line, offset, expNode);
                }
            }
        ;

assignmentStatement returns [AssignmentStatement assignStmtNode]
        : idNode = identifier EQUAL_ASSIGNMENT expNode = expression SEMICOLON 
            {
                   assignStmtNode = new AssignmentStatement(idNode, expNode);
            }
        ;

arrayAssignmentStatement returns [ArrayAssignmentStatement arrayAssignStmtNode]
        : arrayIdNode = identifier OPEN_BRACKET indexExpNode = expression CLOSE_BRACKET 
            EQUAL_ASSIGNMENT valueExpNode = expression SEMICOLON
            {
                arrayAssignStmtNode = new ArrayAssignmentStatement(arrayIdNode, indexExpNode, valueExpNode);
            }
        ;

block returns [Block blockNode]
@init {
    List<Statement> statements = new ArrayList<Statement>();
}
        : OPEN_BRACE (stmtNode = statement 
            {
                if (stmtNode != null) {
                    statements.add(stmtNode);
                }         
            }
          ) * CLOSE_BRACE
            {
                int line = $OPEN_BRACE.line, offset = $OPEN_BRACE.pos;
                blockNode = new Block(line, offset, statements);
            }
        ;

// We nest expressions which use binary operators in increasing order
// of precedence to enforce precedence rules and prevent left-recursion
// issues.
expression returns [Expression exprNode]
        : lhs = lessThanExpression {exprNode = lhs;}  
          (
            eq = EQUAL_COMPARISON nextOperand = lessThanExpression { 
                exprNode = new EqualityExpression($eq.line, $eq.pos, exprNode, nextOperand);
            }
          )*
        ;

lessThanExpression returns [Expression exprNode] 
        : left = plusMinusExpression { exprNode = left; } 
          (
            lt = LESS_THAN nextOperand = plusMinusExpression {
                exprNode = new LessThanExpression($lt.line, $lt.pos, exprNode, nextOperand);
            }
          )*
        ;

plusMinusExpression returns [Expression exprNode]
        : left = multExpression { exprNode = left; }
            ( 
                (plusToken = PLUS | minusToken = MINUS )
                
                nextOperand = multExpression { 
                    // Only one of the token variables should have a value.
                    assert (plusToken == null) != (minusToken == null);
                    
                    // Determine the position of the operator and create a new node for 
                    // the most recently parsed operator.
                    int line, offset;
                    if (plusToken != null) {
                        line = $plusToken.line;
                        offset = $plusToken.pos;
                        exprNode = new AddExpression(line, offset, exprNode, nextOperand);
                    }
                    else {
                        line = $minusToken.line;
                        offset = $minusToken.pos;
                        exprNode = new SubtractExpression(line, offset, exprNode, nextOperand);
                    }   
                } 
            )*
        ;

multExpression returns [Expression exprNode]
        : left = expressionAtom { 
                // If there is no operator, just pass the node up to the next level.
                exprNode = left; 
          }
          (
                MULTIPLY nextOperand = expressionAtom {
                        // Each time we find an operator, make a new binary node with the 
                        // last one as the left child.
                        int line = $MULTIPLY.line;
                        int offset =  $MULTIPLY.pos; 
                        exprNode = new MultiplyExpression(line, offset, exprNode, nextOperand);
                }
          )*
        ;

expressionAtom returns [Expression exprNode]
        : arrayRefNode = arrayReference {exprNode = arrayRefNode;}
        | funcCallNode = functionCall {exprNode = funcCallNode;}
        | idNode = identifier {exprNode = idNode;}
        | litExprNode = literal {exprNode = litExprNode;}
        | OPEN_PAREN e = expression CLOSE_PAREN {exprNode = new ParenExpression($OPEN_PAREN.line, $OPEN_PAREN.pos, e);} 
        ;

functionCall returns [FunctionCall funcCallNode]
        : idNode = identifier OPEN_PAREN exprList = expressionList CLOSE_PAREN {
                funcCallNode = new FunctionCall(idNode, exprList);
            }
        ;

expressionList returns [List<Expression> expressions]
@init {
    expressions = new ArrayList<Expression>();
}
        : firstExprNode = expression {expressions.add(firstExprNode); } 
          (
            subsequentExprNode = exprMore {expressions.add(subsequentExprNode);} 
          )*
        | // Can be empty
        ;

arrayReference returns [Expression arrayRefNode]
        : id = identifier OPEN_BRACKET indexExpNode = expression CLOSE_BRACKET
            {
                arrayRefNode = new ArrayReference(id, indexExpNode);
            }
        ;

exprMore returns [Expression exprNode]
        : COMMA e = expression {exprNode = e;}
        ;

literal returns [Expression exprNode]
        : s = stringLiteral {exprNode = s;}
        | i = intLiteral {exprNode = i;}
        | f = floatLiteral {exprNode = f;}
        | c = charLiteral {exprNode = c;}
        | b = booleanLiteral {exprNode = b;}
        ;

stringLiteral returns [StringLiteral stringLiteralNode] 
        : STRING_CONSTANT { 
                int line=$STRING_CONSTANT.line, offset=$STRING_CONSTANT.pos;                

                // Remove the outer quotes from the string for storage
                String quotedText = $STRING_CONSTANT.text;
                assert (quotedText.length() >= 2);
                String rawText = quotedText.substring(1, quotedText.length() - 1);
                 
                stringLiteralNode = new StringLiteral(line, offset, rawText);
            }
        ;


intLiteral returns [IntLiteral intLiteralNode]
        : INTEGER_CONSTANT {
                int line=$INTEGER_CONSTANT.line, offset=$INTEGER_CONSTANT.pos;                

                String valueText = $INTEGER_CONSTANT.text;
                int value = Integer.parseInt(valueText);
                
                intLiteralNode = new IntLiteral(line, offset, value);
            }
        ;

floatLiteral returns [FloatLiteral floatLiteralNode]
        : FLOAT_CONSTANT {
                int line=$FLOAT_CONSTANT.line, offset=$FLOAT_CONSTANT.pos;                

                String valueText = $FLOAT_CONSTANT.text;
                float value = Float.parseFloat(valueText);
                
                floatLiteralNode = new FloatLiteral(line, offset, value);
            }
        ;


charLiteral returns [CharacterLiteral charLiteralNode]
        : CHARACTER_CONSTANT {

                int line=$CHARACTER_CONSTANT.line, offset=$CHARACTER_CONSTANT.pos;        
                
                String valueText = $CHARACTER_CONSTANT.text;
                assert valueText.length() == 3;
                char value = valueText.charAt(1);
                
                charLiteralNode = new CharacterLiteral(line, offset, value);
         }
        ;

booleanLiteral returns [BooleanLiteral boolLiteralNode]
        : TRUE { boolLiteralNode = new BooleanLiteral($TRUE.line, $TRUE.pos, true); }
        | FALSE { boolLiteralNode = new BooleanLiteral($FALSE.line, $FALSE.pos, false); }
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
