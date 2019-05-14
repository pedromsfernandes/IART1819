import java.util.ArrayList;

class Exam {
    private int id;
    private int duration;
    private ArrayList<Integer> students;
    private Room room;
    private Period period;

    public Exam(int id, int duration, ArrayList<Integer> students) {
        this.setId(id);
        this.setDuration(duration);
        this.setStudents(students);
    }

    public Exam(Exam exam){
        this.id = exam.id;
        this.duration = exam.duration;
        this.students = exam.students;
        this.room = exam.room;
        this.period = exam.period;
    }

    /**
     * @return the period
     */
    public Period getPeriod() {
        return period;
    }

    /**
     * @param period the period to set
     */
    public void setPeriod(Period period) {
        this.period = period;
    }

    /**
     * @return the room
     */
    public Room getRoom() {
        return room;
    }

    /**
     * @param room the room to set
     */
    public void setRoom(Room room) {
        this.room = room;
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

    @Override
    public String toString(){
        return period.getId() + ", " + room.getId();
    }
}