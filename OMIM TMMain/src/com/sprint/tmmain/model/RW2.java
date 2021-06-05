package com.sprint.tmmain.model;

public class RW2 {
	
	private String sku;
	private String description;
	private double qty;
	private double rw2Qty;
	private double delta;
	
	public RW2() {
		sku = "";
		description = "";
		qty = 0;
		rw2Qty = 0;
		delta = 0;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getQty() {
		return qty;
	}

	public void setQty(double qty) {
		this.qty = qty;
	}

	public double getRw2Qty() {
		return rw2Qty;
	}

	public void setRw2Qty(double rw2Qty) {
		this.rw2Qty = rw2Qty;
	}

	public double getDelta() {
		return delta;
	}

	public void setDelta(double delta) {
		this.delta = delta;
	}
	
	
}
