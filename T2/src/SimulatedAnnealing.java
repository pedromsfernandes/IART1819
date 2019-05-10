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

    public static ArrayList<Exam> getNeighbourSolution(ArrayList<Exam> currentSolution, Problem problem) {
        int rand = Problem.randInt(0, 2);

        if (rand == 0)
            return getNeighbourSolutionSwap(currentSolution, problem);

        return getNeighbourSolutionStep(currentSolution, problem);
    }

    public static ArrayList<Exam> getNeighbourSolutionStep(ArrayList<Exam> currentSolution, Problem problem) {
        int randInt = Problem.randInt(0, 2);
        ArrayList<Exam> newSolution = new ArrayList<>(currentSolution);
        int periods = problem.getPeriods().size();
        int rooms = problem.getRooms().size();

        for (int i = 0; i < newSolution.size(); i++) {
            Exam exam = newSolution.get(i);
            int periodId = exam.getPeriod().getId() + Problem.randInt(0, periods);
            int roomId = exam.getRoom().getId() + Problem.randInt(0, rooms);

            if (periodId >= periods) {
                periodId -= periods;
            }

            if (roomId >= rooms) {
                roomId -= rooms;
            }

            Exam newExam = new Exam(exam);

            if (randInt == 0) {
                Period newPeriod = problem.getPeriods().get(periodId);
                newExam.setPeriod(newPeriod);

            } else {
                Room newRoom = problem.getRooms().get(roomId);
                newExam.setRoom(newRoom);
            }

            newSolution.set(i, newExam);
        }

        return newSolution;
    }

    public static ArrayList<Exam> getNeighbourSolutionSwap(ArrayList<Exam> currentSolution, Problem problem) {
        ArrayList<Exam> newSolution = new ArrayList<>(currentSolution);

        int index1 = Problem.randInt(0, newSolution.size());
        int index2 = Problem.randInt(0, newSolution.size());

        Exam newExam1 = new Exam(newSolution.get(index1));
        Exam newExam2 = new Exam(newSolution.get(index2));

        newSolution.set(index1, newExam2);
        newSolution.set(index2, newExam1);

        return newSolution;
    }

    public static ArrayList<Exam> solve(Problem problem) {
        double coolingRate = 0.003;

        ArrayList<Exam> currentSolution = new ArrayList<Exam>(problem.getRandomSolution());
        ArrayList<Exam> best = new ArrayList<Exam>(currentSolution);
        int bestEnergy = problem.evaluate(best, true);

        int i = 0;
        for (double temp = 100000; temp > 1; temp *= 1 - coolingRate, i++) {

            ArrayList<Exam> newSolution = new ArrayList<Exam>(getNeighbourSolution(currentSolution, problem));
            int currentEnergy = problem.evaluate(currentSolution, false);
            int neighbourEnergy = problem.evaluate(newSolution, false);

            if (acceptanceProbability(currentEnergy, neighbourEnergy, temp) > Math.random()) {
                currentSolution = new ArrayList<>(newSolution);
            }

            if (problem.evaluate(currentSolution, false) < bestEnergy) {
                best = new ArrayList<>(currentSolution);
                bestEnergy = problem.evaluate(best, false);
            }

            if (i % 10000 == 0) {
                currentSolution = new ArrayList<Exam>(problem.getRandomSolution());
                temp = 100000;
            }
        }

        return best;
    }
}