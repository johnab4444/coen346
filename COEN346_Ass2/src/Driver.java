public class Driver {
    private static int count = 1;
    private static double totalTime = 1;
    public static void main(String[] args) throws InterruptedException {
        double[] a = new double[]{1, 2, 3, 4};
        double[] b = new double[]{5, 3, 1, 6};

        Process[] processList = new Process[a.length];
        processList = listBuilder(a, b);
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

        System.out.println(processList[0].waitTime());



    }



    public static Process[] listBuilder(double a[], double b[]){
        Process[] list = new Process[a.length];

        for(int i = 0;i<list.length;i++){
            list[i] = new Process(a[i], b[i], count++);
        }
        return list;
    }
}
