package myapp.com.zoptask.model;

import com.google.gson.annotations.SerializedName;

public class ProductData {
    @SerializedName("title")
    private String title;
    @SerializedName("collection")
    private ProductCollections productColection;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ProductCollections getProductColection() {
        return productColection;
    }

    public void setProductColection(ProductCollections productColection) {
        this.productColection = productColection;
    }
}
