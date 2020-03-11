public class Process {
    private double arrivalTime;
    private double burstTime;
    private double remainingTime;
    private double completionTime;
    private boolean newbie;
    private int id;

    public Process(double a, double b, int id){
        this.arrivalTime = a;
        this.burstTime = b;
        this.remainingTime = b;
        this.newbie = true;
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public boolean isNewbie() {
        return newbie;
    }

    public double quantum(){
       if(this.remainingTime == this.burstTime) this.newbie = false;
        double temp = this.remainingTime * 0.1;
        this.remainingTime -= temp;

        return temp;
    }

    public double waitTime(){
        return this.completionTime - this.arrivalTime - this.burstTime;
    }

    public void setCompletionTime(double t){
        this.completionTime = t;
    }

    public double getRemainingTime(){
        return this.remainingTime;
    }

    public double getArrivalTime(){
        return this.arrivalTime;
    }
}
