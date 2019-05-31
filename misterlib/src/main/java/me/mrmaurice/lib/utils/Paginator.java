package me.mrmaurice.lib.utils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class Paginator<T> {

	private T[] objects;
	private double pagSize;
	private int currentPage;
	private int amountOfPages;

	public Paginator(T[] objects, Integer max) {
		this.objects = objects;
		pagSize = new Double(max);
		amountOfPages = (int) Math.ceil(objects.length / pagSize);
	}

	public Paginator(int max) {
		this((T[]) new Object[0], max);
	}

	public void setElements(List<T> objectList) {
		objects = objectList.toArray((T[]) new Object[0]);
		amountOfPages = (int) Math.ceil(objects.length / pagSize);
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

	public List<T> getPage(int pageNum) {
		List<T> page = new ArrayList<>();
		currentPage = pageNum;

		if (objects.length == 0)
			return page;

		double startC = pagSize * (pageNum - 1);
		double finalC = startC + pagSize;

		for (; startC < finalC; startC++)
			if (startC < objects.length)
				page.add(objects[(int) startC]);
		return page;

	}

}
