package bigappcompany.com.artistbooking.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import bigappcompany.com.artistbooking.MainActivity;
import bigappcompany.com.artistbooking.R;
import bigappcompany.com.artistbooking.RippleView;
import bigappcompany.com.artistbooking.models.Constants;
import bigappcompany.com.artistbooking.models.ImageObj;
import bigappcompany.com.artistbooking.models.ImageTypeModel;


public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {
    private ArrayList<ImageObj> models;


    public ArtistAdapter(ArrayList<ImageObj> models) {
        this.models = models;

    }
    Activity activity;
    public void setReference(Activity activity){this.activity=activity;}
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist, parent, false);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        itemView.getLayoutParams().height=height*3/8;
        return new ViewHolder(itemView);
    }
    int lastPosition;
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ImageObj model = this.models.get(position);

        holder.tv.setText(model.getTitle());
        holder.tv_des.setText(model.getDes());
        ViewTreeObserver vto = holder.thumbnailTV.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                int finalHeight, finalWidth;

                holder.thumbnailTV.getViewTreeObserver().removeOnPreDrawListener(this);

                finalHeight = holder.thumbnailTV.getMeasuredHeight();
                finalWidth = holder.thumbnailTV.getMeasuredWidth();


                Log.e("height,width",finalHeight+","+finalWidth);
                Picasso.with(holder.thumbnailTV.getContext()).load(model.getPhoto()).resize(finalWidth,finalHeight).centerCrop().into(holder.thumbnailTV);
                //PicassoTrustAll.getInstance(holder.thumbnailTV.getContext()).load(model.getPhoto()).resize(finalWidth,finalWidth).centerCrop().into(holder.thumbnailTV);
                return true;
            }
        });
        holder.itemRV.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                rippleView.getContext().startActivity(new Intent(rippleView.getContext(), MainActivity.class).putExtra(Constants.ARTIST,model));
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

    public interface OnMusicItemListener {
        void onMusicItemClick(ImageTypeModel model, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements RippleView.OnRippleCompleteListener {
        ImageView thumbnailTV;

        RippleView itemRV;
        TextView tv,tv_des;

        ViewHolder(View itemView) {
            super(itemView);

            thumbnailTV = (ImageView) itemView.findViewById(R.id.itemImage);
            itemRV = (RippleView) itemView.findViewById(R.id.rp_item);
            tv=(TextView)itemView.findViewById(R.id.tvArtist);
            tv_des=(TextView)itemView.findViewById(R.id.tvDesig);


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
}

