import java.util.ArrayList;

public class Process {
    private int arrivalTime;
    private int burstTime;
    private int cpuTime;
    private int startTime;
    private int processCount;
    private boolean newbie;
    private int quantum = 3000;
    private boolean done;
    private int id;
    private int lastTimeStamp;
    private ArrayList<Process> processes = new ArrayList<Process>();

    //constructors
    //individual Process initialization constructor
    public Process(int arrivalTime, int burstTime, int id) {
        this.arrivalTime = arrivalTime*1000;
        this.burstTime = burstTime*1000;
        this.id = id;
        this.newbie = true;
        this.done = false;
        this.cpuTime =0;

    }
    //copy constructor for passing arraylist
    public Process(ArrayList<Process> proList){
        processes = proList;
    }

    public void punchClock(int t){
        lastTimeStamp = t;
    }

    public int timeStamp(){
        return lastTimeStamp;
    }

    public Process(Process pro){
        this.arrivalTime = pro.arrivalTime;
        this.burstTime = pro.burstTime;
        this.id = pro.id;
        this.cpuTime = pro.cpuTime;
        this.startTime = pro.startTime;
        this.newbie = pro.newbie;
    }

    //constructor for initial input list of processes
    public Process(String[] data){
        processCount = Integer.parseInt(data[0]);
        int max = data.length;
        int i = 1;
        int count = 1;
        do{
            processes.add(new Process(Integer.parseInt(data[count++]),Integer.parseInt(data[count++]), i++));
        }while(count<max);
    }

    //returns a process
    public Process getPro(int i){
        return processes.get(i);
    }

    //returns the amount original amount of processes
    public Process(int p){
        processCount = p;
    }

    //checks if the process has finished executing
    public int checkTime(int t){
        if((t-startTime) >= burstTime){
            done = true;
            return -2;
        }else if((t-startTime) >= quantum){
            return 1;
        }
        return -1;
    }

    //check if process has had a turn with the CPU yet
    public boolean isNewbie(){
        return newbie;
    }

    //declares start time of process and changes status to not 'New'
    public void notANewbie(int t){
        startTime = t;
        newbie = false;
    }

    //adds a process to the arrayList
    public void addPro(Process p){
        processes.add(p);
    }

    //returns number of processes in ArrayList
    public int proSize(){
        return processes.size();
    }

    //verify if the process is ready to execute
    public boolean readyCheck(Process p, int t){
        if(p.arrivalTime <= t && p.cpuTime < quantum){
            return true;
        }
        return false;
    }

    //verify if the process is done
    public boolean isDone(){
        return done;
    }


    //verifies if the process is in the arrayList
    public boolean isInList(Process p){
        for(int i= 0; i<processes.size(); i++) {
            if (p.id == processes.get(i).id) {
                return true;
            }
        }
        return false;
    }


    public int getProcessCount() {
        return processCount;
    }


    //get the id of a process
    public int getId(){
        return this.id;
    }

    //get the processes arrival time
    public int getArrivalTime(){
        return this.arrivalTime;
    }

    public ArrayList<Process> getProcesses() {
        return processes;
    }

    //removes process from the arrayList
    public void remove(int id){
        for(int i = 0; i< processes.size(); i++){
            if(id == processes.get(i).id){
                processes.remove(i);
                return;
            }
        }
    }
}
