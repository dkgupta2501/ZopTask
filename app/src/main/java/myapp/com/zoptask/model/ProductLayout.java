package myapp.com.zoptask.model;

import com.google.gson.annotations.SerializedName;

public class ProductLayout {
    @SerializedName("name")
    private String name;
    @SerializedName("data")
    private ProductData productData;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductData getProductData() {
        return productData;
    }

    public void setProductData(ProductData productData) {
        this.productData = productData;
    }
}
