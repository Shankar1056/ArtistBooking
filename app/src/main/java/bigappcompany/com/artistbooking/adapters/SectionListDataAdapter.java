package bigappcompany.com.artistbooking.adapters;

/**
 * Created by pratap.kesaboyina on 24-12-2014.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import bigappcompany.com.artistbooking.ArtistsActivity;
import bigappcompany.com.artistbooking.R;
import bigappcompany.com.artistbooking.models.SingleItemModel;
import bigappcompany.com.artistbooking.network.JsonParser;
import bigappcompany.com.artistbooking.network.PicassoTrustAll;

public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {

    private ArrayList<SingleItemModel> itemsList;
    private Context mContext;

    public SectionListDataAdapter(Context context, ArrayList<SingleItemModel> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single_card, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, int i) {

        final SingleItemModel singleItem = itemsList.get(i);

        holder.tvTitle.setText(singleItem.getName());

        ViewTreeObserver vto = holder.itemImage.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int finalHeight, finalWidth;

                holder.itemImage.getViewTreeObserver().removeOnPreDrawListener(this);

                finalHeight = holder.itemImage.getMeasuredHeight();
                finalWidth = holder.itemImage.getMeasuredWidth();
                PicassoTrustAll.getInstance(holder.itemImage.getContext()).load(singleItem.getUrl()).resize(finalWidth,finalHeight).centerCrop().into(holder.itemImage);
                //Log.e("image",singleItem.getUrl());
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

        protected TextView tvTitle;

        protected ImageView itemImage;


        public SingleItemRowHolder(View view) {
            super(view);

            this.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    v.getContext().startActivity(new Intent(v.getContext(), ArtistsActivity.class).putExtra(JsonParser.SUB_NAME,tvTitle.getText()).putExtra(JsonParser.SUB_CAT_ID,itemsList.get(getAdapterPosition()).getId()));
                    //Toast.makeText(v.getContext(), itemsList.get(getAdapterPosition()).getId(), Toast.LENGTH_SHORT).show();

                }
            });


        }

    }

}