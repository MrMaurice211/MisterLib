package me.mrmaurice.lib.utils;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

@SuppressWarnings("unchecked")
public class Paginator<T> {

	private T dullObject;
	private List<T> objects;
	private double pagSize;
	private int currentPage;
	private int amountOfPages;

	public Paginator(T[] objects, int max) {
		this.objects = Lists.newLinkedList(Arrays.asList(objects));
		pagSize = max;
		amountOfPages = (int) Math.ceil(objects.length / pagSize);
	}

	public Paginator(int max) {
		this((T[]) new Object[0], max);
	}

	public void setElements(List<T> objectList) {
		objects = Lists.newLinkedList(objectList);
		amountOfPages = (int) Math.ceil(objects.size() / pagSize);
	}

	public void addElement(T element) {
		objects.add(element);
		amountOfPages = (int) Math.ceil(objects.size() / pagSize);
	}

	public void delElement(T element) {
		objects.remove(element);
		amountOfPages = (int) Math.ceil(objects.size() / pagSize);
	}

	public boolean hasNext() {
		return currentPage < amountOfPages;
	}

	public boolean hasPrev() {
		return currentPage > 1;
	}

	public int getNext() {
		return currentPage + 1;
	}

	public int getPrev() {
		return currentPage - 1;
	}

	public int getTotalPages() {
		return amountOfPages;
	}

	public int getCurrent() {
		return currentPage;
	}

	public void setCurrent(int pageNum) {
		currentPage = pageNum;
	}

	public void setDullObject(T object) {
		dullObject = object;
	}

	public T getDullObject() {
		return dullObject;
	}

	public List<T> getPage() {
		List<T> page = Lists.newLinkedList();

		if (objects.size() == 0) {
			for (int i = 0; i < pagSize; i++)
				page.add(dullObject);
			return page;
		}

		double startC = pagSize * (currentPage - 1);
		double finalC = startC + pagSize;

		for (; startC < finalC; startC++)
			if (startC < objects.size())
				page.add(objects.get((int) startC));
			else
				page.add(dullObject);
		return page;

	}

}
