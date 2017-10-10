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
import android.widget.TextView;

import java.util.ArrayList;

import bigappcompany.com.artistbooking.R;
import bigappcompany.com.artistbooking.RippleView;
import bigappcompany.com.artistbooking.models.ImageTypeModel;
import bigappcompany.com.artistbooking.models.VideoObj;
import bigappcompany.com.artistbooking.network.PicassoTrustAll;


public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private ArrayList<VideoObj> models;


    public NotificationsAdapter(ArrayList<VideoObj> models) {
        this.models = models;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);

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


                //Log.e("height,width",finalHeight+","+finalWidth);
                Log.e("Id",model.getId());
                //Picasso.with(holder.thumbnailTV.getContext()).load(model.getPhoto()).resize(Math.max(finalHeight,finalWidth),Math.max(finalHeight,finalWidth)).centerCrop().into(holder.thumbnailTV);
                PicassoTrustAll.getInstance(holder.thumbnailTV.getContext()).load(model.getPhoto()).resize(finalWidth,finalHeight).centerCrop().into(holder.thumbnailTV);
                return true;
            }
        });


        holder.tv_title.setText(model.getTitle());
        holder.tv_notify.setText(model.getVideoId());
        holder.tv_catg.setText(model.getId());
        setAnimation(holder.itemView,position);

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


        TextView tv_title,tv_catg,tv_notify;

        ViewHolder(View itemView) {
            super(itemView);

            thumbnailTV = (ImageView) itemView.findViewById(R.id.audioimg);
            tv_title = (TextView) itemView.findViewById(R.id.tv_name);
            tv_catg = (TextView) itemView.findViewById(R.id.tv_catg);
            tv_notify = (TextView) itemView.findViewById(R.id.tv_notify);

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
        void onVideo(VideoObj model, int pos);
    }
}

