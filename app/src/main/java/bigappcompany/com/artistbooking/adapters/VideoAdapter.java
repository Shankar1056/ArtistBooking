package bigappcompany.com.artistbooking.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.ArrayList;


import bigappcompany.com.artistbooking.R;
import bigappcompany.com.artistbooking.RippleView;
import bigappcompany.com.artistbooking.models.ImageTypeModel;
import bigappcompany.com.artistbooking.models.VideoObj;
import bigappcompany.com.artistbooking.network.PicassoTrustAll;


public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private final OnVideoItemListener listener;
    private ArrayList<VideoObj> models;


    public VideoAdapter(ArrayList<VideoObj> models,OnVideoItemListener listener) {
        this.models = models;
        this.listener=listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);

        return new ViewHolder(itemView);
    }
    int lastPosition;
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final VideoObj model = this.models.get(position);


        ViewTreeObserver vto = holder.thumbnailTV.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                int finalHeight, finalWidth;

                holder.thumbnailTV.getViewTreeObserver().removeOnPreDrawListener(this);

                finalHeight = holder.thumbnailTV.getMeasuredHeight();
                finalWidth = holder.thumbnailTV.getMeasuredWidth();


                Log.e("height,width",finalHeight+","+finalWidth);
                //Picasso.with(holder.thumbnailTV.getContext()).load(model.getPhoto()).resize(Math.max(finalHeight,finalWidth),Math.max(finalHeight,finalWidth)).centerCrop().into(holder.thumbnailTV);
                PicassoTrustAll.getInstance(holder.thumbnailTV.getContext()).load(model.getPhoto()).resize(finalWidth,finalHeight).centerCrop().into(holder.thumbnailTV);
                return true;
            }
        });
        holder.itemRV.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                listener.onVideo(position);
            }
        });
        /*holder.thumbnailTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //v.getContext().startActivity(new Intent(v.getContext(), Artists.class).putExtra("model",models).putExtra("pos",position));
            }
        });*/
        setAnimation(holder.itemView,position);
        //Picasso.with(holder.thumbnailTV.getContext()).load(model.getPhoto()).resize(holder.thumbnailTV.getMeasuredWidth(),holder.thumbnailTV.getMeasuredHeight()).centerCrop().into(holder.thumbnailTV);

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public void addItem(VideoObj obj) {
        models.add(obj);
        notifyItemInserted(models.size()-1);
    }



    public interface OnMusicItemListener {
        void onMusicItemClick(ImageTypeModel model, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements RippleView.OnRippleCompleteListener {
        ImageView thumbnailTV;

        RippleView itemRV;

        ViewHolder(View itemView) {
            super(itemView);

            thumbnailTV = (ImageView) itemView.findViewById(R.id.audioimg);
            itemRV = (RippleView) itemView.findViewById(R.id.rp_row);
            itemRV.setOnRippleCompleteListener(this);
        }



        @Override
        public void onComplete(RippleView rippleView) {

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

    public interface OnVideoItemListener {
        void onVideo(int pos);
    }
}

