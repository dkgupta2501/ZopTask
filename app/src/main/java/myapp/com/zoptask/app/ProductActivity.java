package myapp.com.zoptask.app;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import myapp.com.zoptask.R;
import myapp.com.zoptask.adapter.ProductAdapter;
import myapp.com.zoptask.application.AppApplication;
import myapp.com.zoptask.config.Config;
import myapp.com.zoptask.databasehelper.DatabaseHelper;
import myapp.com.zoptask.model.Product;
import myapp.com.zoptask.model.ProductList;
import myapp.com.zoptask.mylibrary.APIHit;

public class ProductActivity extends AppCompatActivity implements ProductAdapter.ProductAdapterListener {
    private RecyclerView product_recycler_view;
    private AppCompatButton btnTotal;
    private List<ProductList> productList;
    private ProductAdapter adapter;
    private Toolbar toolbar;

    private TextView textCartItemCount;
    private int mCartItemCount = 0;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Product");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            // Do something for lollipop and above versions
            final Drawable upArrow = ContextCompat.getDrawable(getBaseContext(), R.drawable.abc_ic_ab_back_material);
            upArrow.setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.white), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            //set Status Bar color
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(ContextCompat.getColor(getBaseContext(), R.color.statusbarcolor));
        } else{
            // do something for phones running an SDK before lollipop
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        btnTotal=(AppCompatButton)findViewById(R.id.btnTotal);

        databaseHelper=DatabaseHelper.getInstance(getBaseContext());
        sqLiteDatabase=databaseHelper.getWritableDatabase();

        //get the total cart value and show to bottom
        totalCartValue();

        product_recycler_view = (RecyclerView) findViewById(R.id.product_recycler_view);
        productList = new ArrayList<>();
        adapter = new ProductAdapter(productList, this, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        product_recycler_view.setLayoutManager(mLayoutManager);
        product_recycler_view.setHasFixedSize(true);
        product_recycler_view.setItemAnimator(new DefaultItemAnimator());
        product_recycler_view.setAdapter(adapter);


        //get Data from server
        APIHit.hitURL(new APIHit.VolleyResponseCallBack() {
            @Override
            public void onSuccess(String response) {
                Product product = new Gson().fromJson(response.toString(), Product.class);
                List<ProductList> product_List = product.getProductLayoutList().get(1).getProductData().getProductColection().getProductListList();
                productList.clear();
                productList.addAll(product_List);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getBaseContext(),"Something went Wrong!",Toast.LENGTH_SHORT).show();
            }
        }, Config.PRODUCT_URL, ProductActivity.this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);

        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_cart: {
                // open cart Activity
                startActivity(new Intent(getBaseContext(),CartActivity.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //show the badge counter to the cart icon
    private void setupBadge() {
        mCartItemCount=databaseHelper.getCartCount(AppApplication.getInstance().getUser_id(),databaseHelper.getReadableDatabase());
        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onMinusClicked(ProductList productList) {
        if(productList.getProductItemCount()!=0) {
            productList.setProductItemCount(databaseHelper.getItemCountByProductId(productList.getId(), AppApplication.getInstance().getUser_id(), databaseHelper.getReadableDatabase()));
            databaseHelper.insertProductToCartTable(productList.getId(), productList.getName(), productList.getMrp(), productList.getProductItemCount() - 1,
                    AppApplication.getInstance().getUser_id(),  sqLiteDatabase);
            adapter.notifyDataSetChanged();

            //update the total cart to bottom
            totalCartValue();
            //decrement the badge view
            setupBadge();
        }else{
            Toast.makeText(getBaseContext(), "not performed action", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPlusClicked(ProductList productList) {
        productList.setProductItemCount(databaseHelper.getItemCountByProductId(productList.getId(),AppApplication.getInstance().getUser_id(),databaseHelper.getReadableDatabase()));
        databaseHelper.insertProductToCartTable(productList.getId(),productList.getName(),productList.getMrp(),productList.getProductItemCount()+1,
                AppApplication.getInstance().getUser_id(),sqLiteDatabase);
        adapter.notifyDataSetChanged();

        //update the total cart to bottom
        totalCartValue();
        //increment the badge view
        setupBadge();
    }

    //calculate the total cart value
    private void totalCartValue(){
        btnTotal.setText("Total Value: "+databaseHelper.getCartTotalValue(AppApplication.getInstance().getUser_id(),sqLiteDatabase)+"");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }
}
