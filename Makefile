# Allows us to build the antlr infrastructure and the compiler

GNAME= UnnamedLanguage
GSRC= $(GNAME).g

JAVAC_OPTS= -Xlint:unchecked

all: grammar compiler

grammar: $(GSRCS)
	javac $(JAVAC_OPTS) ast/*.java type/*java
	java org.antlr.Tool -fo . $(GSRC) 

compiler:
	javac $(JAVAC_OPTS) semantic/*.java
	javac $(JAVAC_OPTS) *.java

clean:
	rm -f *.class $(GNAME)*.java $(GNAME).tokens ast/*.class type/*.class semantic/*.class

