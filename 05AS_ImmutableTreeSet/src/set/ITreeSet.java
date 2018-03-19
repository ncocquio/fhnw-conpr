package set;

public interface ITreeSet<E extends Comparable<E>> {
    ITreeSet<E> insert(E e);
    boolean contains(E e);
    boolean isEmpty();
}