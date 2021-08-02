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

## Back-end architecture

An intermediate representation is prescribed by the instructor. It defines the IR format to be used to generate bytecode for the JVM. The IR is designed specifically to make this translation process feasible on the course's tight schedule by making it reasonably similar in structure to Java bytecode. 

After semantic checking is complete (assuming there are no errors), the `ir.IRAstVisitor` class is used to traverse the AST to produce an `ir.IRProgram` object. This `IRProgram` object is then traversed by another visitor in the `codegen` package, which coverts each IR instruction/construct to corresponding Jasmin assembly code and writes the result to a Jasmin assembly file. Jasmin can then be used to convert the .j file to a runnable .class file.

## Tooling

Lexing and parser generation is handled by ANTLR. Note that version 3.5.2 of ANTLR was used for pedagogical reasons. Current versions of ANTLR (4+) will not work with this source code package. 

Class file assembly is performed using Jasmin. See http://jasmin.sourceforge.net/guide.html for details.

## Compilation
To compile a .ul source file:
* Download (or build) an appropriate (v3.x.x) ANTLR Jar
* Add the ANTLR jar file to your `CLASSPATH`
* Add a Jasmin jar file to your `CLASSPATH`
* Build the compiler with `make`
* Compile an input file with `java Compiler <inputfile>` to produce a .j file
* Run `java jasmin.Main <foo.j>` to produce `foo.class`, which can be run with `java foo` 

## Testing

Several sample input files are included in `test-cases/`. Running `run_tests.sh` will attempt to compile and run each one.

The lab machines do not appear to have JUnit installed and we are required to use Make rather than Gradle, so unit testing is being done manually using assertions in the source files' `main` methods. These unit tests can be run with `make all` or `make unit_test`.
