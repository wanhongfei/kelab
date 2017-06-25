package org.kelab.skiplist;

import lombok.Data;

/**
 * Created by hongfei.whf on 2017/5/10.
 */
@Data
public class SkipListNode<T> {

	public static final int HEAD_KEY = Integer.MAX_VALUE;
	public static final int TAIL_KEY = Integer.MAX_VALUE;

	private int key;
	private T value;

	private SkipListNode<T> up, down, left, right;

	public SkipListNode(int key, T value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SkipListNode)) return false;
		if (!super.equals(o)) return false;

		SkipListNode<?> that = (SkipListNode<?>) o;

		if (key != that.key) return false;
		return value != null ? value.equals(that.value) : that.value == null;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + key;
		result = 31 * result + (value != null ? value.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return key + "-" + value;
	}
}
