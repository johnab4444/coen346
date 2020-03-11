public class Scheduler extends Thread{
    private Process process;
    private ReadyQueue<Process> readyQueue;

    public Scheduler(Process process, ReadyQueue<Process> readyQueue){
        this.process = process;
        this.readyQueue = readyQueue;
    }

    @Override
    public void run() {
            try {
                if (readyQueue.getTotalTime() > this.process.getArrivalTime() && !readyQueue.isInQueue(this.process.getId())) {
                    readyQueue.insert(this.process);
                } else if (process.getRemainingTime() < 0.01) {
                    this.process.setCompletionTime(readyQueue.getTotalTime());
                    readyQueue.delMin();
                    System.out.println("Time " + readyQueue.getTotalTime() + ", " + Thread.currentThread().getName() + ", Finished");
                    return;
                }

                compute();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

    }

    public void compute() throws InterruptedException {
        synchronized (readyQueue) {
            while (readyQueue.myTurn(this.process)) {
                if (process.isNewbie()) {
                    System.out.println("Time " + readyQueue.getTotalTime() + ", " + Thread.currentThread().getName() + ", Started");
                    readyQueue.setTotalTime(process.quantum());
                    System.out.println("Time " + readyQueue.getTotalTime() + ", " + Thread.currentThread().getName() + ", Paused");
                    if(readyQueue.isInQueue(this.process.getId())) System.out.println("its here");
                    readyQueue.notifyAll();
                } else {
                    readyQueue.delMin();
                    System.out.println("Time " + readyQueue.getTotalTime() + ", " + Thread.currentThread().getName() + ", Resumed");
                    readyQueue.setTotalTime(process.quantum());
                    readyQueue.insert(process);
                    System.out.println("Time " + readyQueue.getTotalTime() + ", " + Thread.currentThread().getName() + ", Paused");
                    readyQueue.notifyAll();
                }
            }
        }
    }



}
