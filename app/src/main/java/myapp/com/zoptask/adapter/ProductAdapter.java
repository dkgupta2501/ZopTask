package myapp.com.zoptask.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import myapp.com.zoptask.R;
import myapp.com.zoptask.application.AppApplication;
import myapp.com.zoptask.databasehelper.DatabaseHelper;
import myapp.com.zoptask.model.ProductList;
import myapp.com.zoptask.mylibrary.CustomFontsLoader;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{
    private List<ProductList> productLists;
    private Context mCtx;
    private ProductAdapterListener listener;
    private DatabaseHelper databaseHelper;
    String imgUrl = "https://api.androidhive.info/images/glide/medium/deadpool.jpg";

    public ProductAdapter(List<ProductList> productLists,Context mCtx,ProductAdapterListener listener){
        this.productLists=productLists;
        this.mCtx=mCtx;
        this.listener=listener;
        databaseHelper=DatabaseHelper.getInstance(mCtx);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_cart_row_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ProductList productList=productLists.get(position);

        holder.txtProductName.setText(productList.getName());
        holder.txtProductPrice.setText(mCtx.getResources().getString(R.string.Rs)+" "+productList.getMrp().replaceAll("\\.0*$", ""));
        holder.txtProductPriceDiscount.setText(mCtx.getResources().getString(R.string.Rs)+" "+productList.getDiscount().replaceAll("\\.0*$", ""));
        holder.txtProductPriceDiscount.setPaintFlags(holder.txtProductPriceDiscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        productList.setProductItemCount(databaseHelper.getItemCountByProductId(productList.getId(), AppApplication.getInstance().getUser_id(),databaseHelper.getReadableDatabase()));
        holder.productInc.setText(productList.getProductItemCount()+"");

        //check product item count not equal to zero
        if(productList.getProductItemCount()!=0) {
            holder.txtProductQuantity.setVisibility(View.VISIBLE);
            int total = productList.getProductItemCount() * Integer.parseInt(productList.getMrp().replaceAll("\\.0*$", ""));
            holder.txtProductQuantity.setText(productList.getProductItemCount() +" " +mCtx.getResources().getString(R.string.multiplication) + " "+productList.getMrp().replaceAll("\\.0*$", "") + " = " + total + "");
        }else{
            holder.txtProductQuantity.setVisibility(View.INVISIBLE);
        }

        //set product image (hardcoded image string url)
        Glide.with(mCtx).load("https://cdn.decorpad.com/photos/2016/05/10/white-canopy-bamboo-motif-bed.jpeg")
                .thumbnail(0.5f)
                .into(holder.product_thumb);

    }

    @Override
    public int getItemCount() {
        return productLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtProductName,productInc,txtProductPrice,txtProductQuantity,txtProductPriceDiscount;
        private ImageView imgMinus,imgPlus,product_thumb;

        public ViewHolder(View itemView) {
            super(itemView);
            txtProductName=(TextView)itemView.findViewById(R.id.txtProductName);
            imgMinus=(ImageView)itemView.findViewById(R.id.imgMinus);
            imgPlus=(ImageView)itemView.findViewById(R.id.imgPlus);
            product_thumb=(ImageView)itemView.findViewById(R.id.product_thumb);
            productInc=(TextView) itemView.findViewById(R.id.productInc);
            txtProductPrice=(TextView) itemView.findViewById(R.id.txtProductPrice);
            txtProductQuantity=(TextView) itemView.findViewById(R.id.txtProductQuantity);
            txtProductPriceDiscount=(TextView) itemView.findViewById(R.id.txtProductPriceDiscount);

            txtProductName.setTypeface(CustomFontsLoader.getTypeface(mCtx, CustomFontsLoader.ROBOTO_REGULAR));
            txtProductPrice.setTypeface(CustomFontsLoader.getTypeface(mCtx, CustomFontsLoader.HELVETICALTSTD_LIGHT));
            productInc.setTypeface(CustomFontsLoader.getTypeface(mCtx, CustomFontsLoader.HELVETICALTSTD_LIGHT));
            txtProductQuantity.setTypeface(CustomFontsLoader.getTypeface(mCtx, CustomFontsLoader.HELVETICALTSTD_LIGHT));
            txtProductPriceDiscount.setTypeface(CustomFontsLoader.getTypeface(mCtx, CustomFontsLoader.HELVETICALTSTD_LIGHT));

            imgMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onMinusClicked(productLists.get(getAdapterPosition()));
                }
            });

            imgPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPlusClicked(productLists.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface ProductAdapterListener {
        void onMinusClicked(ProductList productList);
        void onPlusClicked(ProductList productList);
    }

}
