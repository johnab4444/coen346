import java.util.ArrayList;

public class Process {
    private int arrivalTime;
    private int burstTime;
    private int cpuTime;
    private int processCount;
    private boolean newbie;
    private int quantum = 3000;
    private boolean done = false;
    private int id;
    private ArrayList<Process> processes = new ArrayList<Process>();
    private ArrayList<Process> processQueue = new ArrayList<Process>();

    //constructor
    public Process(String[] data){
        processCount = Integer.parseInt(data[0]);
        int max = data.length-1;
        int j = 0;
        int i = 1;
        int count = 0;
        do{
            processes.add(new Process(Integer.parseInt(data[count++]),Integer.parseInt(data[count++]), i++));
            processes.get(j).newbie = true;
            processes.get(j++).cpuTime = 0;
        }while(count<max);
    }

    public void runOut(int t){
        cpuTime += t;
        burstTime -= t;
    }

    public boolean isNewbie(){
        return newbie;
    }

    public void notANewbie(){
        newbie = false;
    }

    public synchronized void deQueue(){
        Process temp = new Process(processQueue.get(0).arrivalTime,processQueue.get(0).burstTime,processQueue.get(0).id);
        processQueue.remove(0);
    }

    public boolean emptyQueue(){
        return processQueue.size() == 0;
    }

    public synchronized Integer firstInLine(){
        if(emptyQueue()){ return null;}
        else {
            return processQueue.get(0).id;
        }

    }

    public synchronized boolean canStillRun(int t){
        if(cpuTime < quantum && burstTime > 0){return true;}
        else if(cpuTime >= quantum && burstTime > 0){
            cpuTime = 0;
            return false;
        }else {
            done = true;
            return false;
        }
    }

    public boolean isDone(){
        return done;
    }

    public synchronized void enQueue(Process temp){
        processQueue.add(temp);
    }

    public boolean notInQueue(int id){
        if(emptyQueue()){return true;}
        else{
            for(int i =0; i< processQueue.size(); i++){
                if(processQueue.get(i).id == id){return false;}
            }
            return true;
        }
    }

    public Process(int arrivalTime, int burstTime, int id) {
        this.arrivalTime = arrivalTime*1000;
        this.burstTime = burstTime*1000;
        this.id = id;
    }

    public int getProcessCount() {
        return processCount;
    }
    public int getBurstTime(){
        return burstTime;
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
