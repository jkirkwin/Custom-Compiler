# Builds the Compiler using ANTLR and the source files in this and sub-directories.

GNAME= UnnamedLanguage
GSRC= $(GNAME).g
PACKAGES= ast type semantic ir

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

semantic: ast type semantic/*.java
	$(JAVAC) semantic/*.java

ir: ast type ir/*.java
	$(JAVAC) ir/*.java

compiler: grammar $(PACKAGES) Compiler.java
	$(JAVAC) Compiler.java

unit_test: compiler
	java -ea semantic.Environment

clean:
	rm -f *.class $(GNAME)*.java $(GNAME).tokens 
	for pkg in $(PACKAGES) ; do rm -f ./$$pkg/*.class ; done
