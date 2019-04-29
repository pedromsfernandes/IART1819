import java.io.*;
import java.util.*;

public class Main {

    public static final Integer POPULATION_SIZE = 20;
    public static final Integer TOURNAMENT_SIZE = 4;
    public static final Double MUTATION_PROBABILITY = 0.9;
    public static final Integer BEST_VALUE = 0;
    public static final Integer ELITISM = 3;

    public static final Integer numSlots;
    public static final Integer numSubjets
    ArrayList<ArrayList<Integer>> studentsSubjects = new ArrayList<ArrayList<Integer>>();;

    public static void main(String[] args) throws IOException {

        Reader reader = new Reader();
        reader.readFile();

        this.numSlots = reader.numSlots;
        this.numSubjets = reader.numSubjets;
        this.studentsSubjects = reader.studentsSubjects;

        int c = 0;

        for(int i = 0; i < 20; i++) {
            ArrayList<ArrayList<Integer>> p = generatePopulation();
            ArrayList<Integer> bestSolution = getBestSolution(p);
            int generationCounter = 0;

            while (penalty(bestSolution) > BEST_VALUE) {
                p = getNextGeneration(p);
                bestSolution = getBestSolution(p);
                generationCounter++;
            }

            bestSolution = getBestSolution(p);
            c += generationCounter;
            System.out.println("ITERATION " + i);
        }
        System.out.println("C/20 IS " + c/20);

        return;
    }

    public static void printColisionTable(){
        int colisions;

        for(Integer i = 0; i < numSubjets; i++){
            for (Integer j = i+1; j< numSubjets; j++){
                colisions = compareSubjects(studentsSubjects.get(i), studentsSubjects.get(j));
                System.out.print("Classes " + (i+1) + " and " + (j+1) + ": ");
                System.out.println(colisions);
            }
        }
    }

    public static Integer penalty(ArrayList<Integer> solution) {
        int penalty = 0;

        for(Integer i = 0; i < solution.size(); i++) {
            for (Integer j = i + 1; j < solution.size(); j++) {
                if(solution.get(i).equals(solution.get(j))){
                    penalty += compareSubjects(studentsSubjects.get(i), studentsSubjects.get(j));
                }
            }
        }

        return penalty;
    }

    private static ArrayList<ArrayList<Integer>> getNextGeneration(ArrayList<ArrayList<Integer>> population){
        ArrayList<ArrayList<Integer>> newGeneration = new ArrayList<ArrayList<Integer>>();

        if(ELITISM > 0) {
            Collections.sort(population, Comparator.comparing(s -> penalty(s)));
            for (int j = 0; j < ELITISM; j++) {
                newGeneration.add(population.get(j));
            }
        }

        for(int i = 0; i < POPULATION_SIZE - ELITISM; i++) {
            ArrayList<Integer> individual1 = tournament(population);
            ArrayList<Integer> individual2 = tournament(population);
            ArrayList<Integer> child = crossover(individual1, individual2);
            ArrayList<Integer> newChild = mutation(child);
            newGeneration.add(newChild);
        }

        return newGeneration;
    }

    private static ArrayList<Integer> generateSolutions() {

        ArrayList<Integer> solution = new ArrayList<Integer>();
        Random rand = new Random();

        for(int i = 0; i < numSubjets; i++){
            int slot = rand.nextInt(numSlots);
            solution.add(i, slot + 1);
        }

        return solution;
    }

    private static ArrayList<ArrayList<Integer>> generatePopulation() {
        ArrayList<ArrayList<Integer>> population = new ArrayList<ArrayList<Integer>>();

        for (int i = 0; i < POPULATION_SIZE; i++){
            ArrayList<Integer> solution = generateSolutions();
            population.add(solution);
        }

        return population;
    }

    private static ArrayList<Integer> tournament(ArrayList<ArrayList<Integer>> population) {
        Collections.shuffle(population);
        ArrayList<ArrayList<Integer>> subPopulation = new ArrayList<ArrayList<Integer>>();

        for(int i = 0; i < TOURNAMENT_SIZE; i++){
            subPopulation.add(population.get(i));
        }

        return getBestSolution(subPopulation);
    }

    private static ArrayList<Integer> mutation (ArrayList<Integer> solution) {
        Random rand = new Random();
        Double p = rand.nextDouble();

        if(p < MUTATION_PROBABILITY){
            ArrayList<Integer> newSolution = new ArrayList<Integer>(solution);
            int index = rand.nextInt(numSubjets);
            int mutatedGene = rand.nextInt(numSlots) + 1;

            newSolution.set(index, mutatedGene);

            return newSolution;
        }

        return solution;
    }

    private static ArrayList<Integer> crossover (ArrayList<Integer> solution1, ArrayList<Integer> solution2){
        ArrayList<Integer> newSolution = new ArrayList<Integer>(solution1);

        for(int i = 0; i < solution1.size(); i++) {
            if(i%2 == 0){
                newSolution.set(i, solution2.get(i));
            }
        }

        return newSolution;
    }

    private static Integer compareSubjects(ArrayList<Integer> subject1, ArrayList<Integer> subject2) {
        Integer counter = 0;

        for(int i = 0; i < subject1.size(); i++) {
            for (int j = 0; j < subject2.size(); j++) {
                if(subject1.get(i).equals(subject2.get(j))){
                    counter++;
                }
            }
        }

        return counter;
    }

    private static ArrayList<Integer> getBestSolution(ArrayList<ArrayList<Integer>> population) {
        int index = 0;
        int best = penalty(population.get(0));

        for(int i = 1; i < population.size(); i++) {
            int newValue = penalty(population.get(i));
            if(newValue < best) {
                index = i;
                best = newValue;
            }
        }

        return population.get(index);
    }

    private static void printSolution(ArrayList<Integer> solution) {
        for(Integer i: solution) {
            System.out.print(i + ", ");
        }
    }

}
