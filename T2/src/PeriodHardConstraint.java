class PeriodHardConstraint {
    private int exam1;
    private int exam2;
    private String constraint; // TODO: enum?

    public PeriodHardConstraint(int exam1, int exam2, String constraint){
        this.exam1 = exam1;
        this.exam2 = exam2;
        this.constraint = constraint;
    }
}