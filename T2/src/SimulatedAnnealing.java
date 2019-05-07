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
            int index1 = (int) (Math.random() * newSolution.size());
            int index2 = (int) (Math.random() * newSolution.size());

            String exam1 = newSolution.get(index1);
            String exam2 = newSolution.get(index2);

            int period1 = problem.getPeriod(exam1);
            int period2 = problem.getPeriod(exam2);

            int room1 = problem.getRoom(exam1);
            int room2 = problem.getRoom(exam2);

            if(randInt == 0){
                newSolution.set(index1, period2 + ", " + room1);
                newSolution.set(index2, period1 + ", " + room2);
            }
            else{
                newSolution.set(index1, period1 + ", " + room2);
                newSolution.set(index2, period2 + ", " + room1);
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