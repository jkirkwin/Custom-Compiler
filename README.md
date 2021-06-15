# Custom-Compiler
A compiler for a simple C-inspired language. Built as part of CSC 435 (Compiler Construction) at UVic.

## Front-End Architecture

ANTLR generates a lexer and parser for us based on the grammar specified in `UnnamedLanguage.g`. This grammar has been annotated 
to create the abstract syntax tree (AST) manually rather than using ANTLR to do so directly for pedagogical reasons.
This manual process instantiates `ASTNode` objects found in the `ast` package and builds an AST structure by composition. 
The result of this process is a reference to a `ast.Program` instance which holds all other AST nodes as (possibly transitive) children.

Once the AST is created, it is operated on via the Visitor pattern.

To minimize both code duplication and error-prone down-casts, a generic `ASTVisitor` interface is provided in the `ast` package. This allows
multiple visitor implementations which serve different purposes to be used without requiring casting to handle specific visitor types
in the AST nodes themselves, and eliminates the need to down-cast from `Object` when processing the tree.
This approach does unfortunately force us to include `throws` clauses in the interface and `ASTNode`s' `accept` methods which are usually
not required. To minimize the ugliness here, an `ASTVisitorException` class is provided from which all checked exceptions thrown by a `visit`
or `accept` operation must inherit.

A `PrettyPrintVisitor` is provided for initial testing and is recommended as the starting place for viewers new to the Visitor pattern.

The infrastructure for semantic analysis including type checking is provided in the `semantic` package.
This includes a set of `ASTVisitorException`s thrown due to semantic issues with the source code being compiled and a `TypeCheckVisitor`
which contains the type checking and semantic analysis logic. Certain issues like missing return statements in complex branching structures
are not checked at this stage due to their complexity.

## Tooling

Lexing and parser generation is handled by ANTLR. Note that version 3.5.2 of ANTLR was used for pedagogical reasons. Current versions of ANTLR (4+) will not work with this source code package. 

## Compilation
To compile a .ul source file:
* Download (or build) an appropriate (v3.x.x) ANTLR Jar
* Add the ANTLR jar file to your `CLASSPATH`
* Build the compiler with `make`
* Compile an input file with `java Compiler <inputfile>`

## Testing

Several sample input files are included in `test-cases/`. Running `run_tests.sh` will attempt to compile each one. There are some false negatives being reported at this time for invalid input files which print error messages but do not throw an exception to indicate failure. This is an issue with the antlr infrastructure provided by the instructor and is up to them to resolve.

The lab machines do not appear to have JUnit installed and we are required to use Make rather than Gradle, so unit testing is being done manually using assertions in the source files' `main` methods. These unit tests can be run with `make all` or `make unit_test`.
