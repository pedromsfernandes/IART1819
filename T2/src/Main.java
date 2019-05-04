import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void usage() {
        System.out.println("Usage: java Main <FILENAME> <ALGORITHM>");
        System.out.println("Where ALGORITHM can be one of the following: GENETIC, HILLCLIMB or ANNEALING.");
        System.out.println("Example: java Main ../res/exam_comp_set1.exam GENETIC");
    }

    public static void main(String[] args) {

        ArrayList<String> algorithms = new ArrayList<String>(Arrays.asList("GENETIC", "HILLCLIMB", "ANNEALING"));

        if (args.length != 2 || !algorithms.contains(args[1])) {
            usage();
            return;
        }

        try {
            Problem problem = new Problem(args[0]);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
