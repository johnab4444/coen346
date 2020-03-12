import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class Scheduler extends Thread{
    private Process process;
    private final ReadyQueue<Process> readyQueue;
    private static DecimalFormat df2 = new DecimalFormat("#.###");
    private FileWriter fileWriter = new FileWriter("output.txt", false);


    public Scheduler(Process process, ReadyQueue<Process> readyQueue, FileWriter fileWriter) throws IOException {
        this.process = process;
        this.readyQueue = readyQueue;
        this.fileWriter = fileWriter;
    }

    public synchronized void writeToFile(String text) throws IOException {
        this.fileWriter.write(text);
    }

    @Override
    public void run() {

        while(!this.process.isDone()) {

            try {
                compute();

            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
        /*try {
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public void compute() throws InterruptedException, IOException {
        synchronized (readyQueue) {
            if (readyQueue.getTotalTime() >= this.process.getArrivalTime() && !readyQueue.isInQueue(this.process.getId())) {
                readyQueue.insert(this.process);
            } else if (process.getRemainingTime() < 0.01) {
                this.process.setCompletionTime(readyQueue.getTotalTime());
                readyQueue.delMin();
                this.process.allDone();
                readyQueue.processDone();
                System.out.println("Time " + df2.format(readyQueue.getTotalTime()) + ", " + Thread.currentThread().getName() + ", Finished");
                writeToFile("Time " + df2.format(readyQueue.getTotalTime()) + ", " + Thread.currentThread().getName() + ", Finished\n");
            }
            if (this.readyQueue.isInQueue(this.process.getId()) && readyQueue.myTurn(this.process)) {
                if (this.process.isNewbie()) {
                    System.out.println("Time " + df2.format(readyQueue.getTotalTime()) + ", " + Thread.currentThread().getName() + ", Started");
                    writeToFile( "Time " + df2.format(readyQueue.getTotalTime()) + ", " + Thread.currentThread().getName() + ", Started\n");
                    readyQueue.setTotalTime(this.process.quantum());
                    System.out.println("Time " + df2.format(readyQueue.getTotalTime()) + ", " + Thread.currentThread().getName() + ", Paused");
                    writeToFile( "Time " + df2.format(readyQueue.getTotalTime()) + ", " + Thread.currentThread().getName() + ", Paused\n");
                    readyQueue.notifyAll();
                    readyQueue.wait();
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
            } else {
                readyQueue.notifyAll();
            }
        }
    }
}
