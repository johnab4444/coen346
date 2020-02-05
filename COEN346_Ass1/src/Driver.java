import java.util.Scanner;
import java.io.*;

public class Driver {
	//declare gobal variable to keep track of thread count
	public static int threadCount = 0;

	public static void main(String[] args) throws InterruptedException, IOException{
		
		//read input file
		Scanner inFile = new Scanner(new File("C:\\Users\\Joe\\Desktop\\coen346\\COEN346_Ass1\\src\\input.txt"));
		
		//declare arrays for the list of bulbs and their positions 
		int[] bulbArray = new int[Integer.parseInt(inFile.nextLine())];
		int[] pos = new int[bulbArray.length];
		int j = 0;
		
		//initialize the arrays using the input file data and position incremented by 1 for each input value
		while(inFile.hasNextLine()) {
			bulbArray[j] = Integer.parseInt(inFile.nextLine());
			pos[j++] = j;
		}
		
		inFile.close();
		
		// create a new object of helper class to keep track of burnt bulb positions
		BulbList bulbs = new BulbList(bulbArray.length);
		
		//call function to find all defective bulbs
		FindDefective(bulbArray,pos,bulbs);
		
		//get the list of defective bulbs
		int[] burntBulbs = bulbs.getList();
		
		if(burntBulbs.length == 0) {
			System.out.println("There are no defective bulbs");
		}
		else if(burntBulbs.length == 1) {
			System.out.println("Bulb #" + burntBulbs[0] + " is defective");
		}else {
			System.out.print("The defective bulbs are: ");
			for(int i=0;i<burntBulbs.length;i++) {
				if(burntBulbs[i]==0) {
					break;
				}
				if(burntBulbs[i+1] == 0) {
					System.out.println("and #" + burntBulbs[i]);
				}
				else {
					System.out.print("#" + burntBulbs[i]+", ");
				}
			}
		}
		System.out.println("The thread count is " + threadCount);

	}
	
	/*verify if at least one defective bulb in list*/
	public static boolean bulbScanner(int[] arr) {
		for(int i=0;i<arr.length;i++) {
			if(arr[i]==0) {
				return true;
			}
		}
		return false;
	}
	
	/*find indexes of defective bulbs*/
	public static void FindDefective(int[] arr,int[] pos,BulbList bulbs) throws InterruptedException {
		
		/*count number of threads called*/
		threadCount++;
		
		/*when array of length 1 and contains a 0 then add its index into array*/
		if(arr.length == 1 && arr[0] == 0) {
			bulbs.register(pos[0]);
			return;
		}
		
		/*if array of length 1 and contains a 1 just return*/
		else if(arr.length == 1 && pos[0] != 0) {
			return;
		}
		
		/*if no defective bulb found in array, return*/
		else if(bulbScanner(arr) == false) {
			return;
		}
		
		/*if defective bulb found*/
		else {
			
			/*separate array into 2 arrays of equal length*/
			int mid = arr.length/2;
			int[] leftArr = new int[mid];
			int[] rightArr = new int[mid];
			int[] leftPos = new int[mid];
			int[] rightPos = new int[mid];
			
			/*insert values of arrays into new arrays*/
			for(int i=0;i<mid;i++) {
				leftArr[i] = arr[i];
				leftPos[i] = pos[i];
				rightArr[i] = arr[i+mid];
				rightPos[i] = pos[i+mid];
			}
			
			/*this thread will handle the left array*/
			Thread thread1 = new Thread(new Runnable() {
				public void run() {
					try {
						FindDefective(leftArr,leftPos,bulbs);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			
			/*this thread will handle right array*/
			Thread thread2 = new Thread(new Runnable() {
				public void run() {
					try {
						FindDefective(rightArr,rightPos,bulbs);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			thread1.start();
			thread2.start();
			thread1.join();
			thread2.join();
		}
	}

}
