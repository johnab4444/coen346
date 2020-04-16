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
    private Integer[] result = new Integer[2];
    private int bump = 1000;
    private int bumps =0;
    private int execTime =0;
    private int clock;
    private boolean runVM = true;
    private Semaphore runningPros = new Semaphore(2, true);

    public Scheduler (Commander com, Process pro, VM vm){
        this.commandList = com;
        this.process = pro;
        this.vmm = vm;
        clock =0;
    }



    @Override
    public void run() {

        while(commandList.showCount() > 0 && !commandList.isEmpty()){
            if(runVM){
                try {
                    Thread vmThread = new Thread(new VM(vmm.getFrameCount()));
                    System.out.println("memcount " + vmm.getMainMem().size());
                    vmThread.start();
                    //vmThread.join();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                runVM = false;
            }
            try {
                execTime = 0;
                compute();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        vmm.allDone();
    }

    public void compute() throws IOException, InterruptedException {
        synchronized (vmm){

            if(process.getArrivalTime() <= clock && process.notInQueue(process.getId())){
                process.enQueue(process);
            }
            if(!process.emptyQueue() && process.getId() == process.firstInLine()){
                do {
                    runningPros.acquire();
                    process.deQueue();
                    if (process.isNewbie()) {
                        System.out.println("Clock: " + clock + ", " + currentThread().getName() + ",Started.");
                        //TODO write to output file
                        process.notANewbie();
                    } else {
                        System.out.println("Clock: " + clock + ", " + currentThread().getName() + ",Resumed");
                        //TODO write to output file
                    }
                    doCommand = commandList.nextCommand();
                    execTime = timeStamp(clock, execTime);
                    result = vmm.executeC(doCommand.getCommand(), doCommand.getVar(), doCommand.getValue(), execTime);
                    printOut(execTime, result, doCommand, currentThread().getName());
                    vmm.sleep(100);
                    process.runOut(clock);
                    passTime();
                }while(process.canStillRun(clock));
                if(process.isDone()){
                    commandList.yeetProcess();
                    System.out.println("Clock: " + clock + currentThread().getName() + ": Finished.");
                }else{
                    System.out.println("Clock: " + clock + currentThread().getName() + ": Paused");
                }
                runningPros.release();
                vmm.notifyAll();
                vmm.wait();
            } else{
                passTime();
                vmm.notifyAll();
            }
        }
    }

    private synchronized void printOut(int t, Integer[] r, Commander c, String title){
        switch (c.getCommand()){
            case 's':
                System.out.println("Clock:" + t + ", " + title + ",Store: Variable " + c.getVar() + ", Value: " + c.getValue());
                if(r[0] >= 0){
                    System.out.println("Clock: " + (t+10) + ", Memory Manager, SWAP: Variable" + c.getVar() + " with Variable " + r[0]);
                }
                break;

            case 'r':
                System.out.println("Clock:" + t + ", " + title + ",Release: Variable" + c.getVar());
                break;

            case 'l':
                System.out.println("Clock:" + t + ", " + title + ",Lookup: Variable " + c.getVar() + ", Value: " + r[1]);
                if(r[0] == -1){
                    System.out.println("Clock: " + (t+10) + ", ERROR variableID does NOT exist");
                }else if(r[0] >= 0){
                    System.out.println("Clock: " + (t+10) + ", Memory Manager, SWAP: Variable" + c.getVar() + " with Variable " + r[0]);
                }
                 break;
        }
        //TODO add write to output
    }

    private synchronized void passTime(){
        clock += bump;
    }


    private synchronized int timeStamp(int t, int x){
        Random rand = new Random();
        return t + x + rand.nextInt(1000-x);
    }
}
