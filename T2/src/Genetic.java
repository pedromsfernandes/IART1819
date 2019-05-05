import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Genetic {
    public static final Integer POPULATION_SIZE = 20;
    public static final Integer TOURNAMENT_SIZE = 4;
    public static final Double MUTATION_PROBABILITY = 0.9;
    public static final Integer BEST_VALUE = 3;
    public static final Integer ELITISM = 3;

    public static ArrayList<Exam> exams = new ArrayList<>();
    public static ArrayList<Period> periods = new ArrayList<>();
    public static ArrayList<Room> rooms = new ArrayList<>();

    public static Problem problemInstance;

    public static void geneticSolve(Problem problem) {
        exams = problem.getExams();
        periods = problem.getPeriods();
        rooms = problem.getRooms();
        problemInstance = problem;

        ArrayList<ArrayList<String>> population = generatePopulation(exams.size(), periods.size(), rooms.size());
        ArrayList<String> bestSolution = getBestSolution(population);

        System.out.print("BEST FROM INITIAL POPULATION:  ");
        printSolution(bestSolution);
        System.out.println("\nVALUE: " + problem.evaluate(bestSolution));

        int generationCounter = 0;

        while (problem.evaluate(bestSolution) > BEST_VALUE) {
            population = getNextGeneration(population);
            bestSolution = getBestSolution(population);
            generationCounter++;
        }

        System.out.print("\nBEST FROM FINAL POPULATION:  ");
        bestSolution = getBestSolution(population);
        printSolution(bestSolution);
        System.out.println("\nVALUE: " + problem.evaluate(bestSolution));

        System.out.println("Number of generations: " + generationCounter);

        return;
    }

    // COMPLETE
    private static ArrayList<ArrayList<String>> generatePopulation(int numExams, int numTimeslots, int numRooms) {
        ArrayList<ArrayList<String>> population = new ArrayList<ArrayList<String>>();

        for (int i = 0; i < POPULATION_SIZE; i++){
            ArrayList<String> solution = getRandomSolution(numExams, numTimeslots, numRooms);
            population.add(solution);
        }

        return population;
    }

    // COMPLETE
    public static ArrayList<String> getRandomSolution(int numExams, int numTimeslots, int numRooms) {
        ArrayList<String> solution = new ArrayList<String>();

        for (int i = 0; i < numExams; i++) {
            int timeslot = randInt(0, numTimeslots);
            int room = randInt(0, numRooms);

            solution.add(timeslot + ", " + room);
        }

        return solution;
    }

    // COMPLETE
    private static ArrayList<ArrayList<String>> getNextGeneration(ArrayList<ArrayList<String>> population){
        ArrayList<ArrayList<String>> newGeneration = new ArrayList<ArrayList<String>>();

        if(ELITISM > 0) {
            Collections.sort(population, Comparator.comparing(s -> problemInstance.evaluate(s)));
            for (int j = 0; j < ELITISM; j++) {
                newGeneration.add(population.get(j));
            }
        }

        for(int i = 0; i < POPULATION_SIZE - ELITISM; i++) {
            ArrayList<String> individual1 = tournament(population);
            ArrayList<String> individual2 = tournament(population);
            ArrayList<String> child = crossover(individual1, individual2);
            ArrayList<String> newChild = mutation(child);
            newGeneration.add(newChild);
        }

        return newGeneration;
    }

    // COMPLETE
    private static ArrayList<String> tournament(ArrayList<ArrayList<String>> population) {
        Collections.shuffle(population);
        ArrayList<ArrayList<String>> subPopulation = new ArrayList<ArrayList<String>>();

        for(int i = 0; i < TOURNAMENT_SIZE; i++){
            subPopulation.add(population.get(i));
        }

        return getBestSolution(subPopulation);
    }

    // TODO
    private static ArrayList<String> mutation (ArrayList<String> solution) {
        Random rand = new Random();
        Double p = rand.nextDouble();
        Boolean mutationTarget = false; // true for room, false for period

        if (p < 0.5) { // 50% chance of mutating a room or a period
            mutationTarget = true;
        }

        p = rand.nextDouble();

        if(p < MUTATION_PROBABILITY){
            ArrayList<String> newSolution = new ArrayList<String>(solution);
            int index = rand.nextInt(exams.size());

            if(mutationTarget) {
                
            }

            /*
            int mutatedGene = rand.nextInt(reader.numSlots) + 1;
            newSolution.set(index, mutatedGene);
             */

            return newSolution;
        }

        return solution;
    }

    // TODO: Return two solutions instead of one
    private static ArrayList<String> crossover (ArrayList<String> solution1, ArrayList<String> solution2){
        ArrayList<String> newSolution = new ArrayList<String>(solution1);

        for(int i = 0; i < solution1.size(); i++) {
            if(i%2 == 0){
                newSolution.set(i, solution2.get(i));
            }
        }

        return newSolution;
    }

    // COMPLETE
    private static ArrayList<String> getBestSolution(ArrayList<ArrayList<String>> population) {
        int index = 0;
        int best = problemInstance.evaluate(population.get(0));

        for(int i = 1; i < population.size(); i++) {
            int newValue = problemInstance.evaluate(population.get(i));
            if(newValue < best) {
                index = i;
                best = newValue;
            }
        }

        return population.get(index);
    }

    // TODO
    private static void printSolution(ArrayList<String> solution) {
        for(String i: solution) {
            System.out.print(i + ", ");
        }
    }

    // AUXILIAR
    public static int randInt(int min, int max) {

        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
