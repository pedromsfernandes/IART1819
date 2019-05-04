import java.io.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {

        Problem problem = new Problem(args[0]);

        ArrayList<Room> rooms = problem.getRooms();

        for(Room room : rooms){
            System.out.println(room.getCapacity());
        }

        return;
    }

    public static void writeSolution(ArrayList<String> solution, String filename) throws IOException {
        FileOutputStream fstream = null;

        try {
            fstream = new FileOutputStream(filename + ".sln");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fstream));

        for (String exam : solution) {
            System.out.println(exam);
            bw.write(exam + "\r\n");
        }

        bw.close();
    }

}
