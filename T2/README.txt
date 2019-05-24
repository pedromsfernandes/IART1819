COMPILAR

cd src/main/java
javac *.java

CORRER

java Main # sem argumentos retorna usage

Usage: java Main <FILENAME> <ALGORITHM>
Where ALGORITHM can be one of the following: GENETIC, HILLCLIMB_SIMPLE, HILLCLIMB_STEEPEST or ANNEALING
Example: java Main ../res/exam_comp_set1.exam GENETIC

UTILIZAR

Consultar valores retornados pela função evaluate no ecrã, e o resultado na mesma pasta (res), com mesmo nome mas extensão .sln.