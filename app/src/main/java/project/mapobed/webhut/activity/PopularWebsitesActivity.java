package project.mapobed.webhut.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import project.mapobed.webhut.R;
import project.mapobed.webhut.adapter.PopularClassifiedAdapter;
import project.mapobed.webhut.adapter.PopularDoubtAdapter;
import project.mapobed.webhut.adapter.PopularEngineAdapter;
import project.mapobed.webhut.adapter.PopularEntertainmentAdapter;
import project.mapobed.webhut.adapter.PopularNewsAdapter;
import project.mapobed.webhut.adapter.PopularShoppingAdapter;
import project.mapobed.webhut.adapter.PopularSocialAdapter;
import project.mapobed.webhut.modalclass.PopularWebsiteModalClass;

public class PopularWebsitesActivity extends AppCompatActivity implements PopularNewsAdapter.pop_website_link_clicked,PopularClassifiedAdapter.pop_website_link_clicked,PopularEngineAdapter.pop_website_link_clicked,PopularSocialAdapter.pop_website_link_clicked,PopularShoppingAdapter.pop_website_link_clicked,PopularDoubtAdapter.pop_website_link_clicked, PopularEntertainmentAdapter.pop_website_link_clicked {
    private RecyclerView popular_social, popular_doubt, popular_entertainment, popular_shopping, popular_engine, popular_news, popular_classified;
    private List<PopularWebsiteModalClass> list_social,list_entertainment,list_doubts,list_shopping,list_engine,list_news,list_classified;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_websites);

        inflateItems();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        popularSocial();

        popularEntertainment();

        popularDoubts();

        popularShopping();

        popularSearchEngines();

        popularNews();

        popularclassified();


    }

    private void popularclassified() {
        list_classified = new ArrayList<>();

        list_classified.add(new PopularWebsiteModalClass(getString(R.string.linkedin), "https://www.linkedin.com/", R.drawable.ic_linkedin));
        list_classified.add(new PopularWebsiteModalClass(getString(R.string.internshala), "https://internshala.com/", R.drawable.internshala));
        list_classified.add(new PopularWebsiteModalClass(getString(R.string.gfg), "https://www.geeksforgeeks.org/", R.drawable.geeksforgeeks));
        list_classified.add(new PopularWebsiteModalClass(getString(R.string.codechef), "https://www.codechef.com/", R.drawable.ic_codechef));
        list_classified.add(new PopularWebsiteModalClass(getString(R.string.github), "https://github.com/", R.drawable.ic_github));
        list_classified.add(new PopularWebsiteModalClass(getString(R.string.bitbucket), "https://bitbucket.org/", R.drawable.ic_bitbucket));

        popular_classified.setAdapter(new PopularClassifiedAdapter(list_classified, PopularWebsitesActivity.this));
        popular_classified.setLayoutManager(new GridLayoutManager(this, 4));
        popular_classified.setHasFixedSize(true);
        popular_classified.setItemAnimator(new DefaultItemAnimator());
        popular_classified.setLayoutFrozen(true);
    }

    private void popularNews() {
        list_news = new ArrayList<>();
        list_news.add(new PopularWebsiteModalClass(getString(R.string.aajtak), "https://www.aajtak.in/", R.drawable.ic_aajtak));
        list_news.add(new PopularWebsiteModalClass(getString(R.string.ndtv), "https://www.ndtv.com/", R.drawable.ic_ndtv));
        list_news.add(new PopularWebsiteModalClass(getString(R.string.times_of_india), "https://timesofindia.indiatimes.com/", R.drawable.ic_toi));
        list_news.add(new PopularWebsiteModalClass(getString(R.string.the_hindu), "https://www.thehindu.com/", R.drawable.ic_hindu));

        popular_news.setAdapter(new PopularNewsAdapter(list_news, PopularWebsitesActivity.this));
        popular_news.setLayoutManager(new GridLayoutManager(this, 4));
        popular_news.setHasFixedSize(true);
        popular_news.setLayoutFrozen(true);
        popular_news.setItemAnimator(new DefaultItemAnimator());
    }

    private void popularSearchEngines() {
        list_engine = new ArrayList<>();

        list_engine.add(new PopularWebsiteModalClass(getString(R.string.google), "https://www.google.com/", R.drawable.ic_google));
        list_engine.add(new PopularWebsiteModalClass(getString(R.string.yahoo), "https://in.yahoo.com/", R.drawable.ic_yahoo));
        list_engine.add(new PopularWebsiteModalClass(getString(R.string.bing), "https://www.bing.com/", R.drawable.ic_bing));
        list_engine.add(new PopularWebsiteModalClass(getString(R.string.duck_duck_go), "https://www.duckduckgo.com/", R.drawable.ic_duckduckgo));
        list_engine.add(new PopularWebsiteModalClass(getString(R.string.wikipedia), "https://www.wikipedia.org/", R.drawable.ic_wikipedia));

        popular_engine.setAdapter(new PopularEngineAdapter(list_engine, PopularWebsitesActivity.this));
        popular_engine.setLayoutManager(new GridLayoutManager(this, 4));
        popular_engine.setHasFixedSize(true);
        popular_engine.setItemAnimator(new DefaultItemAnimator());
        popular_engine.setLayoutFrozen(true);

    }

    private void popularShopping() {
        list_shopping = new ArrayList<>();

        list_shopping.add(new PopularWebsiteModalClass(getString(R.string.ebay), "https://in.ebay.com/", R.drawable.ic_ebay));
        list_shopping.add(new PopularWebsiteModalClass(getString(R.string.flipkart), "https://www.flipkart.com/", R.drawable.ic_flipkart));
        list_shopping.add(new PopularWebsiteModalClass(getString(R.string.amazon), "https://www.amazon.in/", R.drawable.ic_amazon));
        list_shopping.add(new PopularWebsiteModalClass(getString(R.string.myntra), "https://www.myntra.com/", R.drawable.ic_myntra));
        list_shopping.add(new PopularWebsiteModalClass(getString(R.string.ajio), "https://www.ajio.com/", R.drawable.ic_ajio));
        list_shopping.add(new PopularWebsiteModalClass(getString(R.string.snapdeal), "https://www.snapdeal.com/", R.drawable.ic_snapdeal));
        list_shopping.add(new PopularWebsiteModalClass(getString(R.string.apple), "https://www.apple.com/in/", R.drawable.ic_apple));
        list_shopping.add(new PopularWebsiteModalClass(getString(R.string.micosoft), "https://www.microsoft.com/en-in", R.drawable.ic_microsoft));

        popular_shopping.setAdapter(new PopularShoppingAdapter(list_shopping, PopularWebsitesActivity.this));
        popular_shopping.setLayoutManager(new GridLayoutManager(this, 4));
        popular_shopping.setHasFixedSize(true);
        popular_shopping.setItemAnimator(new DefaultItemAnimator());
        popular_shopping.setLayoutFrozen(true);
    }

    private void popularEntertainment() {
        list_entertainment = new ArrayList<>();

        list_entertainment.add(new PopularWebsiteModalClass(getString(R.string.hotstar), "https://www.hotstar.com/in", R.drawable.ic_hotstar));
        list_entertainment.add(new PopularWebsiteModalClass(getString(R.string.youtube), "https://www.youtube.com/", R.drawable.ic_youtube));
        list_entertainment.add(new PopularWebsiteModalClass(getString(R.string.discord), "https://discord.com/", R.drawable.ic_discord));
        list_entertainment.add(new PopularWebsiteModalClass(getString(R.string.hangout), "https://hangouts.google.com/", R.drawable.ic_google_hangouts));
        list_entertainment.add(new PopularWebsiteModalClass(getString(R.string.skype), "https://www.skype.com/", R.drawable.ic_skype));

        popular_entertainment.setAdapter(new PopularEntertainmentAdapter(list_entertainment, PopularWebsitesActivity.this));
        popular_entertainment.setLayoutManager(new GridLayoutManager(this, 4));
        popular_entertainment.setHasFixedSize(true);
        popular_entertainment.setItemAnimator(new DefaultItemAnimator());
        popular_entertainment.setLayoutFrozen(true);

    }

    private void popularDoubts() {
        list_doubts = new ArrayList<>();

        list_doubts.add(new PopularWebsiteModalClass(getString(R.string.reddit), "https://www.reddit.com/", R.drawable.ic_reddit));
        list_doubts.add(new PopularWebsiteModalClass(getString(R.string.quora), "https://www.quora.com/", R.drawable.ic_quora));
        list_doubts.add(new PopularWebsiteModalClass(getString(R.string.pinterest), "https://in.pinterest.com/", R.drawable.ic_pinterest));
        list_doubts.add(new PopularWebsiteModalClass(getString(R.string.stackoverflow), "https://stackoverflow.com/", R.drawable.ic_stack_overflow));

        popular_doubt.setAdapter(new PopularDoubtAdapter(list_doubts, PopularWebsitesActivity.this));
        popular_doubt.setLayoutManager(new GridLayoutManager(this, 4));
        popular_doubt.setHasFixedSize(true);
        popular_doubt.setItemAnimator(new DefaultItemAnimator());
        popular_doubt.setLayoutFrozen(true);
    }


    private void inflateItems() {
        back = findViewById(R.id.popular_web_back);
        popular_social = findViewById(R.id.popular_social_websites_rec);
        popular_classified = findViewById(R.id.popular_classified_websites_rec);
        popular_doubt = findViewById(R.id.popular_doubts_websites_rec);
        popular_entertainment = findViewById(R.id.popular_entertainment_websites_rec);
        popular_shopping = findViewById(R.id.popular_shopping_websites_rec);
        popular_engine = findViewById(R.id.popular_engine_websites_rec);
        popular_news = findViewById(R.id.popular_news_websites_rec);
    }

    private void popularSocial() {
        list_social = new ArrayList<>();
        list_social.add(new PopularWebsiteModalClass(getString(R.string.facebook), "https://www.facebook.com/", R.drawable.ic_facebook));
        list_social.add(new PopularWebsiteModalClass(getString(R.string.twitter), "https://twitter.com/", R.drawable.ic_twitter));
        list_social.add(new PopularWebsiteModalClass(getString(R.string.instagram), "https://instagram.com/home", R.drawable.ic_instagram));
        list_social.add(new PopularWebsiteModalClass(getString(R.string.tumblr), "https://www.tumblr.com/", R.drawable.ic_tumblr));
        list_social.add(new PopularWebsiteModalClass(getString(R.string.gmail), "https://mail.google.com/", R.drawable.ic_gmail));
        list_social.add(new PopularWebsiteModalClass(getString(R.string.outlook), "https://outlook.live.com/", R.drawable.ic_outlook));

        popular_social.setAdapter(new PopularSocialAdapter(list_social, PopularWebsitesActivity.this));
        popular_social.setLayoutManager(new GridLayoutManager(this, 4));
        popular_social.setHasFixedSize(true);
        popular_social.setItemAnimator(new DefaultItemAnimator());
    }


    @Override
    public void link_clicked_classified(int name) {
        Intent intent=new Intent();
        intent.putExtra("Website_Link",list_classified.get(name).getWeb_link());
        setResult(RESULT_OK,intent);
        finish();

    }

    @Override
    public void link_clicked_engine(int name) {
        Intent intent=new Intent();
        intent.putExtra("Website_Link",list_engine.get(name).getWeb_link());
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void link_clicked_doubt(int name) {
        Intent intent=new Intent();
        intent.putExtra("Website_Link",list_doubts.get(name).getWeb_link());
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void link_clicked_entertainment(int name) {
        Intent intent=new Intent();
        intent.putExtra("Website_Link",list_entertainment.get(name).getWeb_link());
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void link_clicked_news(int name) {
        Intent intent=new Intent();
        intent.putExtra("Website_Link",list_news.get(name).getWeb_link());
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void link_clicked_shopping(int name) {
        Intent intent=new Intent();
        intent.putExtra("Website_Link",list_shopping.get(name).getWeb_link());
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void link_clicked_social(int name) {
        Intent intent=new Intent();
        intent.putExtra("Website_Link",list_social.get(name).getWeb_link());
        setResult(RESULT_OK,intent);
        finish();
    }
}