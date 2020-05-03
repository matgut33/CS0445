public class MergeSort<T extends Comparable<? super T>>

implements Sorter<T>

{

     private int MIN_SIZE; // min size to recurse, use InsertionSort

                              // for smaller sizes to complete sort

     public MergeSort()

     {

          MIN_SIZE = 3;

     }

// remaining code in MergeSort class not shown

      // You must complete this class – in particular the methods

      // sort() and setMin().

     public void setMin(int m) {
     	MIN_SIZE = m;
     }


     public void sort(T[] a, int s)
	{
		if (s <= MIN_SIZE) insertionSort(a);
		else {
			T[] tmp = (T[]) new Comparable[s];
			mergeSort(a, tmp,  0,  s-1);
		}
	}


	private void mergeSort(T[] a, T[] tmp, int left, int right)
	{
		if( left < right )
		{
			int center = (left + right) / 2;
			mergeSort(a, tmp, left, center);
			mergeSort(a, tmp, center + 1, right);
			merge(a, tmp, left, center + 1, right);
		}
	}


    private void merge(T[ ] a, T[ ] tmp, int left, int right, int rightEnd )
    {
        int leftEnd = right - 1;
        int k = left;
        int num = rightEnd - left + 1;

        while(left <= leftEnd && right <= rightEnd)
            if(a[left].compareTo(a[right]) <= 0)
                tmp[k++] = a[left++];
            else
                tmp[k++] = a[right++];

        while(left <= leftEnd)    // Copy rest of first half
            tmp[k++] = a[left++];

        while(right <= rightEnd)  // Copy rest of right half
            tmp[k++] = a[right++];

        // Copy tmp back
        for(int i = 0; i < num; i++, rightEnd--)
            a[rightEnd] = tmp[rightEnd];
    }

    public void insertionSort(T arr[]) 
      { 
        int n = arr.length; 
        for (int i = 1; i < n; ++i) { 
            T key = arr[i]; 
            int j = i - 1; 
            while (j >= 0 && arr[j].compareTo(key)>1) { 
                arr[j + 1] = arr[j]; 
                j = j - 1; 
            } 
            arr[j + 1] = key; 
        } 
      } 

}