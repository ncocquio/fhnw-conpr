package atomics;

import java.util.concurrent.atomic.AtomicReference;

/**
 * ConcurrentStack
 * <p/>
 * Nonblocking stack using Treiber's algorithm
 * 
 * @author Brian Goetz and Tim Peierls
 */
public class ConcurrentStack<E> {
	AtomicReference<Node<E>> top = new AtomicReference<Node<E>>();

	public void push(E item) {
		Node<E> newHead = new Node<E>(item);
		Node<E> oldHead;
		while (true) {
			oldHead = top.get();
			newHead.next = oldHead;
			if (top.compareAndSet(oldHead, newHead))
				return;
		}
	}

	public E pop() {
		Node<E> oldHead;
		Node<E> newHead;
		while (true) {
			oldHead = top.get();
			if (oldHead == null)
				throw new IllegalStateException();
			newHead = oldHead.next;
			if (top.compareAndSet(oldHead, newHead)) {
				return oldHead.item;
			}
		}
	}

	private static class Node<E> {
		public final E item;
		public Node<E> next;

		public Node(E item) {
			this.item = item;
		}
	}
}
