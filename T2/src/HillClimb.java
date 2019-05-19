import java.util.ArrayList;

class HillClimb {

    public static Problem problemStatic;
    
    public static ArrayList<Exam> solve(Problem problem){
        problemStatic = problem;
        
        ArrayList<Exam> current = new ArrayList<Exam>();
        ArrayList<Exam> neighbor = new ArrayList<Exam>();
        int bestScore = Integer.MAX_VALUE;
        int currentScore;
        current = problem.getRandomSolution();
        currentScore = problem.evaluate(current);

        System.out.println("Start: " + currentScore);

        while(true) {
            neighbor = getBestNeighbor(current);
            currentScore = problem.evaluate(neighbor);
            System.out.println("Iteration: " + currentScore);
            
            if(currentScore < bestScore){
                bestScore = currentScore;
                current = cloneList(neighbor);
            }else{
                bestScore = currentScore;
                current = cloneList(neighbor);
                break;
            }
        }
        
        //problem.evaluate(current,true);
        return cloneList(current);
    }

    private static ArrayList<Exam> getBestNeighbor(ArrayList<Exam> current) {
        ArrayList<Exam> temp = new ArrayList<Exam>();
        ArrayList<Exam> solution = new ArrayList<Exam>();
        solution = cloneList(current);
        int bestScore = problemStatic.evaluate(current);
        int currentScore;

        for(int i = 0; i < current.size() - 1; i++) {
            for(int j = 1 ; j < current.size(); j++) {

                Exam exam1 = current.get(i);
                Exam exam2 = current.get(j);

                //1st Solution: Swap periods
                Period period1 = exam1.getPeriod();
                exam1.setPeriod(exam2.getPeriod());
                exam2.setPeriod(period1);

                temp = current;
                temp.set(i,exam1);
                temp.set(j,exam2);

                currentScore = problemStatic.evaluate(temp);
                if(currentScore < bestScore){
                    bestScore = currentScore;
                    solution = cloneList(temp);
                    System.out.println("Change: " + currentScore);
                }

                //2st Solution: Swap rooms
                exam1 = current.get(i);
                exam2 = current.get(j);

                Room room1 = exam1.getRoom();
                exam1.setRoom(exam2.getRoom());
                exam2.setRoom(room1);

                temp = current;
                temp.set(i,exam1);
                temp.set(j,exam2);

                currentScore = problemStatic.evaluate(temp);
                if(currentScore < bestScore){
                    bestScore = currentScore;
                    solution = cloneList(temp);
                    System.out.println("Change: " + currentScore);
                }
            }
        }

        //System.out.println("Returning a solution with score: " + bestScore + " but actual " + problemStatic.evaluate(solution));
        return solution;
    }

    public static ArrayList<Exam> cloneList(ArrayList<Exam> list){
        ArrayList<Exam> clone = new ArrayList<Exam>(list.size());
        for(Exam exam : list ) clone.add(new Exam(exam));
        return clone;
    }

}