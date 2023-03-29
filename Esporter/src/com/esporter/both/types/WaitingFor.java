package com.esporter.both.types;

import java.util.concurrent.atomic.AtomicReferenceArray;

import com.esporter.both.socket.Response;

public class WaitingFor {
	
	private volatile AtomicReferenceArray<Response> goal;
	private Response actualState;
	private Thread t;
	
	public WaitingFor() {
		
	}
	
	public void waitFor(Response... goal) {
		this.goal = new AtomicReferenceArray<>(goal);
		actualState=null;
		t = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("Waiting for "+goal);
				while (continueWaiting()) {
					synchronized (t) {
						try {
							t.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				System.out.println("Not waiting anymore");
			}
		});
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			t.interrupt();
		}
	}
	
	public boolean continueWaiting() {
		if (actualState != null) {
			for (int i = 0; i<goal.length();i++) {
				if (actualState.equals(goal.get(i))) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void setActualState(Response actualState) {
		synchronized (t) {
			this.actualState = actualState;
			t.notify();
		}
	}
	
	public Response getActualState() {
		return actualState;
	}
	
	
	

}
