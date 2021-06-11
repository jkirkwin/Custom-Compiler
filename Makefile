# Allows us to build the antlr infrastructure and the compiler

GNAME= UnnamedLanguage
GSRC= $(GNAME).g

JAVAC_OPTS= -Xlint:unchecked

# Don't run the unit tests by default so there is no extra output
# for the marker.
app: compiler grammar 

all: grammar compiler

grammar: $(GSRCS)
	javac $(JAVAC_OPTS) ast/*.java type/*java
	java org.antlr.Tool -fo . $(GSRC) 

compiler: grammar
	javac $(JAVAC_OPTS) semantic/*.java
	javac $(JAVAC_OPTS) *.java

unit_test: compiler
	java -ea semantic.Environment

clean:
	rm -f *.class $(GNAME)*.java $(GNAME).tokens ast/*.class type/*.class semantic/*.class

