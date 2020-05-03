import java.util.*;

public class RandomPivot<T extends Comparable<? super T>> implements Partitionable<T> {

	public int random(int low,int high, T[]arr) 
    { 
      
        Random rand= new Random(); 
        int pivot = rand.nextInt(high-low) + low; 
          
        T temp1=arr[pivot]; 
        arr[pivot]=arr[high]; 
        arr[high]=temp1;
        return pivot; 
    } 
      
    public int partition(T[] array, int lo, int hi) {
    	int pivotIndex = random(lo,hi,array);
	    T pivot = array[pivotIndex];

	    // send pivot to the back
	    swap(array, pivotIndex, hi);

	    // index of low/high boundary
	    int index = lo;

	    for (int i = lo; i < hi; i++) {
	      if (array[i].compareTo(pivot) <= 0) { // element is lower or
	                            // equal to the pivot
	                            // swap it to the low
	                            // region
	        swap(array, i, index);
	        index++;
	      }
	    }

	    swap(array, hi, index);

	    return index;
	}


  private static <T> void swap(T[] array, int i, int j) {
    T temp = array[i];
    array[i] = array[j];
    array[j] = temp;
  }  
}