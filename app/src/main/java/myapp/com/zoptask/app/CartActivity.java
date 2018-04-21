package myapp.com.zoptask.app;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import myapp.com.zoptask.R;
import myapp.com.zoptask.adapter.CartAdapter;
import myapp.com.zoptask.application.AppApplication;
import myapp.com.zoptask.databasehelper.DatabaseHelper;
import myapp.com.zoptask.model.ProductCart;

public class CartActivity extends AppCompatActivity {
    private RecyclerView cart_recycler_view;
    private Toolbar cartToolbar;
    private CartAdapter adapter;
    private List<ProductCart> productCartList;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;
    private TextView tvUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartToolbar = (Toolbar) findViewById(R.id.cartToolbar);
        cartToolbar.setTitle("Cart Details");
        cartToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(cartToolbar);

        //database instance
        databaseHelper=DatabaseHelper.getInstance(getBaseContext());
        sqLiteDatabase=databaseHelper.getReadableDatabase();

        cart_recycler_view=(RecyclerView)findViewById(R.id.cart_recycler_view);
        tvUserName=(TextView)findViewById(R.id.tvUserName);

        tvUserName.setText(AppApplication.getInstance().getUserName());

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

        productCartList = new ArrayList<>();
        productCartList=databaseHelper.getProductCartDetails(AppApplication.getInstance().getUser_id(),sqLiteDatabase);
        adapter = new CartAdapter(productCartList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        cart_recycler_view.setLayoutManager(mLayoutManager);
        cart_recycler_view.setHasFixedSize(true);
        cart_recycler_view.setItemAnimator(new DefaultItemAnimator());
        cart_recycler_view.setAdapter(adapter);

        cartToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }
}
