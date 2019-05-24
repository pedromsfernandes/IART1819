class PeriodHardConstraint {
    private int exam1;
    private int exam2;
    private String constraint; // TODO: enum?

    public PeriodHardConstraint(int exam1, int exam2, String constraint) {
        this.setExam1(exam1);
        this.setExam2(exam2);
        this.setConstraint(constraint);
    }

    /**
     * @return the constraint
     */
    public String getConstraint() {
        return constraint;
    }

    /**
     * @param constraint the constraint to set
     */
    public void setConstraint(String constraint) {
        this.constraint = constraint;
    }

    /**
     * @return the exam2
     */
    public int getExam2() {
        return exam2;
    }

    /**
     * @param exam2 the exam2 to set
     */
    public void setExam2(int exam2) {
        this.exam2 = exam2;
    }

    /**
     * @return the exam1
     */
	public int getExam1() {
		return exam1;
	}

	/**
	 * @param exam1 the exam1 to set
	 */
	public void setExam1(int exam1) {
		this.exam1 = exam1;
	}
}