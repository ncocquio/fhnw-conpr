package set;

public class TreeSet<E extends Comparable<E>> implements ITreeSet<E> {

	public static <E extends Comparable<E>> ITreeSet<E> empty() {
		throw new RuntimeException("TODO");
	}
	
	@Override
	public ITreeSet<E> insert(E e) {
		throw new RuntimeException("TODO");
	}

	@Override
	public boolean contains(E e) {
		throw new RuntimeException("TODO");
	}

	@Override
	public boolean isEmpty() {
		throw new RuntimeException("TODO");
	}
}
