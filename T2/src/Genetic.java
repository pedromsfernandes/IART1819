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

    public static ArrayList<Exam> geneticSolve(Problem problem) {
        exams = problem.getExams();
        periods = problem.getPeriods();
        rooms = problem.getRooms();
        problemInstance = problem;

        ArrayList<ArrayList<Exam>> population = generatePopulation(problem);
        ArrayList<Exam> bestSolution = getBestSolution(population);

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

        return bestSolution;
    }

    // COMPLETE
    private static ArrayList<ArrayList<Exam>> generatePopulation(Problem problem) {
        ArrayList<ArrayList<Exam>> population = new ArrayList<ArrayList<Exam>>();

        for (int i = 0; i < POPULATION_SIZE; i++){
            ArrayList<Exam> solution = problem.getRandomSolution();
            population.add(solution);
        }

        return population;
    }

    // COMPLETE
    private static ArrayList<ArrayList<Exam>> getNextGeneration(ArrayList<ArrayList<Exam>> population){
        ArrayList<ArrayList<Exam>> newGeneration = new ArrayList<ArrayList<Exam>>();

        if(ELITISM > 0) {
            Collections.sort(population, Comparator.comparing(s -> problemInstance.evaluate(s)));
            for (int j = 0; j < ELITISM; j++) {
                newGeneration.add(population.get(j));
            }
        }

        for(int i = 0; i < POPULATION_SIZE - ELITISM; i++) {
            ArrayList<Exam> individual1 = tournament(population);
            ArrayList<Exam> individual2 = tournament(population);
            ArrayList<Exam> child = crossover(individual1, individual2);
            ArrayList<Exam> newChild = mutation(child);
            newGeneration.add(newChild);
        }

        return newGeneration;
    }

    // COMPLETE
    private static ArrayList<Exam> tournament(ArrayList<ArrayList<Exam>> population) {
        Collections.shuffle(population);
        ArrayList<ArrayList<Exam>> subPopulation = new ArrayList<ArrayList<Exam>>();

        for(int i = 0; i < TOURNAMENT_SIZE; i++){
            subPopulation.add(population.get(i));
        }

        return getBestSolution(subPopulation);
    }

    // TODO
    private static ArrayList<Exam> mutation (ArrayList<Exam> solution) {
        Random rand = new Random();
        Double p = rand.nextDouble();
        Boolean mutationTarget = false; // true for room, false for period

        if (p < 0.5) { // 50% chance of mutating a room or a period
            mutationTarget = true;
        }

        p = rand.nextDouble();

        if(p < MUTATION_PROBABILITY){
            ArrayList<Exam> newSolution = new ArrayList<Exam>(solution);
            int index = rand.nextInt(exams.size());

            if(mutationTarget) {
                //TODO: MUTATE ROOM
            } else {
                // TODO: MUTATE PERIOD
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
    private static ArrayList<Exam> crossover (ArrayList<Exam> solution1, ArrayList<Exam> solution2){
        ArrayList<Exam> newSolution = new ArrayList<Exam>(solution1);

        for(int i = 0; i < solution1.size(); i++) {
            if(i%2 == 0){
                newSolution.set(i, solution2.get(i));
            }
        }

        return newSolution;
    }

    // COMPLETE
    private static ArrayList<Exam> getBestSolution(ArrayList<ArrayList<Exam>> population) {
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
    private static void printSolution(ArrayList<Exam> solution) {
        for(Exam i : solution) {
            System.out.print(i + ", ");
        }
    }
}
