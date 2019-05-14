import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Genetic {
    public static final Integer POPULATION_SIZE = 50;
    public static final Integer TOURNAMENT_SIZE = 10;
    public static final Double MUTATION_PROBABILITY = 0.9;
    public static final Integer BEST_VALUE = 0;
    public static final Integer ELITISM = 2;
    public static final Integer MAX_GENERATIONS = 50;

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

        System.out.println("BEST FROM INITIAL POPULATION");
        printSolution(bestSolution);
        System.out.println("\nPENALTY VALUE: " + problem.evaluate(bestSolution));

        int generationCounter = 0;

        while ((problem.evaluate(bestSolution) > BEST_VALUE) && (generationCounter < MAX_GENERATIONS)) {
            population = getNextGeneration(population);
            bestSolution = getBestSolution(population);
            generationCounter++;
            System.out.println("GENERATION " + generationCounter + ", BEST SOLUTION PENALTY VALUE: " + problem.evaluate(bestSolution));
        }

        System.out.print("\nBEST FROM FINAL POPULATION:  ");
        bestSolution = getBestSolution(population);
        printSolution(bestSolution);

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
    private static ArrayList<ArrayList<Exam>> getNextGeneration(ArrayList<ArrayList<Exam>> currentPopulation){
        ArrayList<ArrayList<Exam>> newGeneration = new ArrayList<ArrayList<Exam>>();
        int generationSize = currentPopulation.size();

        if(ELITISM > 0) {
            Collections.sort(currentPopulation, Comparator.comparing(s -> problemInstance.evaluate(s)));

            for (int j = 0; j < ELITISM; j++) {
                newGeneration.add(currentPopulation.get(j));
            }
        }

        while(newGeneration.size() < generationSize) {
            ArrayList<Exam> individual1 = tournament(currentPopulation);
            ArrayList<Exam> individual2 = tournament(currentPopulation);
            ArrayList<ArrayList<Exam>> children = crossover(individual1, individual2);
            ArrayList<Exam> newChild1 = mutation(children.get(0));
            ArrayList<Exam> newChild2 = mutation(children.get(1));
            newGeneration.add(newChild1);
            newGeneration.add(newChild2);
        }

        return newGeneration;
    }

    // COMPLETE
    private static ArrayList<Exam> tournament(ArrayList<ArrayList<Exam>> tournamentPopulation) {
        ArrayList<ArrayList<Exam>> copy = new ArrayList<ArrayList<Exam>>(tournamentPopulation);
        Collections.shuffle(copy);
        ArrayList<ArrayList<Exam>> subPopulation = new ArrayList<ArrayList<Exam>>();

        for(int i = 0; i < TOURNAMENT_SIZE; i++){
            subPopulation.add(new ArrayList<Exam>(copy.get(i)));
        }

        return getBestSolution(subPopulation);
    }

    // COMPLETE
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
                int newRoomNr = rand.nextInt(rooms.size());
                Exam targetElement = new Exam(newSolution.get(index));
                newSolution.remove(index);

                Room newRoom = rooms.get(newRoomNr);
                targetElement.setRoom(newRoom);

                newSolution.add(targetElement);
            }
            else {
                int newPeriodNr = rand.nextInt(periods.size());
                Exam targetElement = new Exam(newSolution.get(index));
                newSolution.remove(index);

                Period newPeriod = periods.get(newPeriodNr);
                targetElement.setPeriod(newPeriod);

                newSolution.add(targetElement);
            }

            return newSolution;
        }

        return solution;
    }

    private static ArrayList<ArrayList<Exam>> crossover (ArrayList<Exam> solution1, ArrayList<Exam> solution2){
        ArrayList<ArrayList<Exam>> newSolutions = new ArrayList<ArrayList<Exam>>();

        Random rand = new Random();
        int cutIndex = rand.nextInt((solution1.size() - 0) + 1);

        ArrayList<Exam> newSolution1 = new ArrayList<>();
        ArrayList<Exam> newSolution2 = new ArrayList<>();

        for(int i = 0; i < cutIndex; i++) {
            newSolution1.add(solution2.get(i));
            newSolution2.add(solution1.get(i));
        }

        for (int i = cutIndex; i < solution1.size(); i++) {
            newSolution1.add(solution1.get(i));
            newSolution2.add(solution2.get(i));
        }

        newSolutions.add(newSolution1);
        newSolutions.add(newSolution2);

        return newSolutions;
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

        return new ArrayList<Exam>(population.get(index));
    }

    // COMPLETE
    private static void printSolution(ArrayList<Exam> solution) {
        for(Exam i : solution) {
            System.out.println(i.getPeriod().getId() + ", " + i.getRoom().getId());
        }
    }
}
