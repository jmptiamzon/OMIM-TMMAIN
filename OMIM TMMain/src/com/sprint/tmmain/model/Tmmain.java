package com.sprint.tmmain.model;

public class Tmmain {
	private String sku;
	private double qty;
	private int tmmainQty;
	private double delta;
	
	public Tmmain() {
		sku = "";
		qty = 0;
		tmmainQty = 0;
		delta = 0;
	}

	public double getQty() {
		return qty;
	}

	public void setQty(double qty) {
		this.qty = qty;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public double getTmmainQty() {
		return tmmainQty;
	}

	public void setTmmainQty(int tmmainQty) {
		this.tmmainQty = tmmainQty;
	}

	public double getDelta() {
		return delta;
	}

	public void setDelta(double delta) {
		this.delta = delta;
	}
	
}
