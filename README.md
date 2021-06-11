# Custom-Compiler
A compiler for a simple C-inspired language. Built as part of CSC 435 (Compiler Construction) at UVic.

## Tooling

Lexing and parser generation is handled by ANTLR. Note that version 3.5.2 of ANTLR was used for pedagogical reasons. Current versions of ANTLR (4+) will not work with this source code package. 

## Compilation
To compile a .ul source file:
* Download (or build) an appropriate (v3.*.*) ANTLR Jar
* Add the ANTLR jar file to your `CLASSPATH`
* Build the compiler with `make`
* Compile an input file with `java Compiler <inputfile>`

## Testing

Several sample input files are included in `test-cases/`. Running `run_tests.sh` will attempt to compile each one. There are some false negatives being reported at this time for invalid input files which print error messages but do not throw an exception to indicate failure. This is an issue with the antlr infrastructure provided by the instructor and is up to them to resolve.

The lab machines do not appear to have JUnit installed and we are required to use Make rather than Gradle, so unit testing is being done manually using assertions in the source files' `main` methods. These unit tests can be run with `make all` or `make unit_test`.
