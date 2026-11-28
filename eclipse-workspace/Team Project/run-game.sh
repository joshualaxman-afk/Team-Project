#!/bin/sh

# REMARK:
# Eclipse was so hard to work with that I am doing what I did in high
# school. What a crap program; unbelievable that it's still maintained,
# let alone used as the flagship standard program at a college. Writing
# a shell script is *easier* than dealing with Eclipse. What an
# embarrassment for Eclipse.
javac -cp algs4.jar -d bin src/TeamProject/*.java

# NOTE: Parenthesis performs the same thing that SetLocal/EndLocal does in Batch.
(cd bin && jar cf TeamProject/TeamProject.jar TeamProject/*.class)
java -cp algs4.jar:bin/TeamProject/TeamProject.jar TeamProject.Game

