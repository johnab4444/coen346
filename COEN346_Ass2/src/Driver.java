import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Scanner;

public class Driver {

    private static int count = 1;
    private static DecimalFormat df2 = new DecimalFormat("#.###");

    public static void main(String[] args) throws InterruptedException, IOException {
        int index = 0;
        double[] a = new double[1000];
        double[] b = new double[1000];
        Scanner read = new Scanner(new File("input.txt"));
        while(read.hasNextLine()){
            String line = read.nextLine();
            line = line.replace(" ", "");
            a[index] = Double.parseDouble(String.valueOf(line.charAt(0)));
            b[index] = Double.parseDouble(String.valueOf(line.charAt(1)));
            index++;
        }
        //double[] a = new double[]{1, 2, 3, 4};
        //double[] b = new double[]{5, 3, 1, 6};

        Process[] processList;
        processList = listBuilder(a, b, index);
        Thread[] threads = new Thread[processList.length];

        ReadyQueue<Process> readyQueue = new ReadyQueue<>(processList.length);

        for(int i = 0;i<threads.length;i++){
            threads[i] = new Thread(new Scheduler(processList[i], readyQueue), "Process " + processList[i].getId());
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }

        System.out.println("-----------------------------");
        System.out.println("Waiting Times:");
        for(int i=0;i<processList.length;i++){
            System.out.println("Process " + processList[i].getId() + " : " + df2.format(processList[i].waitTime()));
        }
    }

    public static Process[] listBuilder(double[] a, double[] b, int index){
        Process[] list = new Process[index];

        for(int i = 0;i<list.length;i++){
            list[i] = new Process(a[i], b[i], count++);
        }
        return list;
    }
}
