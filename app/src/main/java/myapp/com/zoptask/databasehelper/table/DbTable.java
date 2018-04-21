package myapp.com.zoptask.databasehelper.table;

public class DbTable {
    public static abstract class UserTable{
        public static final String u_id="_id";
        public static final String username="username";
        public static final String password="password";
        public static final String TABLE_NAME="usertable";
    }

    public static abstract class ProductTable{
        public static final String product_id="product_id";
        public static final String product_name="produt_name";
        public static final String product_item_count="product_item_count";
        public static final String product_price="product_price";
        public static final String user_id="user_id";
        public static final String TABLE_NAME="product_cart";
    }
}
