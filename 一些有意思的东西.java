	private static int binarysearch0(long[] a, int fromIndex, int toIndex, long key) {
		int low = fromIndex;
		int high = toIndex - 1;
		
		while (low <= high) {
			int mid = (low + high) >>> 1;
			long midVal = a[mid];
			
			if (midVal < key)
				low = mid + 1;
			else if (midVal > key)
				high = mid - 1;
			else 
				return mid;
		}
		return -(low + 1);
	}