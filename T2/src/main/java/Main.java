import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void usage() {
        System.out.println("Usage: gradle run --args=\"<FILENAME> <ALGORITHM>\"");
        System.out.println(
                "Where ALGORITHM can be one of the following: GENETIC, HILLCLIMB_SIMPLE, HILLCLIMB_STEEPEST or ANNEALING.");
        System.out.println("Example:gradle run --args=\"res/exam_comp_set1.exam GENETIC\"");
    }

    public static void main(String[] args) {

        ArrayList<String> algorithms = new ArrayList<String>(
                Arrays.asList("GENETIC", "HILLCLIMB_SIMPLE", "HILLCLIMB_STEEPEST", "ANNEALING"));

        if (args[0].equals("STATS")) {
            if (args.length == 2 && !algorithms.contains(args[1])) {
                usage();
                return;
            }
        } else if (args.length != 2 || !algorithms.contains(args[1])) {
            usage();
            return;
        }

        if (args[0].equals("STATS")) {
            Stats stats = new Stats();
            try {
                if (args.length == 1) {

                    stats.saveStatsHillClimb("ANNEALING");
                    stats.saveStatsHillClimb("HILLCLIMB_SIMPLE");
                    stats.saveStatsHillClimb("HILLCLIMB_STEEPEST");
                    stats.saveStatsGenetic();
                } else {
                    switch (args[1]) {
                    case "ANNEALING":
                        stats.saveStatsHillClimb("ANNEALING");
                        break;
                    case "HILLCLIMB_SIMPLE":
                        stats.saveStatsHillClimb("HILLCLIMB_SIMPLE");
                        break;
                    case "HILLCLIMB_STEEPEST":
                        stats.saveStatsHillClimb("HILLCLIMB_STEEPEST");
                        break;
                    case "GENETIC":
                        stats.saveStatsGenetic();
                        break;
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
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
                case "HILLCLIMB_SIMPLE":
                    solution = HillClimb.solve(problem, true);
                    break;
                case "HILLCLIMB_STEEPEST":
                    solution = HillClimb.solve(problem, false);
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
}
