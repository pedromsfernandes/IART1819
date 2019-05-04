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

    public static ArrayList<String> getRandomSolution(int numExams, int numTimeslots, int numRooms) {
        ArrayList<String> solution = new ArrayList<String>();

        for (int i = 0; i < numExams; i++) {
            int timeslot = randInt(0, numTimeslots);
            int room = randInt(0, numRooms);

            solution.add(timeslot + ", " + room);
        }

        return solution;
    }

    public static int randInt(int min, int max) {

        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static void solve(Problem problem){
        double coolingRate = 0.003;

        ArrayList<Exam> exams = problem.getExams();
        ArrayList<Period> periods = problem.getPeriods(); 
        ArrayList<Room> rooms = problem.getRooms();

        ArrayList<String> currentSolution = getRandomSolution(exams.size(), periods.size(), rooms.size());
        ArrayList<String> best = currentSolution;

        for (double temp = 1000; temp > 1; temp *= 1 - coolingRate ) {
            ArrayList<String> newSolution = currentSolution;

            // int discipline1 = (int) (newSolution.size() * Math.random());
            // int discipline2 = (int) (newSolution.size() * Math.random());

            // int slot1 = newSolution.get(discipline1);
            // int slot2 = newSolution.get(discipline2);
        
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