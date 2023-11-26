JFLAGS = -g
JC = javac
JVM = java
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = projet/View.java projet/Model.java projet/Controller.java

MAIN = projet/Controller.java
default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) projet/*.class

run: classes
	$(JVM) $(MAIN)
