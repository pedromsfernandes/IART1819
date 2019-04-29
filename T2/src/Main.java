import java.io.*;
import java.util.*;

public class Main {

    public static final Integer POPULATION_SIZE = 20;
    public static final Integer TOURNAMENT_SIZE = 4;
    public static final Double MUTATION_PROBABILITY = 0.9;
    public static final Integer BEST_VALUE = 0;
    public static final Integer ELITISM = 3;

    public static void main(String[] args) throws IOException {

        Reader.getInstance().readFile(args[0]);



        return;
    }



    // TODO
    public static Integer penalty(ArrayList<Integer> solution) {
        int penalty = 0;

        return penalty;
    }

    // COMPLETE
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

    // TODO
    private static ArrayList<Integer> generateSolutions() {

        ArrayList<Integer> solution = new ArrayList<Integer>();
        Random rand = new Random();

        return solution;
    }

    // COMPLETE
    private static ArrayList<ArrayList<Integer>> generatePopulation() {
        ArrayList<ArrayList<Integer>> population = new ArrayList<ArrayList<Integer>>();

        for (int i = 0; i < POPULATION_SIZE; i++){
            ArrayList<Integer> solution = generateSolutions();
            population.add(solution);
        }

        return population;
    }

    // COMPLETE
    private static ArrayList<Integer> tournament(ArrayList<ArrayList<Integer>> population) {
        Collections.shuffle(population);
        ArrayList<ArrayList<Integer>> subPopulation = new ArrayList<ArrayList<Integer>>();

        for(int i = 0; i < TOURNAMENT_SIZE; i++){
            subPopulation.add(population.get(i));
        }

        return getBestSolution(subPopulation);
    }

    // TODO
    private static ArrayList<Integer> mutation (ArrayList<Integer> solution) {
        Random rand = new Random();
        Double p = rand.nextDouble();

        if(p < MUTATION_PROBABILITY){
            //return newSolution;
        }

        return solution;
    }

    // TODO
    private static ArrayList<Integer> crossover (ArrayList<Integer> solution1, ArrayList<Integer> solution2){
        ArrayList<Integer> newSolution = new ArrayList<Integer>(solution1);

        return newSolution;
    }

    // TODO
    private static Integer compareSubjects(ArrayList<Integer> subject1, ArrayList<Integer> subject2) {
        Integer counter = 0;

        return counter;
    }

    // COMPLETE
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

    // AUXILIAR
    private static void printSolution(ArrayList<Integer> solution) {
        for(Integer i: solution) {
            System.out.print(i + ", ");
        }
    }

}
