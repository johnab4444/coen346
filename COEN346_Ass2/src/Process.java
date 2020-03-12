public class Process {
    private double arrivalTime;
    private double burstTime;
    private double remainingTime;
    private double completionTime;
    private boolean newbie;
    private boolean done = false;
    private int id;

    //constructor
    public Process(double a, double b, int id){
        this.arrivalTime = a;
        this.burstTime = b;
        this.remainingTime = b;
        this.newbie = true;
        this.id = id;
    }

    //check if a process is done its burst time
    public boolean isDone(){
        return this.done;
    }

    //set the process to done
    public void allDone(){
        this.done = true;
    }

    //get the id of a process
    public int getId(){
        return this.id;
    }

    //check if its a processes first time in the cpu
    public boolean isNewbie() {
        return newbie;
    }

    //calculate the quantum of a process which is 10% of its remaining burst time
    public double quantum(){
       if(this.remainingTime == this.burstTime) this.newbie = false;
        double temp = this.remainingTime * 0.1;
        this.remainingTime -= temp;

        return temp;
    }

    //calculate a processes total wait time
    public double waitTime(){
        return this.completionTime - this.arrivalTime - this.burstTime;
    }

    //set a processes completion time
    public void setCompletionTime(double t){
        this.completionTime = t;
    }

    //get a processes remaining time to completion
    public double getRemainingTime(){
        return this.remainingTime;
    }

    //get the processes arrival time
    public double getArrivalTime(){
        return this.arrivalTime;
    }
}
