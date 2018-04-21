package myapp.com.zoptask.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductCollections {
    @SerializedName("count")
    private int count;
    @SerializedName("limit")
    private int limit;
    @SerializedName("offset")
    private int offset;
    @SerializedName("product")
    private List<ProductList> productListList;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public List<ProductList> getProductListList() {
        return productListList;
    }

    public void setProductListList(List<ProductList> productListList) {
        this.productListList = productListList;
    }
}
