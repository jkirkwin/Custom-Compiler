# Builds the Compiler using ANTLR and the source files in this and sub-directories.

GNAME= UnnamedLanguage
GSRC= $(GNAME).g
PACKAGES= common ast type semantic ir codegen

JAVAC_OPTS= -Xlint:unchecked
JAVAC= javac $(JAVAC_OPTS)

# Don't run the unit tests by default so there is no extra output
# for the marker.
app: compiler grammar 

all: grammar compiler unit_test

grammar: $(GSRCS) ast type
	java org.antlr.Tool -fo . $(GSRC) 

ast: type ./ast/*.java
	$(JAVAC) ast/*.java

type: ./type/*.java
	$(JAVAC) type/*.java

semantic: ast type common semantic/*.java
	$(JAVAC) semantic/*.java

ir: ast type common ir/*.java
	$(JAVAC) ir/*.java

codegen: ir common codegen/*.java
	$(JAVAC) codegen/*.java

common: common/*.java
	$(JAVAC) common/*.java

compiler: grammar $(PACKAGES) Compiler.java
	$(JAVAC) Compiler.java

unit_test: compiler
	java -ea semantic.Environment
	java -ea ir.TempFactory

clean:
	rm -f *.class $(GNAME)*.java $(GNAME).tokens 
	for pkg in $(PACKAGES) ; do rm -f ./$$pkg/*.class ; done
