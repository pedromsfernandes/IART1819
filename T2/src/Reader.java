import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reader {

    public static Reader instance = new Reader();

    // Project Variables ---------------------------------------

    // ---------------------------------------------------------


    public void Reader(){}

    public static Reader getInstance() {
        return instance;
    }

    public ArrayList<Exam> readExams(BufferedReader br) throws IOException {

        ArrayList<Exam> exams = new ArrayList<Exam>();

        String strLine;
        strLine = br.readLine();

        Pattern r = Pattern.compile("(\\d+)");
        Matcher m = r.matcher(strLine);
        m.find();
        int numExams = Integer.parseInt(m.group(0));

        for(int i = 0; i < numExams; i++) {
            strLine = br.readLine();
            String[] values_raw = strLine.split(", ");
            ArrayList<Integer> values = new ArrayList<>();

            for(String value:values_raw){
                values.add(Integer.parseInt(value.trim()));
            }

            int id = i;
            int examDuration = values.get(0);

            ArrayList<Integer> students = values;
            students.remove(0);

            Exam exam = new Exam(id, examDuration, students);
            exams.add(exam);
        }

        return exams;
    }

    public ArrayList<Period> readPeriods(BufferedReader br) throws IOException {
        ArrayList<Period> periods = new ArrayList<Period>();
        String strLine;
        strLine = br.readLine();

        Pattern r = Pattern.compile("(\\d+)");
        Matcher m = r.matcher(strLine);
        m.find();

        int numPeriods = Integer.parseInt(m.group(0));

        for(int i = 0; i < numPeriods; i++) {
            strLine = br.readLine();
            String[] values_raw = strLine.split(", ");

            String date = values_raw[0];
            String time = values_raw[1];
            int duration = Integer.parseInt(values_raw[2]);
            int penalty = Integer.parseInt(values_raw[3]);

            Period period = new Period(date, time, duration, penalty);
            periods.add(period);
        }

        return periods;
    }

    public ArrayList<Room> readRooms(BufferedReader br) throws IOException {
        ArrayList<Room> rooms = new ArrayList<Room>();

        String strLine;
        strLine = br.readLine();

        Pattern r = Pattern.compile("(\\d+)");
        Matcher m = r.matcher(strLine);
        m.find();

        int numRooms = Integer.parseInt(m.group(0));

        for (int i = 0; i < numRooms; i++) {
            strLine = br.readLine();
            String[] values_raw = strLine.split(", ");

            int capacity = Integer.parseInt(values_raw[0]);
            int penalty = Integer.parseInt(values_raw[1]);

            Room room = new Room(capacity, penalty);

            rooms.add(room);
        }

        return rooms;
    }

    public ArrayList<PeriodHardConstraint> readPeriodHardConstraints(BufferedReader br) throws IOException {
        ArrayList<PeriodHardConstraint> periodHardConstraints = new ArrayList<PeriodHardConstraint>();
        String strLine;

        strLine = br.readLine();    // Reading PeriodHardConstraints header
        strLine = br.readLine();    // Reading the first PeriodHardConstraint

        while (strLine.charAt(0) != '[') {

            String[] values_raw = strLine.split(", ");
            int exam1 = Integer.parseInt(values_raw[0]);
            String constraint = values_raw[1];
            int exam2 = Integer.parseInt(values_raw[2]);

            PeriodHardConstraint periodHardConstraint = new PeriodHardConstraint(exam1, exam2, constraint);
            periodHardConstraints.add(periodHardConstraint);

            strLine = br.readLine();
        }

        return periodHardConstraints;
    }

    public ArrayList<RoomHardConstraint>  readRoomHardConstraints(BufferedReader br) throws IOException {
        
        ArrayList<RoomHardConstraint> roomHardConstraints = new ArrayList<RoomHardConstraint>();

        String strLine;

        strLine = br.readLine();    // Reading the first RoomHardConstraint

        while (strLine.charAt(0) != '[') {

            String[] values_raw = strLine.split(", ");
            int room1 = Integer.parseInt(values_raw[0]);

            RoomHardConstraint roomHardConstraint = new RoomHardConstraint(room1);
            roomHardConstraints.add(roomHardConstraint);

            strLine = br.readLine();
        }

        return roomHardConstraints;

        // TODO : read institutional wheightenings if they are going to be used

    }

}
