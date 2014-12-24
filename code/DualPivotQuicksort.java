	public static void sort(int[] a, int left, int right) {
		if(right - left < QUICKSORT_THRESHOLD) {
			sort(a, left, right, true);
			return;
		}
		
		int[] run = new int[MAX_RUN_COUNT + 1];
		int count = 0; run[0] = left;
		
		for (int k = left; k < right; run[count] = k) {
			if (a[k] < a[k + 1]) {
				while (++k <= right && a[k - 1] <= a[k]);
			} else if (a[k] > a[k + 1]) {
				while (++k <= right && a[k - 1] >= a[k]);
				for (int lo = run[count] - 1, hi = k; ++lo < --hi; ) {
					int t = a[lo]; a[lo] = a[hi]; a[hi] = t;
				}
			} else {
				for (int m = MAX_RUN_LENGTH; ++k <= right && a[k - 1] == a[k]; ) {
					if (--m == 0) {
						sort(a, left, right, true);
						return;
					}
				}
			}
			
			if (++count == MAX_RUN_COUNT) {
				sort(a, left, right, true);
				return;
			}
		}
		
		if (run[count] == right++) {
			run[++count] = right;
		} else if (count == 1) {
			return;
		}
		
		int[] b; byte odd = 0;
		for (int n = 1; (n <<= 1) < count; odd ^= 1);
		
		if (odd == 0) {
			b = a; a = new int[b.length];
			for (int i = left - 1; ++i < right; a[i] = b[i]);
		} else {
			b = new int[a.length];
		}
		
		for (int last; count > 1; count = last) {
			for (int k = (last = 0) + 2; k <= count; k += 2) {
				int hi = run[k], mi = run[k - 1];
				for (int i = run[k - 2], p = i, q = mi; i < hi; ++i) {
					if (q >= hi || p < mi && a[p] <= a[q]) {
						b[i] = a[p++];
					} else {
						b[i] = a[q++];
					}
				}
				run[++last] = hi;
			}
			if ((count & 1) != 0) {
				for (int i = right, lo = run[count - 1]; --i >= lo;
						b[i] = a[i]
				);
				run[++last] = right;
			}
			int[] t = a; a = b; b = t;
		}
	}
	
	private static void sort(int[] a, int left, int right, boolean leftmost) {
		int length = right - left + 1;
		
		if (length < INSERTION_SORT_THRESHOLD) {
			if (leftmost) {
				for(int i = left, j = i; i < right; j = ++i) {
					int ai = a[i + 1];
					while (ai < a[j]) {
						a[j + 1] = a[j];
						if (j-- == left) {
							break;
						}
					}
					a[j + 1] = ai;
				}
			} else {
				do {
					if (left >= right) {
						return;
					}
				} while (a[++left] >= a[left - 1]);
				
				for(int k = left; ++left <= right; k = ++left) {
					int a1 = a[k], a2 = a[left];
					
					if (a1 < a2) {
						a2 = a1; a1 = a[left];
					}
					while (a1 < a[--k]) {
						a[k + 2] = a[k];
					}
					a[++k + 1] = a1;
				}
				int last = a[right];
				
				while (last < a[--right]) {
					a[right + 1] = a[right];
				}
				a[right + 1] = last;
			}
			return;
		}
		
		int seventh = (length >> 3) + (length >> 6) + 1;
		
		int e3 = (left + right) >>> 1;
		int e2 = e3 - seventh;
		int e1 = e2 - seventh;
		int e4 = e3 + seventh;
		int e5 = e4 + seventh;
		
		if (a[e2] < a[e1]) { int t = a[e2]; a[e2] = a[e1]; a[e1] = t; }
		
		if (a[e3] < a[e2]) { int t = a[e3]; a[e3] = a[e2]; a[e2] = t;
			if (t < a[e1]) { a[e2] = a[e1]; a[e1] = t;}
		}
		if (a[e4] < a[e3]) { int t = a[e4]; a[e4] = a[e3]; a[e3] = t;
			if (t < a[e2]) { a[e3] = a[e2]; a[e2] = t;
				if (t < a[e1]) { a[e2] = a[e1]; a[e1] = t; }
			}
		}
		if (a[e5] < a[e4]) { int t = a[e5]; a[e5] = a[e4]; a[e4] = t;
			if (t < a[e3]) { a[e4] = a[e3]; a[e3] = t;
				if (t < a[e2]) { a[e3] = a[e2]; a[e2] = t;
					if(t < a[e1]) { a[e2] = a[e1]; a[e1] = t; }
				}
			}
		}
		int less = left;
		int great = right;
		
		if(a[e1] != a[e2] && a[e2] != a[e3] && a[e3] != a[e4] && a[e4] != a[e5]) {
			int pivot1 = a[e2];
			int pivot2 = a[e4];
			
			a[e2] = a[left];
			a[e4] = a[right];
			
			while (a[++less] < pivot1);
			while (a[--great] > pivot2);
			
			outer:
			for (int k = less - 1; ++k <= great; ) {
				int ak = a[k];
				if (ak < pivot1) {
					a[k] = a[less];
					
					a[less] = ak;
					++less;
				} else if (ak > pivot2) {
					while (a[great] > pivot2) {
						if (great-- == k) {
							break outer;
						}
					}
					if (a[great] < pivot1) {
						a[k] = a[less];
						a[less] = a[great];
						++less;
					} else {
						a[k] = a[great];
					}
					
					a[great] = ak;
					--great;
				}
			}
			
			a[left] = a[less - 1]; a[less -1] = pivot1;
			a[right] = a[great + 1]; a[great + 1] = pivot2;
			
			sort(a, left, less - 2, leftmost);
			sort(a, great + 2, right, false);
			
			if (less < e1 && e5 <great) {
				while (a[less] == pivot1) {
					++less;
				}
				while (a[great] == pivot2) {
					--great;
				}
				
				outer:
				for (int k = less - 1; ++k <= great; ) {
					int ak = a[k];
					if (ak == pivot1) {
						a[k] = a[less];
						a[less] = ak;
						++less;
					} else if (ak == pivot2) {
						while (a[great] == pivot2) {
							if (great-- == k) {
								break outer;
							}
						}
						if (a[great] == pivot1) {
							a[k] = a[less];
							a[less] = pivot1;
							++less;
						} else {
							a[k] = a[great];
						}
						a[great] = ak;
						--great;
					}
				}
			}
			
			sort(a, left, great, false);
			
		} else {
			int pivot = a[e3];
			for (int k = less; k <= great; ++k) {
				if (a[k] == pivot) {
					continue;
				}
				int ak = a[k];
				if (ak < pivot) {
					a[k] = a[less];
					a[less] = ak;
					++less;
				} else {
					while (a[great] > pivot) {
						--great;
					}
					if (a[great] < pivot) {
						a[k] = a[less];
						a[less] = a[great];
						++less;
					} else {
						a[k] = pivot;
					}
					a[great] = ak;
					--great;
				}
			}
			
			sort(a, left, less - 1, leftmost);
			sort(a, great + 1, right, false);
		}
	}
