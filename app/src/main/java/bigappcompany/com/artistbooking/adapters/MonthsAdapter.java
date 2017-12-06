package bigappcompany.com.artistbooking.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;


import bigappcompany.com.artistbooking.BookingActivity;
import bigappcompany.com.artistbooking.R;
import bigappcompany.com.artistbooking.models.ImageTypeModel;


public class MonthsAdapter extends RecyclerView.Adapter<MonthsAdapter.ViewHolder> {
    private ArrayList<String> models;


    public MonthsAdapter(ArrayList<String> models) {
        this.models = models;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_month, parent, false);

        return new ViewHolder(itemView);
    }
    int lastPosition;
    int selected=-1;
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String model = this.models.get(position);
        holder.txt.setText(model);
        if(position==selected)
        {
            holder.txt.setBackgroundResource(R.drawable.holo_circle);
        }
        else
        {
            holder.txt.setBackgroundResource(0);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position!=selected) {
                    holder.txt.setBackgroundResource(R.drawable.holo_circle);
                    notifyItemChanged(selected);
                    selected=position;
                    try {
                        ((BookingActivity) holder.txt.getContext()).setSelected(selected);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
        setAnimation(holder.itemView,position);
        //Picasso.with(holder.thumbnailTV.getContext()).load(model.getPhoto()).resize(holder.thumbnailTV.getMeasuredWidth(),holder.thumbnailTV.getMeasuredHeight()).centerCrop().into(holder.thumbnailTV);

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public interface OnMusicItemListener {
        void onMusicItemClick(ImageTypeModel model, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder  {
        TextView txt;

        ViewHolder(View itemView) {
            super(itemView);
            txt=(TextView)itemView.findViewById(R.id.txt_date);
        }



    }
    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.slidefromdown);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public void setSelected(int selected) {

        this.selected = selected;
        notifyDataSetChanged();
    }
}

