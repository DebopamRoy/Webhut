package project.mapobed.webhut.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackHolder> {
    @NonNull
    @Override
    public FeedbackHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class FeedbackHolder extends RecyclerView.ViewHolder {
        public FeedbackHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
