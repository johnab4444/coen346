import java.io.*;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Scanner;

public class Driver {
    public static void main(String[] args) throws IOException {
        Commander commanders;
        Filer filerC = new Filer("commands.txt");
        Filer filerP = new Filer("processes.txt");
        Filer filerM = new Filer("memconfig.txt");
        ArrayList<Character> orders = new ArrayList<Character>();
        ArrayList<Integer> vars = new ArrayList<Integer>();
        ArrayList<Integer> vals = new ArrayList<Integer>();
        Process processes;


        String[] commandInput = filerC.readFile().split("\\s+");
        String[] processInput = filerP.readFile().split("\\s+");
        String[] memconfigInput = filerM.readFile().split("\\s+");

        int[] memSize = new int[Integer.parseInt(memconfigInput[0])];
        System.out.println(memSize.length);

        filerC.commandOrganizer(commandInput);
        orders = filerC.getOrders();
        vals = filerC.getVals();
        vars = filerC.getVars();
        commanders = new Commander(orders, vars, vals);
        processes = new Process(processInput);
        System.out.println(commanders.getCommandList().get(4).getCommand());
        System.out.println(processes.getProcesses().get(0).getArrivalTime());

    }
}
