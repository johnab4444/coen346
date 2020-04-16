import java.io.*;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Scanner;

public class Driver {
    public static void main(String[] args) throws IOException, InterruptedException {
        Commander commanders; //will hold all our commands
        Filer filerC = new Filer("commands.txt");
        Filer filerP = new Filer("processes.txt");
        Filer filerM = new Filer("memconfig.txt");
        ArrayList<Character> orders = new ArrayList<Character>();
        ArrayList<Integer> vars = new ArrayList<Integer>();
        ArrayList<Integer> vals = new ArrayList<Integer>();
        Process processes;
        Queue readyQueue;
        FileWriter fileWriter = new FileWriter("output.txt", false);




        String[] commandInput = filerC.readFile().split("\\s+"); //read commands.txt
        String[] processInput = filerP.readFile().split("\\s+"); //read processes.txt
        String[] memconfigInput = filerM.readFile().split("\\s+"); //read memconfig.txt


        filerC.commandOrganizer(commandInput);
        orders = filerC.getOrders();
        vals = filerC.getVals();
        vars = filerC.getVars();
        processes = new Process(processInput);
        commanders = new Commander(orders, vars, vals);
        readyQueue = new Queue(processes.getProcessCount(), processes.getProcesses());

        VM vm = new VM(Integer.parseInt(memconfigInput[0]), processes.getProcessCount(), commanders.seeCommandCount());

        Thread[] threads = new Thread[processes.getProcessCount()];
        //create each thread and insert into the array
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new Scheduler(commanders, processes.getProcesses().get(i), vm, fileWriter, readyQueue), "Process " + processes.getProcesses()
                    .get(i).getId());
        }

        //start all process threads
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

        //join all process threads
        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }

        fileWriter.flush();
        fileWriter.close();
    }

}
