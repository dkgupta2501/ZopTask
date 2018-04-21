package myapp.com.zoptask.model;

import com.google.gson.annotations.SerializedName;

public class ProductList {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("slug")
    private String slug;
    @SerializedName("description")
    private String description;
    @SerializedName("mrp")
    private String mrp;
    @SerializedName("discount")
    private String discount;

    private int productItemCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public int getProductItemCount() {
        return productItemCount;
    }

    public void setProductItemCount(int productItemCount) {
        this.productItemCount = productItemCount;
    }

}
