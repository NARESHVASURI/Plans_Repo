package com.example.plans.dto;

public class PlanResponse {

	private String id;
	private String name;
	private String type;
	private String data;
	private Double amount;
	private Integer validity;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public Integer getValidity() { return validity; }
    public void setValidity(Integer validity) { this.validity = validity; }
}
