package myapp.com.zoptask.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Product {
    @SerializedName("layouts")
    private List<ProductLayout> productLayoutList;

    public List<ProductLayout> getProductLayoutList() {
        return productLayoutList;
    }

    public void setProductLayoutList(List<ProductLayout> productLayoutList) {
        this.productLayoutList = productLayoutList;
    }
}
