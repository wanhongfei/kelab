package org.kelab.skiplist;

import lombok.Data;

import java.util.Random;

/**
 * Created by hongfei.whf on 2017/5/10.
 */
@Data
public class SkipList<T> {

	private static final double PROBABILITY = 0.5;
	private SkipListNode<T> head, tail;
	private int nodes;
	private int levels;
	private Random rand;

	/**
	 * 构建跳跃表
	 */
	public SkipList() {
		this.rand = new Random();
		clear();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SkipList<String> list = new SkipList<String>();
		System.out.println(list);
		list.put(2, "yan");
		list.put(1, "co");
		list.put(3, "feng");
		list.put(1, "cao");//测试同一个key值
		System.out.println(list.getLevels());
		System.out.println(list);
		System.out.println("====");
		list.remove(2);
//		list.put(4, "曹");
//		list.put(6, "丰");
//		list.put(5, "艳");
		System.out.println(list);
		System.out.println(list.size());
	}

	/**
	 * 是否为空跳跃表
	 *
	 * @return
	 */
	public boolean isEmpty() {
		return nodes == 0;
	}

	/**
	 * 跳跃表大小
	 *
	 * @return
	 */
	public int size() {
		return nodes;
	}

	/**
	 * 清空跳跃表
	 */
	public void clear() {
		this.head = new SkipListNode<T>(SkipListNode.HEAD_KEY, null);
		this.tail = new SkipListNode<T>(SkipListNode.TAIL_KEY, null);
		this.levels = this.nodes = 0;
		left2right(this.head, this.tail);
	}

	/**
	 * 左->右连接
	 *
	 * @param node1
	 * @param node2
	 */
	public void left2right(SkipListNode<T> node1, SkipListNode<T> node2) {
		node1.setRight(node2);
		node2.setLeft(node1);
	}

	/**
	 * 上->下连接
	 *
	 * @param node1
	 * @param node2
	 */
	public void up2down(SkipListNode<T> node1, SkipListNode<T> node2) {
		node1.setDown(node2);
		node2.setUp(node1);
	}

	/**
	 * 查找是否存在key结点，若存在返回value,
	 * 否则返回前一个位置的节点
	 *
	 * @param key
	 * @return
	 */
	private SkipListNode<T> find(int key) {
		SkipListNode<T> p = this.head;
		while (true) {
			while (p.getRight().getKey() != SkipListNode.TAIL_KEY
					&& p.getRight().getKey() <= key) {
				p = p.getRight();
			}
			if (p.getDown() != null) {
				p = p.getDown();
			} else {
				break;
			}
		}
		return p;
	}

	/**
	 * 查询是否存在key，存在返回value，否则返回null
	 *
	 * @param key
	 * @return
	 */
	public SkipListNode<T> search(int key) {
		SkipListNode<T> p = this.find(key);
		if (p.getKey() == key) {
			return p;
		} else {
			return null;
		}
	}

	/**
	 * 跳跃表插入操作
	 *
	 * @param key
	 * @param value
	 */
	public void put(int key, T value) {
		SkipListNode<T> p = find(key);
		if (p.getKey() == key) {
			// 替换最底层节点
			p.setValue(value);
			// 如果存在上层节点，一样替换
			SkipListNode up = p.getUp();
			while (up != null) {
				up.setValue(value);
				up = up.getUp();
			}
			return;
		}
		SkipListNode q = new SkipListNode(key, value);
		append(p, q);
		int currLevel = 0;
		// 概率1/2：是否往上加节点
		while (this.rand.nextDouble() < PROBABILITY) {
			// 如果超出高度，新建一层
			if (currLevel >= this.levels) {
				this.levels++;
				// 新建一层
				SkipListNode<T> p1 = new SkipListNode<T>(SkipListNode.HEAD_KEY, null);
				SkipListNode<T> q1 = new SkipListNode<T>(SkipListNode.TAIL_KEY, null);
				left2right(p1, q1);
				// 连接顶层和新建的一层
				up2down(p1, this.head);
				up2down(q1, this.tail);
				this.head = p1;
				this.tail = q1;
			}
			// 将p移到上层节点
			while (p.getUp() == null) {
				p = p.getLeft();
			}
			p = p.getUp();

			// 在上层比起小的节点后面，添加节点
			SkipListNode<T> e = new SkipListNode<>(key, value);
			append(p, e);
			up2down(e, q);
			q = e;
			currLevel++;
		}
		nodes++;
	}

	/**
	 * 跳跃表删除操作
	 *
	 * @param key
	 */
	public void remove(int key) {
		SkipListNode<T> p = find(key);
		if (p.getKey() == key) {
			// 删除最底层及其上面的结点
			SkipListNode up = p.getUp();
			remove(p);
			while (up != null) {
				remove(up);
				up = up.getUp();
			}
			nodes--;
			// 清除空层
			while (head.getRight().getKey() == SkipListNode.TAIL_KEY) {
				head = head.getDown();
				levels--;
			}
			return;
		}
	}

	/**
	 * node1 后面紧跟着插入node2
	 * node<->node3 ==> node<->node2<->node3
	 *
	 * @param node1
	 * @param node2
	 */
	private void append(SkipListNode node1, SkipListNode node2) {
		node2.setLeft(node1);
		node2.setRight(node1.getRight()/*node3*/);
		node1.getRight()/*node3*/.setLeft(node2);
		node1.setRight(node2);
	}

	/**
	 * 删除node节点
	 * node1<->node<->node2 ==> node1<->node2
	 *
	 * @param node
	 */
	private void remove(SkipListNode node) {
		SkipListNode node1 = node.getLeft();
		SkipListNode node2 = node.getRight();
		left2right(node1, node2);
	}

	/**
	 * 打印出原始数据
	 */
	@Override
	public String toString() {
		if (isEmpty()) {
			return "跳跃表为空！";
		}
		StringBuilder builder = new StringBuilder();
		SkipListNode<T> p = head;
		int plevels = levels;
		while (plevels-- >= 0) {
			SkipListNode<T> q = p.getDown();
			boolean isFirst = true;
			while (p.getRight() != null) {
				if (isFirst) {
					isFirst = false;
				} else {
					builder.append(" <=> ");
				}
				builder.append(p.toString());
				p = p.getRight();
			}
			builder.append("\n");
			p = q;
		}
		return builder.toString();
	}
}
