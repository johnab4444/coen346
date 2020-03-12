import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Scanner;

public class Driver {

    private static int processId = 1; //assign each process to an id
    private static DecimalFormat df2 = new DecimalFormat("#.###");

    public static void main(String[] args) throws InterruptedException, IOException {
        int index = 0;
        FileWriter fileWriter = new FileWriter("output.txt", false);
        double[] a = new double[1000]; //array containing each process arrival time
        double[] b = new double[1000]; //array containing each process burst time

        /*read the input file and put data into the arrays*/
        Scanner read = new Scanner(new File("input.txt"));
        while(read.hasNextLine()){
            String line = read.nextLine();
            line = line.replace(" ", "");
            a[index] = Double.parseDouble(String.valueOf(line.charAt(0)));
            b[index] = Double.parseDouble(String.valueOf(line.charAt(1)));
            index++;
        }

        Process[] processList; //array of all the processes
        processList = listBuilder(a, b, index); //insert the processes into the array
        Thread[] threads = new Thread[processList.length]; //array of all the threads

        //priority queue that orders the processes
        ReadyQueue<Process> readyQueue = new ReadyQueue<>(processList.length);

        //create each thread and insert into the array
        for(int i = 0;i<threads.length;i++){
            threads[i] = new Thread(new Scheduler(processList[i], readyQueue, fileWriter), "Process " + processList[i].getId());
        }

        //start all the threads
        for (Thread thread : threads) {
            thread.start();
        }

        //join all the threads
        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("-----------------------------");
        fileWriter.write("-----------------------------\n");
        System.out.println("Waiting Times:");

        //write the waiting time of each process into the output file
        for (Process process : processList) {
            System.out.println("Process " + process.getId() + " : " + df2.format(process.waitTime()));
            fileWriter.write("Process " + process.getId() + " : " + df2.format(process.waitTime()) + "\n");
        }
        fileWriter.flush();
        fileWriter.close();
    }

    /*this method will create an array of processes and assign the parameters for each*/
    public static Process[] listBuilder(double[] a, double[] b, int index){
        Process[] list = new Process[index];

        for(int i = 0;i<list.length;i++){
            list[i] = new Process(a[i], b[i], processId++);
        }
        return list;
    }
}
