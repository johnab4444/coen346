import java.awt.desktop.SystemEventListener;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class Scheduler extends Thread{
    private Process process;
    private final ReadyQueue<Process> readyQueue;
    private static DecimalFormat df2 = new DecimalFormat("#.###");
    private FileWriter fileWriter = new FileWriter("output.txt", true);


    public Scheduler(Process process, ReadyQueue<Process> readyQueue) throws IOException {
        this.process = process;
        this.readyQueue = readyQueue;
    }

    @Override
    public void run() {

        while(!this.process.isDone()) {

            try {
                compute(fileWriter);

            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void compute(FileWriter fileWriter) throws InterruptedException, IOException {
        synchronized (readyQueue) {
                if (readyQueue.getTotalTime() >= this.process.getArrivalTime() && !readyQueue.isInQueue(this.process.getId())) {
                    readyQueue.insert(this.process);
                } else if (process.getRemainingTime() < 0.01) {
                    this.process.setCompletionTime(readyQueue.getTotalTime());
                    readyQueue.delMin();
                    this.process.allDone();
                    readyQueue.processDone();
                    System.out.println("Time " + df2.format(readyQueue.getTotalTime()) + ", " + Thread.currentThread().getName() + ", Finished");
                    fileWriter.write("\nTime " + df2.format(readyQueue.getTotalTime()) + ", " + Thread.currentThread().getName() + ", Finished");
                }
                if (this.readyQueue.isInQueue(this.process.getId()) && readyQueue.myTurn(this.process)) {
                    if (this.process.isNewbie()) {
                        System.out.println("Time " + df2.format(readyQueue.getTotalTime()) + ", " + Thread.currentThread().getName() + ", Started");
                        fileWriter.write("\nTime " + df2.format(readyQueue.getTotalTime()) + ", " + Thread.currentThread().getName() + ", Started");
                        readyQueue.setTotalTime(this.process.quantum());
                        System.out.println("Time " + df2.format(readyQueue.getTotalTime()) + ", " + Thread.currentThread().getName() + ", Paused");
                        fileWriter.write("\nTime " + df2.format(readyQueue.getTotalTime()) + ", " + Thread.currentThread().getName() + ", Paused");
                        readyQueue.notifyAll();
                        readyQueue.wait();
                    } else {
                        readyQueue.delMin();
                        System.out.println("Time " + df2.format(readyQueue.getTotalTime()) + ", " + Thread.currentThread().getName() + ", Resumed");
                        fileWriter.write("\nTime " + df2.format(readyQueue.getTotalTime()) + ", " + Thread.currentThread().getName() + ", Resumed");
                        readyQueue.setTotalTime(this.process.quantum());
                        readyQueue.insert(this.process);
                        System.out.println("Time " + df2.format(readyQueue.getTotalTime()) + ", " + Thread.currentThread().getName() + ", Paused");
                        fileWriter.write("\nTime " + df2.format(readyQueue.getTotalTime()) + ", " + Thread.currentThread().getName() + ", Paused");
                        readyQueue.notifyAll();
                        if(readyQueue.getProCount() > 1) readyQueue.wait();
                    }
                } else {
                    readyQueue.notifyAll();
                }
        }
    }
    }
