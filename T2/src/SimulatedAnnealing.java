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

    public static ArrayList<Exam> solve(Problem problem) {
        double coolingRate = 0.003;

        ArrayList<Exam> currentSolution = new ArrayList<Exam>(problem.getRandomSolution());
        System.out.println("initial: " + problem.evaluate(currentSolution, true));
        ArrayList<Exam> best = new ArrayList<Exam>(currentSolution);

        for (double temp = 1000; temp > 1; temp *= 1 - coolingRate) {
            ArrayList<Exam> newSolution = new ArrayList<>(currentSolution);

            int randInt = Problem.randInt(0, 2);
            int index1 = (int) (Math.random() * newSolution.size());
            int index2 = (int) (Math.random() * newSolution.size());

            Exam exam1 = newSolution.get(index1);
            Exam exam2 = newSolution.get(index2);

            Period period1 = exam1.getPeriod();
            Period period2 = exam2.getPeriod();

            Room room1 = exam1.getRoom();
            Room room2 = exam2.getRoom();

            Exam newExam1 = new Exam(exam1); 
            Exam newExam2 = new Exam(exam2); 

            if(randInt == 0){

                newExam1.setPeriod(period2);
                newExam2.setPeriod(period1);
                
                newSolution.set(index1, newExam1);
                newSolution.set(index2, newExam2);
            }
            else{
                newExam1.setRoom(room2);
                newExam2.setRoom(room1);
                newSolution.set(index1, newExam1);
                newSolution.set(index2, newExam2);
            }

            int currentEnergy = problem.evaluate(currentSolution, false);
            int neighbourEnergy = problem.evaluate(newSolution, false);

            if (acceptanceProbability(currentEnergy, neighbourEnergy, temp) > Math.random()) {
                currentSolution = new ArrayList<>(newSolution);
            }

            if (problem.evaluate(currentSolution, false) < problem.evaluate(best, false)) {
                best = new ArrayList<>(currentSolution);
            }
        }

        return best;
    }
}