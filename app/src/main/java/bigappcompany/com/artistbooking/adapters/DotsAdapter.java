package bigappcompany.com.artistbooking.adapters;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import bigappcompany.com.artistbooking.R;
import bigappcompany.com.artistbooking.RippleView;
import bigappcompany.com.artistbooking.models.RequestsModel;


public class DotsAdapter extends RecyclerView.Adapter<DotsAdapter.ViewHolder> {

    int selected=0;
    int count;
    int previous=0;

    public DotsAdapter(int count) {
       this.count=count;

    }

    public void setSelected(int selected) {
        this.previous=this.selected;
        this.selected = selected;
        notifyItemChanged(previous);
        notifyItemChanged(selected);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dot, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.e("inside","change");
        if(selected==position)
            holder.img_dot.setImageResource(R.drawable.round_rank_highlight);
        else
            holder.img_dot.setImageResource(android.R.color.transparent);


    }



    @Override
    public int getItemCount() {
        return count;
    }




    class ViewHolder extends RecyclerView.ViewHolder  {

    ImageView img_dot;

        ViewHolder(View itemView) {
            super(itemView);

            img_dot=(ImageView) itemView.findViewById(R.id.img_dot);


        }




    }

}

