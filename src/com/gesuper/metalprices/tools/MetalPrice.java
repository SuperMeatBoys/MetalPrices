package com.gesuper.metalprices.tools;

public class MetalPrice {
	
	private String name;
	
	private float bankBuy;
	private float bankSale;
	private float highest;
	private float lowest;
	
	public MetalPrice(String _name){
		this.name = _name;
		this.updatePrices(0, 0, 0, 0);
	}
	
	public MetalPrice(String _name, float bank_buy, float bank_sale, float _highest, float _lowest){
		this.name = _name;
		this.updatePrices(bank_buy, bank_sale, _highest, _lowest);
	}
	
	public MetalPrice(String _name, float[] prices){
		this.name = _name;
		this.updatePrices(prices);
	}
	
	public void updatePrices(float bank_buy, float bank_sale, float _highest, float _lowest){
		this.bankBuy = bank_buy;
		this.bankSale = bank_sale;
		this.highest = _highest;
		this.lowest = _lowest;
	}
	
	public void updatePrices(float[] prices){
		this.bankBuy = prices[0];
		this.bankSale = prices[1];
		this.highest = prices[2];
		this.highest = prices[3];
	}
	
	public void setName(String _name){
		this.name = _name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getPrice(){
		return "" + this.bankBuy + "/" + this.bankSale;
	}
	
	public void setPriceOfBankBuy(float p){
		this.bankBuy = p;
	}
	
	public float getPriceOfBankBuy(){
		return this.bankBuy;
	}
	
	public void setPriceOfBankSale(float p){
		this.bankSale = p;
	}
	
	public float getPriceOfBankSale(){
		return this.bankSale;
	}
	
	public void setPriceOfHighest(float p){
		this.highest = p;
	}
	
	public float getPriceOfHighest(){
		return this.highest;
	}
	
	public void setPriceOfLowest(float p){
		this.lowest = p;
	}
	
	public float getPriceOfLowest(){
		return this.lowest;
	}
}
