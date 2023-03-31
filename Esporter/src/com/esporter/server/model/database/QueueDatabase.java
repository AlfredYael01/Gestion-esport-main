package com.esporter.server.model.database;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class QueueDatabase<T> {

	private LinkedBlockingQueue<Entry<Integer, T>> queue;
	private int max;
	private int actual;
	private int nbElement;
	private static AtomicInteger ID;
	private DatabaseAccess db;
	private final static int CAPACITY = 50;
	
	public QueueDatabase (DatabaseAccess db) {
		queue = new LinkedBlockingQueue<>(CAPACITY);
		max =0;
		actual=0;
		nbElement =0;
		this.db = db;
		ID = new AtomicInteger(0);
	}
	
	public int put(T s){
		if (queue.size()==0) {
			synchronized (db) {
				db.notify();
			}
		}
		try {
			
			queue.put(new SimpleEntry<>(ID.addAndGet(1),s));
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		return ID.get();
	}
	
	public void put(T s, int id){
		try {
			queue.put(new SimpleEntry<>(id,s));
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	public Entry<Integer, T> next(){
		try {
			return queue.take();
		} catch (InterruptedException e) {
			return null;
		}
	}
	
	public Entry<Integer, T> get(int id){
		Entry<Integer, T> t;
		t = queue.peek();
		while(true) {
			if (t!=null) {
				try {
				if (t.getKey()==id)
					return queue.take();
				Thread.sleep(100);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
			t = queue.peek();
		}
	}
	
	public void remove(int i) {
		
	}
	
	public int getNbElement() {
		return queue.size();
	}
}
