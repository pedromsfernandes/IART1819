import java.util.ArrayList;

class Exam {
    private int id;
    private int duration;
    private ArrayList<Integer> students;

    public Exam(int id, int duration, ArrayList<Integer> students){
        this.id = id;
        this.duration = duration;
        this.students = students;
    }
}