class Period {
    private String date;
    private String time;
    private int duration;
    private int penalty;
    private int id;

    public Period(int id, String date, String time, int duration, int penalty) {
        this.setId(id);
        this.setDate(date);
        this.setTime(time);
        this.setDuration(duration);
        this.setPenalty(penalty);
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

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * @return the penalty
     */
    public int getPenalty() {
        return penalty;
    }

    /**
     * @param penalty the penalty to set
     */
    public void setPenalty(int penalty) {
        this.penalty = penalty;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof Period)) {
            return false;
        }

        Period p = (Period) o;

        // Compare the data members and return accordingly
        return this.id == p.id;
    }
}