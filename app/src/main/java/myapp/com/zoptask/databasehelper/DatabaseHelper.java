package myapp.com.zoptask.databasehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import myapp.com.zoptask.config.Config;
import myapp.com.zoptask.databasehelper.table.DbTable;
import myapp.com.zoptask.model.Product;
import myapp.com.zoptask.model.ProductCart;
import myapp.com.zoptask.model.UserDetail;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper sInstance;

    public static synchronized DatabaseHelper getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    private DatabaseHelper(Context context) {
        super(context, Config.DATABASE_NAME, null, Config.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(" CREATE TABLE IF NOT EXISTS " + DbTable.UserTable.TABLE_NAME + " ("
                + DbTable.UserTable.u_id + " INTEGER PRIMARY KEY autoincrement, "
                + DbTable.UserTable.username + " TEXT , "
                + DbTable.UserTable.password + " TEXT);");

        sqLiteDatabase.execSQL(" CREATE TABLE IF NOT EXISTS " + DbTable.ProductTable.TABLE_NAME + " ("
                + DbTable.ProductTable.product_id + " INTEGER, "
                + DbTable.ProductTable.product_name + " TEXT , "
                + DbTable.ProductTable.product_item_count + " INTEGER , "
                + DbTable.ProductTable.product_price + " TEXT , "
                + DbTable.ProductTable.user_id + " INTEGER);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * Insert User deatils to user table while register
     *
     * @param username
     * @param password
     * @param database
     * @return
     */
    public long insertUserDetails(String username, String password, SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbTable.UserTable.username, username);
        contentValues.put(DbTable.UserTable.password, password);
        long id = database.insert(DbTable.UserTable.TABLE_NAME, null, contentValues);
        return id;
    }

    /**
     * get user details from userdetails table
     *
     * @param username
     * @param password
     * @param sqLiteDatabase
     * @return
     */
    public UserDetail getUserDetails(String username, String password, SQLiteDatabase sqLiteDatabase) {
        UserDetail userDetail = null;
        String[] columns = {
                DbTable.UserTable.u_id,
                DbTable.UserTable.username,
                DbTable.UserTable.password
        };

        String selection = DbTable.UserTable.username + " = ? and " + DbTable.UserTable.password + " = ?";
        String[] selection_args = {username, password};

        Cursor cursor = sqLiteDatabase.query(DbTable.UserTable.TABLE_NAME, columns, selection, selection_args, null, null, null, null);
        boolean exists = (cursor.getCount() > 0);

        if (exists) {
            if (cursor.moveToFirst()) {
                do {
                    int uid = cursor.getInt(cursor.getColumnIndex(DbTable.UserTable.u_id));
                    String user_name = cursor.getString(cursor.getColumnIndex(DbTable.UserTable.username));
                    String user_password = cursor.getString(cursor.getColumnIndex(DbTable.UserTable.password));

                    userDetail = new UserDetail(uid, user_name, user_password);

                    return userDetail;

                } while (cursor.moveToNext());
            }
        } else {
            return userDetail;
        }
        return userDetail;
    }

    /**
     * @param productId
     * @param productName
     * @param productPrice
     * @param productItemCount
     * @param userId
     * @param sqLiteDatabase
     * @return
     */
    public void insertProductToCartTable(int productId, String productName, String productPrice, int productItemCount,
                                         int userId,SQLiteDatabase sqLiteDatabase) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DbTable.ProductTable.product_id, productId);
            contentValues.put(DbTable.ProductTable.product_name, productName);
            contentValues.put(DbTable.ProductTable.product_price, productPrice);
            contentValues.put(DbTable.ProductTable.product_item_count, productItemCount);
            contentValues.put(DbTable.ProductTable.user_id, userId);
            String selection = DbTable.ProductTable.product_id + " = ? and "+DbTable.ProductTable.user_id+" = ?";
            String[] selection_args = {String.valueOf(productId),String.valueOf(userId)};
            int row = sqLiteDatabase.update(DbTable.ProductTable.TABLE_NAME, contentValues, selection, selection_args);
            if (row < 1) {
                contentValues.put(DbTable.ProductTable.product_id, productId);
                sqLiteDatabase.insert(DbTable.ProductTable.TABLE_NAME, null, contentValues);
                Log.d("Inserted", "Success");
            }
            Log.d("Updated", "Success_" + row);
        } catch (Exception e) {
            Log.d("Insert Error", e.toString());
        }

    }

    /**
     * get product cart items details
     *
     * @param user_id
     * @param sqLiteDatabase
     * @return
     */
    public List<ProductCart> getProductCartDetails(int user_id, SQLiteDatabase sqLiteDatabase) {
        List<ProductCart> productCartList = new ArrayList<>();
        String[] columns = {
                DbTable.ProductTable.product_id,
                DbTable.ProductTable.product_name,
                DbTable.ProductTable.product_price,
                DbTable.ProductTable.product_item_count,
                DbTable.ProductTable.user_id
        };

        String selection = DbTable.ProductTable.user_id + " = ? and "+DbTable.ProductTable.product_item_count+" >0";
        String[] selection_args = {String.valueOf(user_id)};

        Cursor cursor = sqLiteDatabase.query(DbTable.ProductTable.TABLE_NAME, columns, selection, selection_args, null, null, null, null);
        boolean exists = (cursor.getCount() > 0);

        if(exists){
            if (cursor.moveToFirst()) {
                do {
                    int product_id = cursor.getInt(cursor.getColumnIndex(DbTable.ProductTable.product_id));
                    String product_name = cursor.getString(cursor.getColumnIndex(DbTable.ProductTable.product_name));
                    String product_price = cursor.getString(cursor.getColumnIndex(DbTable.ProductTable.product_price));
                    int product_item_count = cursor.getInt(cursor.getColumnIndex(DbTable.ProductTable.product_item_count));
                    int userId = cursor.getInt(cursor.getColumnIndex(DbTable.ProductTable.user_id));

                    ProductCart productCart=new ProductCart(product_id,product_name,product_price,product_item_count,userId);

                    productCartList.add(productCart);

                } while (cursor.moveToNext());
            }
        }
        return productCartList;
    }


    /**
     * get product item count by product id and username
     * @param product_id
     * @param user_id
     * @param sqLiteDatabase
     * @return
     */
    public int getItemCountByProductId(int product_id,int user_id,SQLiteDatabase sqLiteDatabase){
        int productItemCount=0;
        String[] columns = {
                DbTable.ProductTable.product_item_count
        };

        String selection = DbTable.ProductTable.product_id + " = ? and "+DbTable.ProductTable.user_id+" = ?";
        String[] selection_args = {String.valueOf(product_id),String.valueOf(user_id)};

        Cursor cursor = sqLiteDatabase.query(DbTable.ProductTable.TABLE_NAME, columns, selection, selection_args, null, null, null, null);
        boolean exists = (cursor.getCount() > 0);

        if(exists){
            if (cursor.moveToFirst()) {
                do {
                    productItemCount = cursor.getInt(cursor.getColumnIndex(DbTable.ProductTable.product_item_count));
                } while (cursor.moveToNext());
            }
        }
        return productItemCount;
    }

    /**
     * get cart count by user id
     * @param user_id
     * @param sqLiteDatabase
     * @return
     */
    public int getCartCount(int user_id,SQLiteDatabase sqLiteDatabase){
        String[] columns = {
                DbTable.ProductTable.product_item_count
        };

        String selection = DbTable.ProductTable.user_id + " = ? and "+DbTable.ProductTable.product_item_count+" >0";
        String[] selection_args = {String.valueOf(user_id)};

        Cursor cursor = sqLiteDatabase.query(DbTable.ProductTable.TABLE_NAME, columns, selection, selection_args, null, null, null, null);

        return cursor.getCount();
    }

    public int getCartTotalValue(int user_id,SQLiteDatabase sqLiteDatabase){
        int totalValue=0;
        String[] columns = {
                DbTable.ProductTable.product_item_count,
                DbTable.ProductTable.product_price
        };

        String selection = DbTable.ProductTable.user_id + " = ? and "+DbTable.ProductTable.product_item_count+" >0";
        String[] selection_args = {String.valueOf(user_id)};

        Cursor cursor = sqLiteDatabase.query(DbTable.ProductTable.TABLE_NAME, columns, selection, selection_args, null, null, null, null);
        boolean exists = (cursor.getCount() > 0);

        if(exists){
            if (cursor.moveToFirst()) {
                do {
                    int productItemCount = cursor.getInt(cursor.getColumnIndex(DbTable.ProductTable.product_item_count));
                    String productPrice=cursor.getString(cursor.getColumnIndex(DbTable.ProductTable.product_price));
                    totalValue=totalValue+(productItemCount*Integer.parseInt(productPrice.replaceAll("\\.0*$", "")));
                } while (cursor.moveToNext());
            }
        }

        return totalValue;
    }
}
