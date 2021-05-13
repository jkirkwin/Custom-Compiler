# Test Cases

This directory tree holds a set of 3 types of possible program files using the Unnamed Language.

`invalid` holds test files which are not valid programs and which will not pass the lexing/parsing stages of compilation.

`valid` holds test files which will pass the lexing/parsing stages of compilation. That is, all these files belong to the language specified by the grammar; however, they may not pass subsequent stages like type checking.

`valid/accept` contains test files which are valid programs that can be fully compiled and run.

`valid/reject` contains test files which belong to the language specified by the grammar, but which cannot be compiled due to some semantic issue.
