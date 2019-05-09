class Room {
    private int capacity;
    private int penalty;
    private int id;

    public Room(int id, int capacity, int penalty) {
        this.setId(id);
        this.setCapacity(capacity);
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
     * @return the capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * @param capacity the capacity to set
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}