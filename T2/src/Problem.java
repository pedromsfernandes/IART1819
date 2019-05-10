import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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

    private HashMap<Integer, ArrayList<Integer>> chart;

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

    public int evaluate(ArrayList<Exam> solution, boolean print) {

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

    public int evaluate(ArrayList<Exam> solution) {

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
    public int getNumConflicts(ArrayList<Exam> solution) {
        int numConflicts = 0;

        for (Map.Entry<Integer, ArrayList<Integer>> entry : chart.entrySet()) {

            int exam1 = entry.getKey();

            for (int exam2 : entry.getValue()) {
                if (solution.get(exam1).getPeriod().equals(solution.get(exam2).getPeriod()))
                    numConflicts++;
            }
        }

        return numConflicts;
    }

    public HashMap<Integer, ArrayList<Integer>> getIncompatibityChart() {
        HashMap<Integer, ArrayList<Integer>> chart = new HashMap<Integer, ArrayList<Integer>>();

        for (int i = 0; i < exams.size(); i++) {
            for (int j = i + 1; j < exams.size(); j++) {
                int common = commonElements(exams.get(i).getStudents(), exams.get(j).getStudents());

                if (common > 0) {
                    ArrayList<Integer> current = chart.get(i);

                    if (current != null) {
                        current.add(j);
                        chart.replace(i, current);
                    } else {
                        current = new ArrayList<Integer>();
                        current.add(j);
                        chart.put(i, current);
                    }

                }
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

    public int getNumRoomOccupancy(ArrayList<Exam> solution) {
        int numRoomOccupancy = 0;
        HashMap<String, Integer> visitedPeriods = new HashMap<String, Integer>();
        int size = solution.size();

        for (int i = 0; i < size; i++) {
            Exam exam = solution.get(i);
            String key = exam.getPeriod().getId() + ", " + exam.getRoom().getId();
            if (visitedPeriods.containsKey(key)) {
                continue;
            }

            int numStudentsExam1 = exams.get(i).getStudents().size();
            visitedPeriods.put(key, numStudentsExam1);

            for (int j = i + 1; j < size; j++) {
                Exam exam2 = solution.get(j);
                String key2 = exam2.getPeriod().getId() + ", " + exam2.getRoom().getId();

                if (key.equals(key2)) {
                    visitedPeriods.replace(key, numStudentsExam1 + exams.get(j).getStudents().size());
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

    public int getNumPeriodUtilisation(ArrayList<Exam> solution) {
        int numPeriodUtilisation = 0;

        for (int i = 0; i < solution.size(); i++) {
            Exam exam = solution.get(i);
            Period period = exam.getPeriod();

            if (exam.getDuration() > period.getDuration())
                numPeriodUtilisation++;
        }

        return numPeriodUtilisation;
    }

    public int getNumPeriodRelated(ArrayList<Exam> solution) {
        int numPeriodRelated = 0;

        for (PeriodHardConstraint constraint : periodHardConstraints) {
            String type = constraint.getConstraint();
            Exam exam1 = solution.get(constraint.getExam1());
            Exam exam2 = solution.get(constraint.getExam2());

            int period1 = exam1.getPeriod().getId();
            int period2 = exam2.getPeriod().getId();

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

    int getNumRoomRelated(ArrayList<Exam> solution) {
        int numRoomRelated = 0;

        for (RoomHardConstraint constraint : roomHardConstraints) {
            int examId = constraint.getExam();
            Exam exam = solution.get(examId);
            int period1 = exam.getPeriod().getId();
            int room1 = exam.getRoom().getId();

            for (int i = 0; i < solution.size(); i++) {
                int period2 = solution.get(i).getPeriod().getId();
                int room2 = solution.get(i).getRoom().getId();
                if (period1 == period2 && room1 == room2 && i != examId)
                    numRoomRelated++;
            }
        }

        return numRoomRelated;
    }

    public ArrayList<Exam> getRandomSolution() {
        ArrayList<Exam> solution = new ArrayList<>(exams);

        for (int i = 0; i < solution.size(); i++) {
            Exam exam = solution.get(i);

            int timeslot = randInt(0, periods.size());
            int room = randInt(0, rooms.size());

            Exam newExam = new Exam(exam);
            newExam.setPeriod(periods.get(timeslot));
            newExam.setRoom(rooms.get(room));
            solution.set(i, newExam);
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

    public static void writeSolution(ArrayList<Exam> solution, String filename) throws IOException {
        FileOutputStream fstream = null;

        try {
            fstream = new FileOutputStream(filename + ".sln");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fstream));

        for (Exam exam : solution) {
            bw.write(exam + "\r\n");
        }

        bw.close();
    }
}