import java.util.NoSuchElementException;

public class ReadyQueue<Key> {
    private Process[] pq;                 // store items at indices 1 to n
    private int n;          // number of items on priority queue
    private double totalTime = 1;

    public synchronized double getTotalTime(){
        return this.totalTime;
    }

    public synchronized void setTotalTime(double x){
        this.totalTime += x;
    }

    public ReadyQueue(int size) {
        pq = new Process[size + 1];
        n = 0;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    public synchronized int min() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        return pq[1].getId();
    }

    public synchronized void insert(Process x) {
        // add x, and percolate it up to maintain heap invariant
        pq[++n] = x;
        swim(n);
    }

    public synchronized Process delMin() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        Process min = pq[1];
        exch(1, n--);
        sink(1);
        pq[n + 1] = null;     // to avoid loitering and help with garbage collection
        return min;
    }

    private void swim(int k) {
        while (k > 1 && greater(k / 2, k)) {
            exch(k, k / 2);
            k = k / 2;
        }
    }

    private void sink(int k) {
        while (2 * k <= n) {
            int j = 2 * k;
            if (j < n && greater(j, j + 1)) j++;
            if (!greater(k, j)) break;
            exch(k, j);
            k = j;
        }
    }

    public synchronized boolean myTurn(Process pro){
        if(pro.getArrivalTime() > totalTime) return false;
        else if(pro.isNewbie()) return true;
        else return pro.getId() == min();
    }

    private boolean greater(int i, int j) {
        if (pq[j].isNewbie()) {
            return true;
        } else if (pq[i].getRemainingTime() == pq[j].getRemainingTime()) {
            return pq[i].getArrivalTime() > pq[j].getArrivalTime();
        } else {
            return pq[i].getRemainingTime() > pq[j].getRemainingTime();
        }
    }

    private void exch(int i, int j) {
        Process swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
    }

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
}


