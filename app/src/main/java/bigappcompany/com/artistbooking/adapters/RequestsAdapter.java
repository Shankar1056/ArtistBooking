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

import bigappcompany.com.artistbooking.R;
import bigappcompany.com.artistbooking.RippleView;
import bigappcompany.com.artistbooking.models.RequestsModel;


public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.ViewHolder> {
    private ArrayList<RequestsModel> models;
    int lastPosition;


    public RequestsAdapter(ArrayList<RequestsModel> models) {
        this.models = models;

    }

    public void add_event(RequestsModel model)
    {
        this.models.add(model);
        notifyItemInserted(models.size()-1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final RequestsModel model = this.models.get(position);


        holder.titleTV.setText(model.getEvent_name());
        holder.tv_addr.setText(model.getAddress());
        holder.tv_bkName.setText(model.getName());
        holder.tv_date.setText(model.getDate());
        holder.tv_contact.setText(model.getPhone());

        setAnimation(holder.itemView,position);
        //Picasso.with(holder.thumbnailTV.getContext()).load(model.getPhoto()).resize(holder.thumbnailTV.getMeasuredWidth(),holder.thumbnailTV.getMeasuredHeight()).centerCrop().into(holder.thumbnailTV);

    }

    private void remove(int position) {
        models.remove(position);
        this.notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public void addItem(RequestsModel requestsModel, int i) {
        models.add(i,requestsModel);
        notifyItemInserted(i);
    }


    class ViewHolder extends RecyclerView.ViewHolder implements RippleView.OnRippleCompleteListener {
        TextView titleTV, tv_bkName,tv_date,tv_contact,tv_addr;



        ViewHolder(View itemView) {
            super(itemView);


            titleTV = (TextView) itemView.findViewById(R.id.tvTitle);
            tv_bkName = (TextView) itemView.findViewById(R.id.tv_bkName);
            tv_addr = (TextView) itemView.findViewById(R.id.tv_addr);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_contact = (TextView) itemView.findViewById(R.id.tv_ph);




            //itemRV.setOnRippleCompleteListener(this);
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

