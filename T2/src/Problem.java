import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

class Problem {

    private Reader reader = Reader.getInstance();

    private ArrayList<Exam> exams;

    private ArrayList<Period> periods;

    private ArrayList<Room> rooms;

    private ArrayList<PeriodHardConstraint> periodHardConstraints;
    private ArrayList<RoomHardConstraint> roomHardConstraints;

    public Problem(String filename) throws IOException {

        FileInputStream fstream = new FileInputStream(filename);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        setExams(reader.readExams(br));
        setPeriods(reader.readPeriods(br));
        setRooms(reader.readRooms(br));
        periodHardConstraints = reader.readPeriodHardConstraints(br);
        roomHardConstraints = reader.readRoomHardConstraints(br);

        fstream.close();
    }

    /**
     * @return the rooms
     */
    public ArrayList<Room> getRooms() {
        return rooms;
    }

    /**
     * @param rooms the rooms to set
     */
    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    /**
     * @return the periods
     */
    public ArrayList<Period> getPeriods() {
        return periods;
    }

    /**
     * @param periods the periods to set
     */
    public void setPeriods(ArrayList<Period> periods) {
        this.periods = periods;
    }

    /**
     * @return the exams
     */
    public ArrayList<Exam> getExams() {
        return exams;
    }

    /**
     * @param exams the exams to set
     */
    public void setExams(ArrayList<Exam> exams) {
        this.exams = exams;
    }

    public int evaluate(ArrayList<String> solution) {
        
        // Hard constraints
        int numConflicts = getNumConflicts(solution);
        int numRoomOccupancy = getNumRoomOccupancy(solution);
        int numRoomRelated = getNumRoomRelated(solution);
        int numPeriodRelated = getNumPeriodRelated(solution);
        int numPeriodUtilisation = getNumPeriodUtilisation(solution);

        return numConflicts + numRoomOccupancy + numRoomRelated + numPeriodRelated + numPeriodUtilisation;
    }

    public int getNumConflicts(ArrayList<String> solution) {
        int numConflicts = 0;
        for (int i = 0; i < periods.size(); i++) {
            int periodConflicts = 0;
            for (String exam : solution) {
                int period = Integer.parseInt(exam.substring(0, exam.indexOf(',')));
                if (period == i)
                    periodConflicts++;
            }

            if (periodConflicts > 1)
                numConflicts += periodConflicts;
        }
        return numConflicts;

    }

    public int getNumRoomOccupancy(ArrayList<String> solution){
        int numRoomOccupancy = 0;
        HashMap<String, Integer> visitedPeriods = new HashMap<String, Integer>();
        int size = solution.size();

        for(int i = 0; i < size; i++){
            String exam = solution.get(i);
            if(visitedPeriods.containsKey(exam)){
                continue;
            }

            int numStudentsExam1 = exams.get(i).getStudents().size();
            visitedPeriods.put(exam, numStudentsExam1);

            for(int j = i + 1; j < size; j++){
                String exam2 = solution.get(j);

                if(exam.equals(exam2)){
                    visitedPeriods.replace(exam, numStudentsExam1 + exams.get(j).getStudents().size());
                }
            }
        }

        for(Map.Entry<String, Integer> entry : visitedPeriods.entrySet()){
            int roomCapacity = rooms.get(Integer.parseInt(entry.getKey().substring(entry.getKey().indexOf(", ") + 2))).getCapacity();

            if(entry.getValue() > roomCapacity)
                numRoomOccupancy++;
        }

        return numRoomOccupancy;
    }

    public int getNumPeriodUtilisation(ArrayList<String> solution) {
        int numPeriodUtilisation = 0;

        for (int i = 0; i < solution.size(); i++) {
            String exam = solution.get(i);
            int period = Integer.parseInt(exam.substring(0, exam.indexOf(',')));

            if (exams.get(i).getDuration() > periods.get(period).getDuration())
                numPeriodUtilisation++;
        }

        return numPeriodUtilisation;
    }

    public int getNumPeriodRelated(ArrayList<String> solution) {
        int numPeriodRelated = 0;

        for (PeriodHardConstraint constraint : periodHardConstraints) {
            String type = constraint.getConstraint();
            String exam1 = solution.get(constraint.getExam1());
            String exam2 = solution.get(constraint.getExam2());

            int period1 = Integer.parseInt(exam1.substring(0, exam1.indexOf(',')));
            int period2 = Integer.parseInt(exam2.substring(0, exam2.indexOf(',')));

            switch (type) {
            case "AFTER":
                if (period1 < period2)
                    numPeriodRelated++;
                break;
            case "EXAM_COINCIDENCE":
                if (period1 != period2)
                    numPeriodRelated++;
                break;
            case "EXCLUSION":
                if (period1 == period2)
                    numPeriodRelated++;
                break;
            }

        }

        return numPeriodRelated;
    }

    int getNumRoomRelated(ArrayList<String> solution){
        int numRoomRelated = 0;
        
        for(RoomHardConstraint constraint : roomHardConstraints){
            String exam = solution.get(constraint.getExam());

            for(int i = 0; i < solution.size(); i++){
                if(solution.get(i).equals(exam) && i != constraint.getExam())
                    numRoomRelated++;
            }
        }

        return numRoomRelated;
    }

    public ArrayList<String> getRandomSolution() {
        ArrayList<String> solution = new ArrayList<String>();

        for (int i = 0; i < exams.size(); i++) {
            int timeslot = randInt(0, periods.size());
            int room = randInt(0, rooms.size());

            solution.add(timeslot + ", " + room);
        }

        return solution;
    }

    public int randInt(int min, int max) {

        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min)) + min;

        return randomNum;
    }
}