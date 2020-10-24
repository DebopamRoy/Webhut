package project.mapobed.webhut.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import project.mapobed.webhut.R;
import project.mapobed.webhut.modalclass.PopularWebsiteModalClass;

public class PopularNewsAdapter extends RecyclerView.Adapter<PopularNewsAdapter.PopularWebsiteHolder> {
    private final List<PopularWebsiteModalClass>list;
    private final pop_website_link_clicked pop_website_link_clicked;

    public PopularNewsAdapter(List<PopularWebsiteModalClass> list, PopularNewsAdapter.pop_website_link_clicked pop_website_link_clicked) {
        this.list = list;
        this.pop_website_link_clicked = pop_website_link_clicked;
    }

    @NonNull
    @Override
    public PopularWebsiteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PopularWebsiteHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_website_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PopularWebsiteHolder holder, int position) {
        holder.img.setImageResource(list.get(position).getWeb_img());
        holder.tv.setText(list.get(position).getWeb_name());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PopularWebsiteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img;
        TextView tv;
        public PopularWebsiteHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.pop_web_image);
            tv=itemView.findViewById(R.id.pop_web_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            pop_website_link_clicked.link_clicked_news(getAdapterPosition());
        }
    }
    public interface pop_website_link_clicked{
        void link_clicked_news(int name);
    }
}
