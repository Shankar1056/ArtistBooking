package bigappcompany.com.artistbooking.adapters;

/**
 * Created by pratap.kesaboyina on 24-12-2014.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.util.ArrayList;

import bigappcompany.com.artistbooking.R;
import bigappcompany.com.artistbooking.models.ImageObj;
import bigappcompany.com.artistbooking.network.PicassoTrustAll;

public class PhotoPhAdapter extends RecyclerView.Adapter<PhotoPhAdapter.SingleItemRowHolder> {

    private ArrayList<ImageObj> itemsList;
    private Context mContext;

    public PhotoPhAdapter(Context context, ArrayList<ImageObj> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_photo, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, int i) {

        final ImageObj singleItem = itemsList.get(i);


        ViewTreeObserver vto = holder.itemImage.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int finalHeight, finalWidth;

                holder.itemImage.getViewTreeObserver().removeOnPreDrawListener(this);

                finalHeight = holder.itemImage.getMeasuredHeight();
                finalWidth = holder.itemImage.getMeasuredWidth();
                PicassoTrustAll.getInstance(holder.itemImage.getContext()).load(singleItem.getPhoto()).resize(finalWidth,finalHeight).centerCrop().into(holder.itemImage);

                return true;
            }
        });

             /* Glide.with(mContext)
                .load(feedItem.getImageURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.bg)
                .into(feedListRowHolder.thumbView);*/
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {



        protected ImageView itemImage;


        public SingleItemRowHolder(View view) {
            super(view);


            this.itemImage = (ImageView) view.findViewById(R.id.audioimg);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


        }

    }

}