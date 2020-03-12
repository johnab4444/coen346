import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class Scheduler extends Thread{
    private Process process;
    private final ReadyQueue<Process> readyQueue;
    private static DecimalFormat df2 = new DecimalFormat("#.###");
    private FileWriter fileWriter;

    //constructor
    public Scheduler(Process process, ReadyQueue<Process> readyQueue, FileWriter fileWriter){
        this.process = process;
        this.readyQueue = readyQueue;
        this.fileWriter = fileWriter;
    }

    //method that writes passed text to a file
    public synchronized void writeToFile(String text) throws IOException {
        this.fileWriter.write(text);
    }

    //overridden run method for threads
    @Override
    public void run() {

        //run until the process is done
        while(!this.process.isDone()) {

            try {
                compute();

            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void compute() throws InterruptedException, IOException {
        synchronized (readyQueue) {
            //insert process into the queue if its arrival time is lower then the total time and it is not in the queue
            if (readyQueue.getTotalTime() >= this.process.getArrivalTime() && !readyQueue.isInQueue(this.process.getId())) {
                readyQueue.insert(this.process);

            //if the remaining time of a process is less then 0.01 then delete the process from the queue and end the process
            } else if (process.getRemainingTime() < 0.01) {
                this.process.setCompletionTime(readyQueue.getTotalTime());
                readyQueue.delMin();
                this.process.allDone();
                readyQueue.processDone();
                System.out.println("Time " + df2.format(readyQueue.getTotalTime()) + ", " + Thread.currentThread().getName() + ", Finished");
                writeToFile("Time " + df2.format(readyQueue.getTotalTime()) + ", " + Thread.currentThread().getName() + ", Finished\n");
            }

            //if the process is in the queue and its hes turn for the cpu
            if (this.readyQueue.isInQueue(this.process.getId()) && readyQueue.myTurn(this.process)) {

                //if the process just entered the queue then start running the process and start calculating its quantum
                if (this.process.isNewbie()) {
                    System.out.println("Time " + df2.format(readyQueue.getTotalTime()) + ", " + Thread.currentThread().getName() + ", Started");
                    writeToFile( "Time " + df2.format(readyQueue.getTotalTime()) + ", " + Thread.currentThread().getName() + ", Started\n");
                    readyQueue.setTotalTime(this.process.quantum());
                    System.out.println("Time " + df2.format(readyQueue.getTotalTime()) + ", " + Thread.currentThread().getName() + ", Paused");
                    writeToFile( "Time " + df2.format(readyQueue.getTotalTime()) + ", " + Thread.currentThread().getName() + ", Paused\n");
                    readyQueue.notifyAll();
                    readyQueue.wait();

                //if the process is not a new process then delete it from the queue,
                    // find the quantum and re-insert into the queue to set the new priorities
                } else {
                    readyQueue.delMin();
                    System.out.println("Time " + df2.format(readyQueue.getTotalTime()) + ", " + Thread.currentThread().getName() + ", Resumed");
                    writeToFile( "Time " + df2.format(readyQueue.getTotalTime()) + ", " + Thread.currentThread().getName() + ", Resumed\n");
                    readyQueue.setTotalTime(this.process.quantum());
                    readyQueue.insert(this.process);
                    System.out.println("Time " + df2.format(readyQueue.getTotalTime()) + ", " + Thread.currentThread().getName() + ", Paused");
                    writeToFile( "Time " + df2.format(readyQueue.getTotalTime()) + ", " + Thread.currentThread().getName() + ", Paused\n");
                    readyQueue.notifyAll();
                    if (readyQueue.getProCount() > 1) readyQueue.wait();
                }

            //if it is not this processes turn then just notify the other processes that they can access the cpu
            } else {
                readyQueue.notifyAll();
            }
        }
    }
}
