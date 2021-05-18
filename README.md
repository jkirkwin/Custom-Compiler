# Custom-Compiler
A compiler for a simple C-inspired language. Built as part of CSC 435 (Compiler Construction) at UVic.

## Tooling

Lexing and parser generation is handled by ANTLR. Note that version 3.5.2 of ANTLR was used for pedagogical reasons. Current versions of ANTLR (4+) will not work with this source code package. 

## Running the code

* Download (or build) an appropriate (v3.*.*) ANTLR Jar
* Add the ANTLR jar file to your `CLASSPATH`
* Build the compiler with `make`
* Compile an input file with `java Compiler <inputfile>`
