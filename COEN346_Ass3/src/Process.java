import java.util.ArrayList;

public class Process {
    private int arrivalTime;
    private int burstTime;
    private int remainingTime;
    private int completionTime;
    private int processCount;

    private boolean done = false;
    private int id;
    private ArrayList<Process> processes = new ArrayList<Process>();

    //constructor
    public Process(String[] data){
        processCount = Integer.parseInt(data[0]);
        int max = data.length;
        int i = 0;
        int count = 1;
        do{
            processes.add(new Process(Integer.parseInt(data[count++]),Integer.parseInt(data[count++]), i++));
        }while(count<max);
    }

    public Process(int arrivalTime, int burstTime, int id) {
        this.arrivalTime = arrivalTime*1000;
        this.burstTime = burstTime*1000;
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

    //get the processes arrival time
    public double getArrivalTime(){
        return this.arrivalTime;
    }

    public ArrayList<Process> getProcesses() {
        return processes;
    }
}
