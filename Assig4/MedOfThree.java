public class MedOfThree<T extends Comparable<? super T>> implements Partitionable<T> {
	public int partition(T[] list, int first, int last)
	{
		T pivot = list[first];
	    int low = first + 1;
	    int high = last;

	    while (high > low) {
	        while (low <= high && (list[low].compareTo(pivot) <= 0)){
	            low++;
	        }

	        while (low <= high && (list[high].compareTo(pivot) > 0)){
	            high--;
	        }

	        if (high > low){
	            T temp = list[high];
	            list[high] = list[low];
	            list[low] = temp;
	        }
	    }

	    while (high > first && (list[high].compareTo(pivot) >= 0)){
	        high--;
	    }

	    if (pivot.compareTo(list[high]) > 0){
	        list[first] = list[high];
	        list[high] = pivot;
	        return high;
	    } else{
	        return first;
	    }
	}  	
}