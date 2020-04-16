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
    private ArrayList<Process> processes = new ArrayList<Process>();
    private ArrayList<Process> processQueue = new ArrayList<Process>();

    //constructors

    public Process(int arrivalTime, int burstTime, int id) {
        this.arrivalTime = arrivalTime*1000;
        this.burstTime = burstTime*1000;
        this.id = id;
        this.newbie = true;
        this.done = false;
        this.cpuTime =0;

    }

    public Process(ArrayList<Process> proList){
        processes = proList;
    }

    public Process(Process pro){
        this.arrivalTime = pro.arrivalTime;
        this.burstTime = pro.burstTime;
        this.id = pro.id;
        this.cpuTime = pro.cpuTime;
        this.startTime = pro.startTime;
        this.newbie = pro.newbie;
    }

    public Process(String[] data){
        processCount = Integer.parseInt(data[0]);
        int max = data.length;
        int i = 1;
        int count = 1;
        do{
            processes.add(new Process(Integer.parseInt(data[count++]),Integer.parseInt(data[count++]), i++));
        }while(count<max);
    }

    public Process getPro(int i){
        return processes.get(i);
    }


    public Process(int p){
        processCount = p;
    }

    public int checkTime(int t){
        if((t-startTime) >= burstTime){
            done = true;
            return -1;
        }else if((t-startTime) >= quantum){
            return 1;
        }
        return -1;
    }

    public boolean isNewbie(){
        return newbie;
    }

    public void notANewbie(int t){
        startTime = t;
        newbie = false;
    }

    public synchronized void deQueue(int p){
        if(p == processQueue.get(0).id) {
            Process temp = new Process(processQueue.get(0).arrivalTime, processQueue.get(0).burstTime, processQueue.get(0).id);
            processQueue.remove(0);
        }else if(p == processQueue.get(1).id){
            Process temp = new Process(processQueue.get(1).arrivalTime, processQueue.get(1).burstTime, processQueue.get(1).id);
            processQueue.remove(1);
        }
    }

    public void addPro(Process p){
        processes.add(p);
    }

    public int proSize(){
        return processes.size();
    }

    public boolean readyCheck(Process p, int t){
        if(p.arrivalTime <= t && p.cpuTime < quantum){
            return true;
        }
        return false;
    }


    public boolean emptyQueue(){
        return processQueue.size() == 0;
    }


    public boolean isDone(){
        return done;
    }

    public synchronized void enQueue(Process temp){
        processQueue.add(temp);
    }

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
    public int getBurstTime(){
        return burstTime;
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

    public void remove(int id){
        for(int i = 0; i< processes.size(); i++){
            if(id == processes.get(i).id){
                processes.remove(i);
                return;
            }
        }
    }
}
