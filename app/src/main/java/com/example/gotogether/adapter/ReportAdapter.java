package com.example.gotogether.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gotogether.FirebaseHelper2;
import com.example.gotogether.R;
import com.example.gotogether.ReportInfo;
import com.example.gotogether.activity.Post2Activity;
import com.example.gotogether.activity.WriteReportActivity;
import com.example.gotogether.listener.OnPostListener;
import com.example.gotogether.view.ReadContentsView2;

import java.util.ArrayList;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MainViewHolder> {
    private ArrayList<ReportInfo> mDataset;
    private Activity activity;
    private FirebaseHelper2 firebaseHelper2;
    private final int MORE_INDEX = 2;

    static class MainViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        MainViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public ReportAdapter(Activity activity, ArrayList<ReportInfo> myDataset) {
        this.mDataset = myDataset;
        this.activity = activity;

        firebaseHelper2 = new FirebaseHelper2(activity);
    }

    public void setOnPostListener(OnPostListener onPostListener){
        firebaseHelper2.setOnPostListener(onPostListener);
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }

    @NonNull
    @Override
    public ReportAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Post2Activity.class);
                intent.putExtra("reportInfo", mDataset.get(mainViewHolder.getAdapterPosition()));
                activity.startActivity(intent);
            }
        });

        cardView.findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v, mainViewHolder.getAdapterPosition());
            }
        });

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        TextView titleTextView = cardView.findViewById(R.id.titleTextView);

        ReportInfo reportInfo = mDataset.get(position);
        titleTextView.setText(reportInfo.getTitle());

        ReadContentsView2 readContentsView2 = cardView.findViewById(R.id.ReadContentsView);
        LinearLayout contentsLayout = cardView.findViewById(R.id.contentsLayout);

        if (contentsLayout.getTag() == null || !contentsLayout.getTag().equals(reportInfo)) {
            contentsLayout.setTag(reportInfo);
            contentsLayout.removeAllViews();

            readContentsView2.setMoreIndex(MORE_INDEX);
            readContentsView2.setReportInfo(reportInfo);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private void showPopup(View v, final int position) {
        PopupMenu popup = new PopupMenu(activity, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.modify:
                        myStartActivity(WriteReportActivity.class, mDataset.get(position));
                        return true;
                    case R.id.delete:
                        firebaseHelper2.storageDelete(mDataset.get(position));
                        return true;
                    default:
                        return false;
                }
            }
        });

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.post, popup.getMenu());
        popup.show();
    }

    private void myStartActivity(Class c, ReportInfo reportInfo) {
        Intent intent = new Intent(activity, c);
        intent.putExtra("reportInfo", reportInfo);
        activity.startActivity(intent);
    }
}

