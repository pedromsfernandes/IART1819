import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Genetic {
    public static final Integer POPULATION_SIZE = 50;
    public static final Integer TOURNAMENT_SIZE = 20;
    public static final Double MUTATION_PROBABILITY = 0.9;
    public static final Integer BEST_VALUE = 130;
    public static final Integer ELITISM = 5;

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
        //printSolution(bestSolution);
        System.out.println("\nVALUE: " + problem.evaluate(bestSolution));

        int generationCounter = 0;

        //while (problem.evaluate(bestSolution) > BEST_VALUE) {
          for(int i = 0; i < 5; i++){
            population = getNextGeneration(population);
            bestSolution = getBestSolution(population);
            generationCounter++;
            System.out.println(generationCounter + " , BEST VALUE: " + problem.evaluate(bestSolution));
        }

        System.out.print("\nBEST FROM FINAL POPULATION:  ");
        bestSolution = getBestSolution(population);
        //printSolution(bestSolution);
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
    private static ArrayList<ArrayList<Exam>> getNextGeneration(ArrayList<ArrayList<Exam>> currentPopulation){
        ArrayList<ArrayList<Exam>> newGeneration = new ArrayList<ArrayList<Exam>>();

        if(ELITISM > 0) {
            Collections.sort(currentPopulation, Comparator.comparing(s -> problemInstance.evaluate(s)));

            System.out.println("ELITISM VALUES");
            for (int i = 0; i < ELITISM; i++){
                System.out.println(problemInstance.evaluate(currentPopulation.get(i)));
            }

            for (int j = 0; j < ELITISM; j++) {
                newGeneration.add(currentPopulation.get(j));
            }

            System.out.println("NEW GENERATION VALUES AFTER INSERTING ELITISM");
            for(int i = 0; i < ELITISM; i++){
                System.out.println(problemInstance.evaluate(newGeneration.get(i)));
            }
        }

        for(int i = 0; i < POPULATION_SIZE - ELITISM; i++) {
            ArrayList<Exam> individual1 = tournament(currentPopulation);
            // TODO: TEST ------------
            Collections.sort(newGeneration, Comparator.comparing(s -> problemInstance.evaluate(s)));
            System.out.println("NEW GENERATION VALUES AFTER TOURNAMENT 1");
            for(int j = 0; j < ELITISM; j++){
                System.out.println(problemInstance.evaluate(newGeneration.get(i)));
            }
            // -----------------------


            ArrayList<Exam> individual2 = tournament(currentPopulation);
            // TODO: TEST ------------
            Collections.sort(newGeneration, Comparator.comparing(s -> problemInstance.evaluate(s)));
            System.out.println("NEW GENERATION VALUES AFTER TOURNAMENT 2");
            for(int j = 0; j < ELITISM; j++){
                System.out.println(problemInstance.evaluate(newGeneration.get(i)));
            }
            // -----------------------


            ArrayList<Exam> child = crossover(individual1, individual2);
            // TODO: TEST ------------
            Collections.sort(newGeneration, Comparator.comparing(s -> problemInstance.evaluate(s)));
            System.out.println("NEW GENERATION VALUES AFTER CROSSOVER");
            for(int j = 0; j < ELITISM; j++){
                System.out.println(problemInstance.evaluate(newGeneration.get(i)));
            }
            // -----------------------


            ArrayList<Exam> newChild = mutation(child);
            // TODO: TEST ------------
            Collections.sort(newGeneration, Comparator.comparing(s -> problemInstance.evaluate(s)));
            System.out.println("NEW GENERATION VALUES AFTER MUTATION");
            for(int j = 0; j < ELITISM; j++){
                System.out.println(problemInstance.evaluate(newGeneration.get(i)));
            }
            // -----------------------
            newGeneration.add(newChild);

            // TODO: TEST ------------
            Collections.sort(newGeneration, Comparator.comparing(s -> problemInstance.evaluate(s)));
            System.out.println("NEW GENERATION VALUES AFTER INSERTING " + (i+1) + "ELEMENT");
            for(int j = 0; j < ELITISM; j++){
                System.out.println(problemInstance.evaluate(newGeneration.get(i)));
            }
            System.out.println();
            // -----------------------
        }

        // TODO: TEST ------------
        Collections.sort(newGeneration, Comparator.comparing(s -> problemInstance.evaluate(s)));
        System.out.println("NEW GENERATION VALUES AFTER INSERTING ALL OTHER ELEMENTS");
        for(int i = 0; i < ELITISM; i++){
            System.out.println(problemInstance.evaluate(newGeneration.get(i)));
        }
       // --------------------------

        return newGeneration;
    }

    // COMPLETE
    private static ArrayList<Exam> tournament(ArrayList<ArrayList<Exam>> tournamentPopulation) {
        Collections.shuffle(tournamentPopulation);
        ArrayList<ArrayList<Exam>> subPopulation = new ArrayList<ArrayList<Exam>>();

        for(int i = 0; i < TOURNAMENT_SIZE; i++){
            subPopulation.add(tournamentPopulation.get(i));
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
                Exam targetElement = newSolution.get(index);
                newSolution.remove(index);

                Room newRoom = rooms.get(newRoomNr);
                targetElement.setRoom(newRoom);

                newSolution.add(targetElement);
            }
            else {
                int newPeriodNr = rand.nextInt(periods.size());
                Exam targetElement = newSolution.get(index);
                newSolution.remove(index);

                Period newPeriod = periods.get(newPeriodNr);
                targetElement.setPeriod(newPeriod);

                newSolution.add(targetElement);
            }

            return newSolution;
        }

        return solution;
    }

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

    // COMPLETE
    private static void printSolution(ArrayList<Exam> solution) {
        for(Exam i : solution) {
            System.out.println(i.getRoom().getId() + ", " + i.getPeriod().getId());
        }
    }
}
