import java.awt.desktop.SystemEventListener;

public class Scheduler extends Thread{
    private Process process;
    private final ReadyQueue<Process> readyQueue;
    private int i =0;

    public Scheduler(Process process, ReadyQueue<Process> readyQueue){
        this.process = process;
        this.readyQueue = readyQueue;
    }

    @Override
    public void run() {

        while(!this.process.isDone()) {

            try {
                compute();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void compute() throws InterruptedException {
        synchronized (readyQueue) {
                if (readyQueue.getTotalTime() >= this.process.getArrivalTime() && !readyQueue.isInQueue(this.process.getId())) {
                    readyQueue.insert(this.process);
                } else if (process.getRemainingTime() < 0.01) {
                    this.process.setCompletionTime(readyQueue.getTotalTime());
                    readyQueue.delMin();
                    this.process.allDone();
                    readyQueue.processDone();
                    System.out.println("Time " + readyQueue.getTotalTime() + ", " + Thread.currentThread().getName() + ", Finished");
                }
                if (this.readyQueue.isInQueue(this.process.getId()) && readyQueue.myTurn(this.process)) {
                    System.out.println("newbie is: " + this.process.isNewbie());
                    if (this.process.isNewbie()) {
                        System.out.println("Time " + readyQueue.getTotalTime() + ", " + Thread.currentThread().getName() + ", Started");
                        readyQueue.setTotalTime(this.process.quantum());
                        System.out.println("Time " + readyQueue.getTotalTime() + ", " + Thread.currentThread().getName() + ", Paused");
                        if (readyQueue.isInQueue(this.process.getId())) System.out.println("its here");
                        readyQueue.wait();
                    } else {
                        readyQueue.delMin();
                        System.out.println("Time " + readyQueue.getTotalTime() + ", " + Thread.currentThread().getName() + ", Resumed");
                        readyQueue.setTotalTime(this.process.quantum());
                        readyQueue.insert(this.process);
                        System.out.println("Time " + readyQueue.getTotalTime() + ", " + Thread.currentThread().getName() + ", Paused");
                        readyQueue.notifyAll();
                        if(readyQueue.getProCount() > 1) readyQueue.wait();
                    }
                } else {
                    readyQueue.notifyAll();
                }
        }
    }
    }
