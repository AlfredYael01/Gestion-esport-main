package com.esporter.both.types;

import java.util.concurrent.atomic.AtomicReferenceArray;

import com.esporter.both.socket.Response;

public class WaitingFor {
	
	private AtomicReferenceArray<Response> goals;
	private Response actualState;
	private Thread t;
	
	public WaitingFor() {
		
	}
	
	public void waitFor(Response... goal) {
		this.goals = new AtomicReferenceArray<>(goal);
		actualState=null;
		t = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("Waiting for "+goal);
				while (continueWaiting()) {
					synchronized (goals) {

						try {
							goals.wait();
							System.out.println("out of sleep");
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
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
		if(actualState != null) {
			for (int i = 0; i<goals.length();i++) {
				if (actualState.equals(goals.get(i))) {
					return false;
				}
			}
		}
		return true;
	}
	
	public  void setActualState(Response actualState) {
		
		synchronized (goals) {
			this.actualState = actualState;
			goals.notifyAll();
		}
		
	}
	
	public Response getActualState() {
		return actualState;
	}
	
	
	

}
