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

    public static void solve(Problem problem){
        double coolingRate = 0.003;

        ArrayList<String> currentSolution = problem.getRandomSolution();
        ArrayList<String> best = currentSolution;

        for (double temp = 1000; temp > 1; temp *= 1 - coolingRate ) {
            ArrayList<String> newSolution = currentSolution;

            // int exam1 = (int) (newSolution.size() * Math.random());
            // int exam2 = (int) (newSolution.size() * Math.random());

            // String value1 = newSolution.get(discipline1);
            // String value2 = newSolution.get(discipline2);
        
            // newSolution.set(discipline1, slot2);
            // newSolution.set(discipline2, slot1);

            int currentEnergy = problem.evaluate(currentSolution);
            int neighbourEnergy = problem.evaluate(newSolution);

            if (acceptanceProbability(currentEnergy, neighbourEnergy, temp) > Math.random()) {
                currentSolution = newSolution;
            }

            if (problem.evaluate(currentSolution) < problem.evaluate(best)) {
                best = currentSolution;
            }
        }
    }
}