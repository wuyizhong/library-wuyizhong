public abstract class Buffer {

	static void checkBounds(int off, int len, int size) {
		if ((off | len | (off + len) | (size - (off + len))) < 0)
			throw new IndexOutOfBoundsException();
	}
}
