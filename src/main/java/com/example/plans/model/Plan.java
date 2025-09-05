package com.example.plans.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Document(collection = "plans")
public class Plan {

    @Id
    private String id;

    @NotBlank(message = "Name is mandatory")
    @Size(max = 100, message = "Name must be at most 100 characters")
    private String name;

    @NotBlank(message = "Type is mandatory")
    @Pattern(regexp = "^(prepaid|postpaid)$", message = "Type must be either 'prepaid' or 'postpaid'")
    private String type;

    @NotBlank(message = "Data is mandatory")
    @Size(max = 50, message = "Data must be at most 50 characters")
    private String data;

    @NotNull(message = "Amount is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    private Double amount;

    @NotNull(message = "Validity is mandatory")
    @Min(value = 1, message = "Validity must be at least 1")
    @Max(value = 365, message = "Validity must not exceed 365")
    private Integer validity;



    public Plan() {}

    public Plan(String id, String name, String type, String data, Double amount, Integer validity) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.data = data;
        this.amount = amount;
        this.validity = validity;
    }

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
