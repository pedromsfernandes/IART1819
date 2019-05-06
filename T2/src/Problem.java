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

    private HashMap<String, Integer> chart;

    public Problem(String filename) throws IOException {

        FileInputStream fstream = new FileInputStream(filename);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        setExams(reader.readExams(br));
        setPeriods(reader.readPeriods(br));
        setRooms(reader.readRooms(br));
        periodHardConstraints = reader.readPeriodHardConstraints(br);
        roomHardConstraints = reader.readRoomHardConstraints(br);
        chart = getIncompatibityChart();

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

    public int evaluate(ArrayList<String> solution, boolean print) {

        // Hard constraints
        int numConflicts = getNumConflicts(solution);
        int numRoomOccupancy = getNumRoomOccupancy(solution);
        int numRoomRelated = getNumRoomRelated(solution);
        int numPeriodRelated = getNumPeriodRelated(solution);
        int numPeriodUtilisation = getNumPeriodUtilisation(solution);

        if (print) {
            System.out.println("numConflicts: " + numConflicts);
            System.out.println("numRoomOccupancy: " + numRoomOccupancy);
            System.out.println("numRoomRelated: " + numRoomRelated);
            System.out.println("numPeriodRelated: " + numPeriodRelated);
            System.out.println("numPeriodUtilisation: " + numPeriodUtilisation);
        }

        return numConflicts + numRoomOccupancy + numRoomRelated + numPeriodRelated + numPeriodUtilisation;
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

    // TODO: numero de conflictos é numero de exames no mesmo periodo, onde pelo
    // menos um estudante está inscrito aos dois
    public int getNumConflicts(ArrayList<String> solution) {
        int numConflicts = 0;

        for (Map.Entry<String, Integer> entry : chart.entrySet()) {
            String pair = entry.getKey();
            int exam1 = Integer.parseInt(pair.substring(0, pair.indexOf("-")));
            int exam2 = Integer.parseInt(pair.substring(pair.indexOf("-") + 1));

            if (getPeriod(solution.get(exam1)) == getPeriod(solution.get(exam2)))
                numConflicts++;

        }

        return numConflicts;
    }

    public HashMap<String, Integer> getIncompatibityChart() {
        HashMap<String, Integer> chart = new HashMap<String, Integer>();

        for (int i = 0; i < exams.size(); i++) {
            for (int j = i + 1; j < exams.size(); j++) {
                int common = commonElements(exams.get(i).getStudents(), exams.get(j).getStudents());

                if (common > 0)
                    chart.put(i + "-" + j, common);
            }
        }

        return chart;
    }

    private int commonElements(ArrayList<Integer> lhs, ArrayList<Integer> rhs) {
        HashSet<Integer> commonElements = new HashSet<Integer>();

        for (int i = 0; i < lhs.size(); i++) {
            if (rhs.contains(lhs.get(i))) {
                commonElements.add(lhs.get(i));
            }
        }

        for (int i = 0; i < rhs.size(); i++) {
            if (lhs.contains(rhs.get(i))) {
                commonElements.add(rhs.get(i));
            }
        }

        return commonElements.size();
    }

    public int getNumRoomOccupancy(ArrayList<String> solution) {
        int numRoomOccupancy = 0;
        HashMap<String, Integer> visitedPeriods = new HashMap<String, Integer>();
        int size = solution.size();

        for (int i = 0; i < size; i++) {
            String exam = solution.get(i);
            if (visitedPeriods.containsKey(exam)) {
                continue;
            }

            int numStudentsExam1 = exams.get(i).getStudents().size();
            visitedPeriods.put(exam, numStudentsExam1);

            for (int j = i + 1; j < size; j++) {
                String exam2 = solution.get(j);

                if (exam.equals(exam2)) {
                    visitedPeriods.replace(exam, numStudentsExam1 + exams.get(j).getStudents().size());
                }
            }
        }

        for (Map.Entry<String, Integer> entry : visitedPeriods.entrySet()) {
            int roomCapacity = rooms.get(getRoom(entry.getKey())).getCapacity();

            if (entry.getValue() > roomCapacity)
                numRoomOccupancy++;
        }

        return numRoomOccupancy;
    }

    public int getRoom(String exam) {
        return Integer.parseInt(exam.substring(exam.indexOf(", ") + 2));
    }

    public int getPeriod(String exam) {
        return Integer.parseInt(exam.substring(0, exam.indexOf(", ")));
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

            int period1 = getPeriod(exam1);
            int period2 = getPeriod(exam2);

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

    int getNumRoomRelated(ArrayList<String> solution) {
        int numRoomRelated = 0;

        for (RoomHardConstraint constraint : roomHardConstraints) {
            String exam = solution.get(constraint.getExam());

            for (int i = 0; i < solution.size(); i++) {
                if (solution.get(i).equals(exam) && i != constraint.getExam())
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

    public static int randInt(int min, int max) {

        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min)) + min;

        return randomNum;
    }
}