package myapp.com.zoptask.model;

public class ProductCart {
    private int productId;
    private String productName;
    private String productPrice;
    private int productItemCount;
    private int userId;


    public ProductCart(int productId, String productName, String productPrice, int productItemCount, int userId) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productItemCount = productItemCount;
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductItemCount() {
        return productItemCount;
    }

    public void setProductItemCount(int productItemCount) {
        this.productItemCount = productItemCount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
