import java.util.ArrayList;

public class Queue {
    private Process pros;
    private Process[] queue;
    private int size;

    public Queue(int s, ArrayList<Process> proList){
        size = s;
        queue = new Process[s];
        pros = new Process(proList);
        firstSort();
    }

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

    public synchronized boolean frontRunner(int id){
        if(queue[0].getId() == id || queue[1].getId() == id){
            return true;
        }
        else return false;
    }

    public synchronized boolean readyPro(Process p, int t){
        for(int i = 0; i<pros.proSize(); i++){
            if(p.getId() == pros.getPro(i).getId() && pros.readyCheck(p,t)){
                return true;
            }
        }
        return false;
    }

    public synchronized boolean othersWaiting(Process p, int t){
        if((queue[0].getId() != p.getId() && readyPro(queue[0],t)) || (queue[1].getId() != p.getId() && readyPro(queue[1],t))){
            return true;
        }
        return false;
    }

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
        queue = newQueue;
        for(int i = 0; i< queue.length; i++){
            pros.addPro(queue[i]);
        }
    }

    private boolean notInArray(Process p, Process[] q){
        for(int i =0; i<q.length; i++){
            if(q[i]==p){
                return false;
            }
        }
        return true;
    }

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

    public synchronized void yeetProcess(Process p){
        pros.remove(p.getId());
        freshSort();
    }
}
