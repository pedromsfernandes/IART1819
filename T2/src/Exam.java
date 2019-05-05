import java.util.ArrayList;

class Exam {
    private int id;
    private int duration;
    private ArrayList<Integer> students;

    public Exam(int id, int duration, ArrayList<Integer> students) {
        this.setId(id);
        this.setDuration(duration);
        this.setStudents(students);
    }

    /**
     * @return the students
     */
    public ArrayList<Integer> getStudents() {
        return students;
    }

    /**
     * @param students the students to set
     */
    public void setStudents(ArrayList<Integer> students) {
        this.students = students;
    }

    /**
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
}