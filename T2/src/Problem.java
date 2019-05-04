import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

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

        int value = 0;

        for(String exam : solution){
            int period = Integer.parseInt(exam.substring(0, exam.indexOf(',')));
            int room = Integer.parseInt(exam.substring(exam.indexOf(',')));
        }

        return value;
    }
}