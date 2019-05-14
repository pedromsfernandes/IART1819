import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void usage() {
        System.out.println("Usage: java Main <FILENAME> <ALGORITHM>");
        System.out.println("Where ALGORITHM can be one of the following: GENETIC, HILLCLIMB or ANNEALING.");
        System.out.println("Example: java Main ../res/exam_comp_set1.exam GENETIC");
    }

    public static void main(String[] args) {

        ArrayList<String> algorithms = new ArrayList<String>(Arrays.asList("GENETIC", "HILLCLIMB", "ANNEALING"));

        if (args.length != 2 || !algorithms.contains(args[1])) {
            usage();
            return;
        }

        try {
            String fileName = args[0];
            Problem problem = new Problem(fileName);
            ArrayList<Exam> solution = new ArrayList<Exam>();

            switch (args[1]) {
            case "GENETIC":
                solution = Genetic.geneticSolve(problem);
                break;
            case "ANNEALING":
                solution = SimulatedAnnealing.solve(problem);
                break;
            case "HILLCLIMB":
                solution = HillClimb.solve(problem);
                break;
            default:
                break;
            }

            System.out.println("Final value: " + problem.evaluate(solution, true));
            Problem.writeSolution(solution, fileName.replaceFirst("[.][^.]+$", ""));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
