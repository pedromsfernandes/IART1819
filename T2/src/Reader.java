import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Reader {

    Integer numSlots;
    Integer numSubjets;
    ArrayList<ArrayList<Integer>> studentsSubjects = new ArrayList<ArrayList<Integer>>();

    public void Reader (String[] args) throws IOException {
        try {
            readFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFile() throws IOException {
        // Open the file
        FileInputStream fstream = new FileInputStream("teste2ex.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String strLine;

        String[] initialValues;

        strLine = br.readLine();
        initialValues = strLine.split(" ");
        this.numSlots = Integer.parseInt(initialValues[0]);
        this.numSubjets = Integer.parseInt(initialValues[1]);

        //Read File Line By Line
        while ((strLine = br.readLine()) != null) {
            String[] students = strLine.split(" ");
            ArrayList<Integer> newStudents = new ArrayList<Integer>();

            for(String s: students) {
                newStudents.add(Integer.parseInt(s));
            }

            this.studentsSubjects.add(newStudents);
        }

        //Close the input stream
        fstream.close();
    }
}
