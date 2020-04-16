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
    }

    public Process(String[] data){
        processCount = Integer.parseInt(data[0]);
        int max = data.length;
        int j = 0;
        int i = 1;
        int count = 1;
        do{
            processes.add(new Process(Integer.parseInt(data[count++]),Integer.parseInt(data[count++]), i++));
            processes.get(j).newbie = true;
            processes.get(j).done = false;
            processes.get(j++).cpuTime = 0;
        }while(count<max);
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

    public boolean progress(int t){
        if(t-arrivalTime >0){return true;}
        else{return false;}
    }

    public boolean emptyQueue(){
        return processQueue.size() == 0;
    }

    public synchronized boolean frontOfLine(int p){
        if(emptyQueue()){ return false;}
        else if(processQueue.get(0).id == p || processQueue.get(1).id == p){
            return true;
        }
        return false;
    }

    public synchronized boolean canStillRun(){
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
