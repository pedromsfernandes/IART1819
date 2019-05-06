import java.util.ArrayList;
import java.util.Random;

class SimulatedAnnealing {

    public static double acceptanceProbability(int energy, int newEnergy, double temperature) {
        // If the new solution is better, accept it
        if (newEnergy < energy) {
            return 1.0;
        }
        // If the new solution is worse, calculate an acceptance probability
        return Math.exp((energy - newEnergy) / temperature);
    }

    public static ArrayList<String> solve(Problem problem) {
        double coolingRate = 0.003;

        ArrayList<String> currentSolution = problem.getRandomSolution();
        System.out.println("initial: " + problem.evaluate(currentSolution, true));
        ArrayList<String> best = currentSolution;

        for (double temp = 1000; temp > 1; temp *= 1 - coolingRate) {
            ArrayList<String> newSolution = currentSolution;

            int randInt = Problem.randInt(0, 2);
            int direction1 = Math.random() > 0.5 ? 1 : -1;
            int direction2 = Math.random() > 0.5 ? 1 : -1;

            int periods = problem.getPeriods().size();
            int rooms = problem.getRooms().size();

            for (int i = 0; i < newSolution.size(); i++) {
                String exam = newSolution.get(i);
   
                int period = Integer.parseInt(exam.substring(0, exam.indexOf(',')));
                int room = Integer.parseInt(exam.substring(exam.indexOf(',') + 2));
                period += randInt * direction1;
                room += randInt * direction2;

                if (period >= periods)
                    period = 0;
                else if (period < 0)
                    period = periods - 1;

                if (room >= rooms)
                    room = 0;
                else if (room < 0)
                    room = rooms - 1;

                newSolution.set(i, period + ", " + room);
            }

            int currentEnergy = problem.evaluate(currentSolution, false);
            int neighbourEnergy = problem.evaluate(newSolution, false);

            if (acceptanceProbability(currentEnergy, neighbourEnergy, temp) > Math.random()) {
                currentSolution = newSolution;
            }

            if (problem.evaluate(currentSolution, false) < problem.evaluate(best, false)) {
                best = currentSolution;
            }
        }

        return best;
    }
}