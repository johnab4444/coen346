/*import java.util.NoSuchElementException;

public class ReadyQueue<Key> {
    private Process[] pq;                 // store items at indices 1 to n
    private int n;                        // number of items on priority queue
    private double totalTime = 1;         // total running time of the cpu
    private int proCount;                 // counter that counts the number of processes in the queue

    //retrieve the total time at a specific time
    public synchronized double getTotalTime(){
        return this.totalTime;
    }

    //set the total time
    public synchronized void setTotalTime(double x){
        this.totalTime += x;
    }

    //create the queue
    public ReadyQueue(int size) {
        pq = new Process[size + 1];
        n = 0;
        proCount = size;
    }

    //decrease the process count when a process is done
    public void processDone(){
        proCount--;
    }

    //retrieve the process count
    public int getProCount(){
        return proCount;
    }

    //check if queue is empty
    public boolean isEmpty() {
        return n == 0;
    }

    //return the id of the process at the top of the queue
    public synchronized int min() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        return pq[1].getId();
    }

    //insert a process into the queue
    public synchronized void insert(Process x) {
        pq[++n] = x;
        swim(n);
    }

    //delete the process at the top of the queue
    public synchronized Process delMin() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        Process min = pq[1];
        exch(1, n--);
        sink(1);
        pq[n + 1] = null;
        return min;
    }

    //swim a process to the top if it has higher priority
    private void swim(int k) {
        while (k > 1 && greater(k / 2, k)) {
            exch(k, k / 2);
            k = k / 2;
        }
    }

    //sink a process if it has lower priority
    private void sink(int k) {
        while (2 * k <= n) {
            int j = 2 * k;
            if (j < n && greater(j, j + 1)) j++;
            if (!greater(k, j)) break;
            exch(k, j);
            k = j;
        }
    }

    //check if a process is allowed the cpu
    public synchronized boolean myTurn(Process pro){
        if(pro.isNewbie()) return true;
        else if(someoneNew()) {
            return false;
        }else {
            return pro.getId() == min();
        }
    }


    public synchronized boolean someoneNew(){
        for(int i = 1; i<=n; i++){
            if(pq[i].getArrivalTime() < totalTime && pq[i].isNewbie()) return true;
        }
        return false;
    }

    //compare processes
    private boolean greater(int i, int j) {
        if (pq[j].isNewbie()) {
            return true;
        } else if (pq[i].getRemainingTime() == pq[j].getRemainingTime()) {
            return pq[i].getArrivalTime() > pq[j].getArrivalTime();
        } else {
            return pq[i].getRemainingTime() > pq[j].getRemainingTime();
        }
    }

    //exchange position of 2 processes
    private void exch(int i, int j) {
        Process swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
    }

    //check if a process is in the queue
    public boolean isInQueue(int x) {
        if (isEmpty()) {
            return false;
        } else {
            for (int i = 1; i <= n; i++) {
                if (pq[i].getId() == x) {
                    return true;
                }
            }
        }
        return false;
    }
}*/


