import java.util.ArrayList;

public class Queue {
    private Process pros;
    private Process[] queue;
    private int size;

    //constructor
    public Queue(int s, ArrayList<Process> proList){
        size = s;
        queue = new Process[s];
        pros = new Process(proList);
        firstSort();
    }

    //function used to initially sort the list of processes
    public synchronized void firstSort(){
        Process temp;
        for(int j = 0; j<queue.length;j++){
            temp = null;
            for(int i = 0; i<pros.proSize(); i++){
                if(i==0){
                    temp = new Process((pros.getPro(i)));
                }else if(temp.getArrivalTime() == pros.getPro(i).getArrivalTime()){
                    if(temp.getId() > pros.getPro(i).getId()){
                        temp = pros.getPro(i);
                    }
                }else if (temp.getArrivalTime() > pros.getPro(i).getArrivalTime()){
                    temp = pros.getPro(i);
                }
            }
            queue[j] = temp;
            pros.remove(temp.getId());
        }
        for(int i = 0; i< queue.length; i++){
            pros.addPro(queue[i]);
        }
    }

    //verify if the process is at the front of the queue
    public synchronized boolean frontRunner(int id){
        if(queue[0].getId() == id || queue[1].getId() == id){
            return true;
        }
        else return false;
    }

    // verify if a process is ready to run
    public synchronized boolean readyPro(Process p, int t){
        for(int i = 0; i<pros.proSize(); i++){
            if(p.getId() == pros.getPro(i).getId() && pros.readyCheck(p,t)){
                return true;
            }
        }
        return false;
    }

    //method to verify if other processes are ready and waiting to execute (pre-emptive scheduling)
    public synchronized boolean othersWaiting(Process p, int t){
        if(queue.length < 2){
            if((queue[0].getId() != p.getId() && readyPro(queue[0],t))){
                return true;
            }
        }else {
            if((queue[0].getId() != p.getId() && readyPro(queue[0],t)) || (queue[1].getId() != p.getId() && readyPro(queue[1],t))){
                return true;
            }
        }
        return false;
    }

    //take the process that is being allowed to run and place it at the end of the queue
    public  synchronized void deQueue(Process p){
        if(p.getId() == queue[0].getId()){
            for(int i =0; i<(queue.length-1);i++){
                queue[i] = queue[i+1];
            }
            queue[queue.length-1] = p;
        }else if(p.getId() == queue[1].getId()){
            for(int i = 1; i<(queue.length-1);i++){
                queue[i] = queue[i+1];
            }
            queue[queue.length-1] = p;
        }else return;
    }

    //re-sort the array if processes at the head of the queue are not ready to run
    public synchronized void reSort(int t){
        Process[] newQueue = new Process[queue.length];
        Process temp;
        int k =0;
        for(int i = 0; i<queue.length; i++){
            temp =null;
            for(int j =0; j < pros.proSize(); j++){
                if(queue[j].getArrivalTime() > t){

                }else if(queue[j].getArrivalTime() <= t && temp == null){
                    temp = new Process(queue[j]);
                }else if (queue[j].getArrivalTime() <= t && temp != null && (t-queue[j].getArrivalTime()) < (t-temp.getArrivalTime())){
                    temp = queue[j];
                } else if (queue[j].getArrivalTime() <= t && queue[j].getArrivalTime() == temp.getArrivalTime() && queue[j].getId() < temp.getId()){
                    temp = queue[j];
                }

            }
            if(temp != null){
                newQueue[k++] = temp;
                pros.remove(temp.getId());
            }
        }
        if(pros.proSize() > 0){
            for(int i=0; k<queue.length; i++){
                if(notInArray(queue[i], newQueue)){
                    newQueue[k++] = queue[i];
                    pros.remove(queue[i].getId());
                }
            }
        }
        //pass newly organized queue to the queue variable
        queue = newQueue;
        //re-insert processes into the arraylist
        for(int i = 0; i< queue.length; i++){
            pros.addPro(queue[i]);
        }
    }

    //function to check if a process is in the queue
    private boolean notInArray(Process p, Process[] q){
        for(int i =0; i<q.length; i++){
            if(q[i]==p){
                return false;
            }
        }
        return true;
    }

    //sorting algorithm used when a process has terminated
    private synchronized void freshSort(){
        Process[] newQueue = new Process[pros.proSize()];
        for(int i = 0; i <queue.length; i++){
            if(pros.isInList(queue[i])){
                newQueue[i] = queue[i];
            }
        }
        queue = new Process[pros.proSize()];
        queue = newQueue;
    }

    //function to delete a process from the arraylist once it has terminated
    public synchronized void yeetProcess(Process p){
        pros.remove(p.getId());
        freshSort();
    }
}
