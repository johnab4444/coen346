import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Scheduler extends Thread {
    private Commander commandList;
    private Commander doCommand;
    private Process process;
    private FileWriter fileWriter;
    private VM vmm;
    private int[] result = new int[2];
    private Thread vmThread;

    private boolean runVM = true;
    private Semaphore runningPros = new Semaphore(2, true);
    private Semaphore commandKey = new Semaphore(1,true);

    public Scheduler (Commander com, Process pro, VM vm, FileWriter fileWriter){
        this.commandList = com;
        this.process = pro;
        this.vmm = vm;
        this.fileWriter = fileWriter;
    }

    private synchronized void writer(String text) throws IOException {
        this.fileWriter.write(text);
    }



    @Override
    public void run() {

        while(vmm.notFinished()){
            if(runVM){
                vmThread = new Thread(vmm);
                vmThread.start();
                runVM = false;
            }
            try {
                compute();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        vmm.allDone();
        try {
            vmThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void compute() throws IOException, InterruptedException {
        synchronized (vmm){

            if(process.getArrivalTime() <= vmm.readClock() && process.notInQueue(process.getId()) && !process.isDone()){
                process.enQueue(process);
            }
            if(!process.emptyQueue() && process.frontOfLine(process.getId())){
                runningPros.acquire();
                process.deQueue(process.getId());
                if (process.isNewbie()) {
                    System.out.println("Clock: " + vmm.readClock() + ", " + currentThread().getName() + ",Started.");
                    writer("Clock: " + vmm.readClock() + ", " + currentThread().getName() + ",Started." + "\n");
                    process.notANewbie(vmm.readClock());
                } else if(vmm.readClock()%1000 == 0){
                    System.out.println("Clock: " + vmm.readClock() + ", " + currentThread().getName() + ",Resumed");
                    writer("Clock: " + vmm.readClock() + ", " + currentThread().getName() + ",Resumed"+ "\n");
                }
                commandKey.acquire();
                doCommand = commandList.nextCommand();
                result = vmm.executeC(doCommand.getCommand(), doCommand.getVar(), doCommand.getValue());
                printOut(result, doCommand, currentThread().getName());
                vmm.checkCommand(commandList.seeCommandCount());
                commandKey.release();

                int p = process.checkTime(vmm.readClock());

                if(process.isDone()){
                    vmm.yeetProcess();
                    System.out.println("Clock: " + vmm.readClock() + currentThread().getName() + ": Finished.");
                    writer("Clock: " + vmm.readClock() + currentThread().getName() + ": Finished."+ "\n");
                }else if(p>0 || !process.emptyQueue()){
                    System.out.println("Clock: " + vmm.readClock() + currentThread().getName() + ": Paused");
                    writer("Clock: " + vmm.readClock() + currentThread().getName() + ": Paused"+ "\n");
                    process.enQueue(process);
                } else{
                    process.enQueue(process);
                }
                runningPros.release();
                vmm.notifyAll();
                vmm.wait();
            } else{
                vmm.notifyAll();
            }
        }
    }

    private synchronized void printOut(int[] r, Commander c, String title) throws IOException {
        switch (c.getCommand()){
            case 's':
                System.out.println("Clock:" + r[2] + ", " + title + ",Store: Variable " + c.getVar() + ", Value: " + c.getValue());
                writer("Clock:" + r[2] + ", " + title + ",Store: Variable " + c.getVar() + ", Value: " + c.getValue()+ "\n");
                if(r[0] >= 0){
                    System.out.println("Clock: " + (r[2]) + ", Memory Manager, SWAP: Variable" + c.getVar() + " with Variable " + r[0]);
                    writer("Clock: " + (r[2]) + ", Memory Manager, SWAP: Variable" + c.getVar() + " with Variable " + r[0]+ "\n");
                }
                break;

            case 'r':
                System.out.println("Clock:" + r[2] + ", " + title + ",Release: Variable" + c.getVar());
                writer("Clock:" + r[2] + ", " + title + ",Release: Variable" + c.getVar()+ "\n");
                break;

            case 'l':
                if(r[0]==-2){
                System.out.println("Clock:" + r[2] + ", " + title + ",Lookup: Variable " + c.getVar() + ", Value: " + r[1]);
                writer("Clock:" + r[2] + ", " + title + ",Lookup: Variable " + c.getVar() + ", Value: " + r[1]+ "\n");
                }else if(r[0] == -1){
                    System.out.println("Clock:" + r[2] + ", " + title + ",Lookup: Variable " + c.getVar() + "...");
                    writer("Clock:" + r[2] + ", " + title + ",Lookup: Variable " + c.getVar() + "..."+ "\n");
                    System.out.println("Clock: " + (r[2]) + ", ERROR variableID does NOT exist");
                    writer("Clock: " + (r[2]) + ", ERROR variableID does NOT exist"+ "\n");
                }else if(r[0] >= 0){
                    System.out.println("Clock:" + r[2] + ", " + title + ",Lookup: Variable " + c.getVar() + "...");
                    writer("Clock:" + r[2] + ", " + title + ",Lookup: Variable " + c.getVar() + "..."+ "\n");
                    System.out.println("Clock: " + (r[2]) + ", Memory Manager, SWAP: Variable" + c.getVar() + " with Variable " + r[0]);
                    writer("Clock: " + (r[2]) + ", Memory Manager, SWAP: Variable" + c.getVar() + " with Variable " + r[0]+ "\n");
                    System.out.println("Clock:" + (r[2]) + ", " + title + ",Lookup: Variable " + c.getVar() + ", Value: " + r[1]);
                    writer("Clock:" + (r[2]) + ", " + title + ",Lookup: Variable " + c.getVar() + ", Value: " + r[1]+ "\n");
                }
                 break;
        }
    }
}
