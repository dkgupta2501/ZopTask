package myapp.com.zoptask.adapter;

import android.content.Context;
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
import myapp.com.zoptask.model.ProductCart;
import myapp.com.zoptask.mylibrary.CustomFontsLoader;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{

    private List<ProductCart> productCartList;
    private Context mCtx;

    public CartAdapter(List<ProductCart> productCartList,Context mCtx){
        this.productCartList=productCartList;
        this.mCtx=mCtx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_row_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ProductCart productList=productCartList.get(position);

        holder.productNameTv.setText(productList.getProductName());

        //check product item count not equal to zero
        if(productList.getProductItemCount()!=0) {
            holder.txtProductQuantity.setVisibility(View.VISIBLE);
            int total = productList.getProductItemCount() * Integer.parseInt(productList.getProductPrice().replaceAll("\\.0*$", ""));
            holder.txtProductQuantity.setText(productList.getProductItemCount() + " * " + productList.getProductPrice().replaceAll("\\.0*$", "") + " = " + total + "");
        }else{
            holder.txtProductQuantity.setVisibility(View.INVISIBLE);
        }

        //set product image (hardcoded image string url)
        Glide.with(mCtx).load("https://cdn.decorpad.com/photos/2016/05/10/white-canopy-bamboo-motif-bed.jpeg")
                .thumbnail(0.5f)
                .into(holder.productImgUrl);


    }

    @Override
    public int getItemCount() {
        return productCartList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtProductQuantity,productNameTv;
        private ImageView productImgUrl;

        public ViewHolder(View itemView) {
            super(itemView);
            txtProductQuantity=(TextView) itemView.findViewById(R.id.txtProductQuantity);
            productNameTv=(TextView) itemView.findViewById(R.id.productNameTv);
            productImgUrl=(ImageView)itemView.findViewById(R.id.productImgUrl);

            productNameTv.setTypeface(CustomFontsLoader.getTypeface(mCtx, CustomFontsLoader.ROBOTO_REGULAR));
            txtProductQuantity.setTypeface(CustomFontsLoader.getTypeface(mCtx, CustomFontsLoader.HELVETICALTSTD_LIGHT));

        }
    }

}
