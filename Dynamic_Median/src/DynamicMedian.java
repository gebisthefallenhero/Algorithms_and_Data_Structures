import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Scanner;

public class DynamicMedian {
    public int currMedian; //The Current Median
    public LeftistHeap maxHeap; // The max Heap
    public SkewHeap minHeap; // The min heap

    /*
    Public constructor for the median
    Starting median is 0 since the salaries will be positive.
     */
    public DynamicMedian(){
        currMedian = 0;
        maxHeap = new LeftistHeap();
        minHeap = new SkewHeap();
    }


    /**
     * Adds a salary value to the dynamic median
     * @param salary an integer salary value to be passed int.
     */
    public void addSalary(int salary){
        if (currMedian == 0){
            currMedian = salary;
            return;
        }
        if (salary < currMedian){
            maxHeap.insert(salary);
        }
        if (salary >= currMedian){
            minHeap.insert(salary);
        }
        balanceHeaps();
    }

    /**
     * An internal routine to restore the heap balance and update the median
     */
    private void balanceHeaps(){
        final int BALANCE_FACTOR = 2;
        if (minHeap.size - maxHeap.size >= BALANCE_FACTOR){
            int temp = (int) minHeap.deleteMin();
            maxHeap.insert(currMedian);
            currMedian = temp;
        }
        if (maxHeap.size - minHeap.size >= BALANCE_FACTOR){
            int temp = (int) maxHeap.deleteMax();
            minHeap.insert(currMedian);
            currMedian = temp;
        }
    }

    /**
     * A method that takes in a file and returns the median salary. Asumming the file is tab separted and the salaries
     * are at position 0 in the tab.
     * @param fileName
     */
    public void readAndPrint(String fileName) {
        final int SALARY_POSITION = 0;
        final int PRINTEVERY = 25;
        DecimalFormat df = new DecimalFormat("$###,###,###");
        try {
            File salaries = new File(fileName);
            Scanner myReader = new Scanner(salaries);
            int count = -1;// Count starts here to avoid the header line.
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                //This line is to avoid the header line.
                if (count == -1) {
                    count++;
                    continue;
                }
                count++;
                String[] lineValues = line.split("\t");
                int newSalary = Integer.parseInt(lineValues[SALARY_POSITION]);
                addSalary(newSalary);
                if (count % PRINTEVERY == 0){
                    System.out.println("The median salary at " + count + " salaries is " + df.format(currMedian));
                }
            }
            System.out.println("After all salaries the median is " + df.format(currMedian));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DynamicMedian median = new DynamicMedian();
        String fileName = "Salaries.txt";
        median.readAndPrint(fileName);
    }
}
