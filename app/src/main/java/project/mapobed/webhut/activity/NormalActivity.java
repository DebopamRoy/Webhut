package project.mapobed.webhut.activity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import project.mapobed.webhut.R;
import project.mapobed.webhut.adapter.PopularSocialAdapter;
import project.mapobed.webhut.fragment.BookmarkDialog;
import project.mapobed.webhut.modalclass.PopularWebsiteModalClass;
import project.mapobed.webhut.room.Bookmark;
import project.mapobed.webhut.room.BookmarkViewModel;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;

public class NormalActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, BookmarkDialog.ExampleDialogListener, PopularSocialAdapter.pop_website_link_clicked {
    private static final int EXTERNAL_STORAGE_WRITE = 1, EXTERNAL_STORAGE_READ = 2, MIC_REQ_CODE = 3;
    private static final int POP_WEB_REQ = 4;
    private static final int ADD_BOOKMARK = 5;
    private static final int ACCESS_FINE_LOCATION = 6;
    private ImageView home, more, bookmark, close;
    private EditText search;
    private ProgressBar progressBar;
    private String[] permission = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION};
    private WebView webView;
    private boolean desktop_enable = false;
    private SharedPreferences sharedPreferences;
    private String executableJavaScript = "";
    private LottieAnimationView anm;
    private TextView anim_text, popular_website_view_all;
    private RecyclerView recyclerView_popular;
    private ConstraintLayout animation_cons, home_cons;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<PopularWebsiteModalClass> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);

        inflateItems();

       /* Intent intent = getIntent();
       // String action = intent.getAction();
        Uri data = intent.getData();
        if (!data.toString().equals(null))
            performSearch(data + "");
*/
        closeKeyboard();

        popularWebsites();

        webSettings();


        bookmark.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivityForResult(new Intent(NormalActivity.this, BookmarkActivity.class), ADD_BOOKMARK);
                return true;
            }
        });


        popular_website_view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(NormalActivity.this, PopularWebsitesActivity.class), POP_WEB_REQ);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Close Button
                if (!search.getText().toString().trim().isEmpty()) {
                    close.setImageResource(R.drawable.ic_round_mic_none_24);
                    search.setText("");
                } else {
                    //Mic Button
                    Intent micIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    micIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    micIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

                    if (micIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(micIntent, MIC_REQ_CODE);
                    } else {
                        Toasty.error(NormalActivity.this, R.string.doesnt_support_voice_input, Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.reload();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookmarkDialog exampleDialog = new BookmarkDialog(search.getText().toString().trim(), webView.getTitle());
                exampleDialog.show(getSupportFragmentManager(), "bookmark_dialog");

            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu nor_PopupMenu = new PopupMenu(NormalActivity.this, v);
                nor_PopupMenu.setOnMenuItemClickListener(NormalActivity.this);
                nor_PopupMenu.inflate(R.menu.nor_more);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    nor_PopupMenu.setForceShowIcon(true);
                nor_PopupMenu.show();
            }
        });

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    closeKeyboard();
                    if (checkNetwork()) {

                        anim_text.setVisibility(View.GONE);
                        anm.setVisibility(View.GONE);
                        animation_cons.setVisibility(View.GONE);
                        performSearch(search.getText().toString().trim());
                    } else {
                        home_cons.setVisibility(View.GONE);
                        anim_text.setVisibility(View.VISIBLE);
                        anm.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setVisibility(View.GONE);
                        animation_cons.setVisibility(View.VISIBLE);
                    }
                    return true;
                }
                return false;

            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                search.setText(webView.getUrl());
                close.setImageResource(R.drawable.ic_round_close_24);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                exeJavaScript();
                progressBar.setVisibility(View.INVISIBLE);
            }


        });

        webView.setWebChromeClient(new WebChromeClient() {


            private View mCustomView;
            private WebChromeClient.CustomViewCallback mCustomViewCallback;
            private int mOriginalOrientation;
            private int mOriginalSystemUiVisibility;


            public Bitmap getDefaultVideoPoster() {
                if (mCustomView == null) {
                    return null;
                }
                return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                if (ContextCompat.checkSelfPermission(NormalActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(NormalActivity.this, permission, ACCESS_FINE_LOCATION);
                } else {

                    enableLocation();
                    callback.invoke(origin, true, false);
                }
            }

            public void onHideCustomView() {
                ((FrameLayout) getWindow().getDecorView()).removeView(this.mCustomView);
                this.mCustomView = null;
                getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
                setRequestedOrientation(this.mOriginalOrientation);
                this.mCustomViewCallback.onCustomViewHidden();
                this.mCustomViewCallback = null;
            }

            public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback) {
                if (this.mCustomView != null) {
                    onHideCustomView();
                    return;
                }
                this.mCustomView = paramView;
                this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
                this.mOriginalOrientation = getRequestedOrientation();
                this.mCustomViewCallback = paramCustomViewCallback;
                ((FrameLayout) getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
                getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }


            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.INVISIBLE);
                    exeJavaScript();
                }
                progressBar.setProgress(newProgress);
            }

        });


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
                NormalActivity.this.recreate();
                webView.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.GONE);
                webView.clearHistory();
                close.setImageResource(R.drawable.ic_round_mic_none_24);
                home_cons.setVisibility(View.VISIBLE);

            }
        });


        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (ContextCompat.checkSelfPermission(NormalActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(NormalActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_WRITE);
                } else {
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    request.setMimeType(mimetype);

                    String cookies = CookieManager.getInstance().getCookie(url);
                    request.addRequestHeader("cookie", cookies);
                    request.addRequestHeader("User-Agent", userAgent);
                    request.setDescription(getString(R.string.starting_download));

                    Toasty.success(NormalActivity.this, R.string.starting_download, Toast.LENGTH_SHORT).show();

                    request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimetype));
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                }
            }
        });

    }

    private void enableLocation() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));
        builder.setAlwaysShow(true);
        LocationSettingsRequest mLocationSettingsRequest = builder.build();

        SettingsClient mSettingsClient = LocationServices.getSettingsClient(NormalActivity.this);

        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        //Success Perform Task Here
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(NormalActivity.this, 32);
                                } catch (IntentSender.SendIntentException sie) {
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        }
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                    }
                });
    }


    private void popularWebsites() {
        list = new ArrayList<>();
        list.add(new PopularWebsiteModalClass(getString(R.string.facebook), "https://www.facebook.com/", R.drawable.ic_facebook));
        list.add(new PopularWebsiteModalClass(getString(R.string.twitter), "https://twitter.com/", R.drawable.ic_twitter));
        list.add(new PopularWebsiteModalClass(getString(R.string.instagram), "https://instagram.com/home", R.drawable.ic_instagram));
        list.add(new PopularWebsiteModalClass(getString(R.string.skype), "https://www.skype.com/", R.drawable.ic_skype));
        list.add(new PopularWebsiteModalClass(getString(R.string.youtube), "https://www.youtube.com/", R.drawable.ic_youtube));
        list.add(new PopularWebsiteModalClass(getString(R.string.gmail), "https://mail.google.com/", R.drawable.ic_gmail));
        list.add(new PopularWebsiteModalClass(getString(R.string.quora), "https://www.quora.com/", R.drawable.ic_quora));
        list.add(new PopularWebsiteModalClass(getString(R.string.stackoverflow), "https://stackoverflow.com/", R.drawable.ic_stack_overflow));
        list.add(new PopularWebsiteModalClass(getString(R.string.amazon), "https://www.amazon.in/", R.drawable.ic_amazon));
        list.add(new PopularWebsiteModalClass(getString(R.string.flipkart), "https://www.flipkart.com/", R.drawable.ic_flipkart));
        list.add(new PopularWebsiteModalClass(getString(R.string.apple), "https://www.apple.com/in/", R.drawable.ic_apple));
        list.add(new PopularWebsiteModalClass(getString(R.string.ndtv), "https://www.ndtv.com/", R.drawable.ic_ndtv));

        recyclerView_popular.setAdapter(new PopularSocialAdapter(list, this));
        recyclerView_popular.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView_popular.setHasFixedSize(true);
        recyclerView_popular.setItemAnimator(new DefaultItemAnimator());
        recyclerView_popular.setLayoutFrozen(true);
    }

    private boolean checkNetwork() {
        ConnectivityManager con_manager = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);

        return (con_manager.getActiveNetworkInfo() != null
                && con_manager.getActiveNetworkInfo().isAvailable()
                && con_manager.getActiveNetworkInfo().isConnected());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == EXTERNAL_STORAGE_WRITE) {

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toasty.error(this, getString(R.string.unsuccessful_download), Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == EXTERNAL_STORAGE_READ) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toasty.error(this, getString(R.string.unsuccessful_upload), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MIC_REQ_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    search.setText(result.get(0));
                    performSearch(search.getText().toString().trim());
                }
                break;
            case POP_WEB_REQ:
                if (resultCode == RESULT_OK)
                    if (checkNetwork()) {
                        anim_text.setVisibility(View.GONE);
                        anm.setVisibility(View.GONE);
                        animation_cons.setVisibility(View.GONE);
                        performSearch(data.getStringExtra("Website_Link"));
                    } else {
                        home_cons.setVisibility(View.GONE);
                        anim_text.setVisibility(View.VISIBLE);
                        anm.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setVisibility(View.GONE);
                        animation_cons.setVisibility(View.VISIBLE);
                    }
                break;

            case ADD_BOOKMARK:
                if (resultCode == RESULT_OK) {
                    if (checkNetwork()) {
                        anim_text.setVisibility(View.GONE);
                        anm.setVisibility(View.GONE);
                        animation_cons.setVisibility(View.GONE);
                        String url = data.getStringExtra("book_url");
                        performSearch(url);
                    } else {
                        home_cons.setVisibility(View.GONE);
                        anim_text.setVisibility(View.VISIBLE);
                        anm.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setVisibility(View.GONE);
                        animation_cons.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
    }

    void performSearch(String url) {
        if (Patterns.WEB_URL.matcher(url).matches()) {
            if (url.startsWith("www")) {
                url = "http://" + url;
            } else if (url.startsWith("https://") || (url.startsWith("http://"))) {

            } else {
                url = "http://" + url;
            }
            webView.loadUrl(url);
            webView.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        } else {
            String en = sharedPreferences.getString("engine", getString(R.string.google));

            if (en.equals(getString(R.string.google))) {
                url = "https://www.google.com/search?q=" + url;
            } else if (en.equals(getString(R.string.yahoo))) {

                url = "https://in.search.yahoo.com/search?p=" + url;
            } else if (en.equals(getString(R.string.bing))) {

                url = "https://www.bing.com/search?q=" + url;
            } else {
                url = "https://duckduckgo.com/?q=" + url;
            }

            webView.loadUrl(url);
        }
        animation_cons.setVisibility(View.GONE);
        home_cons.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        webView.setVisibility(View.VISIBLE);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private void exeJavaScript() {

        if (executableJavaScript != null) {
            webView.evaluateJavascript(executableJavaScript, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                }
            });
        }
    }

    private void webSettings() {
        final WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        registerForContextMenu(webView);
        webSettings.setUseWideViewPort(true);
        webSettings.setDomStorageEnabled(true);
        webView.getSettings().setSavePassword(true);
        webSettings.setSupportMultipleWindows(false);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setUserAgentString("Mozilla/5.0 (Linux; Android 5.1.1; Nexus 5 Build/LMY48B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.65 Mobile Safari/537.36");
        webSettings.setDatabaseEnabled(true);
        CookieSyncManager.createInstance(this);
        CookieManager.getInstance().setAcceptCookie(true);
        webSettings.setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        executableJavaScript = getIntent().getStringExtra("executableJavaScript");
    }

    private void inflateItems() {
        sharedPreferences = getSharedPreferences(getString(R.string.shared_preference), MODE_PRIVATE);
        anim_text = findViewById(R.id.anim_text);
        popular_website_view_all = findViewById(R.id.normal_websites_view_all);
        bookmark = findViewById(R.id.normal_search_bookmark);
        home = findViewById(R.id.normal_search_home);
        more = findViewById(R.id.normal_search_more_menu);
        animation_cons = findViewById(R.id.normal_no_internet);
        anm = findViewById(R.id.animationView);
        search = findViewById(R.id.normal_search_edit_text);
        close = findViewById(R.id.normal_search_close);
        webView = findViewById(R.id.normal_web_view);
        progressBar = findViewById(R.id.normal_progress_bar);
        swipeRefreshLayout = findViewById(R.id.nor_swipe_refresh_layout);
        home_cons = findViewById(R.id.normal_home_page);
        recyclerView_popular = findViewById(R.id.normal_popular_rec);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nor_new_incognito:
                Toasty.success(this, R.string.incognito_mode_started, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, IncognitoActivity.class));
                break;
            case R.id.nor_reload:
                if (search.getText().toString().trim().isEmpty())
                    Toasty.error(this, R.string.url_empty, Toast.LENGTH_SHORT).show();
                else
                    webView.reload();
                break;
            case R.id.nor_desktop:
                if (desktop_enable) {
                    setDesktopMode(webView, true);
                    desktop_enable = false;
                } else {
                    setDesktopMode(webView, false);
                    desktop_enable = true;
                }

                break;
            case R.id.nor_help_feedback:
                startActivity(new Intent(this, HelpActivity.class));
                break;
            case R.id.nor_setting:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.nor_bookmark:
                startActivityForResult(new Intent(this, BookmarkActivity.class), ADD_BOOKMARK);
                break;
            case R.id.nor_share:
                if (search.getText().toString().trim().length() > 0) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, search.getText().toString().trim());
                    startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_url_via)));
                } else {
                    Toasty.warning(this, R.string.select_valid_url, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nor_new_tab:
                search.setText("");
                this.recreate();
                break;
            case R.id.nor_exit:
                finish();
                Toasty.success(this, R.string.application_close, Toast.LENGTH_SHORT).show();
                System.exit(0);
                break;
        }

        return false;
    }

    public void setDesktopMode(WebView webView, boolean enabled) {
        String newUserAgent = webView.getSettings().getUserAgentString();
        if (enabled) {
            try {
                String ua = webView.getSettings().getUserAgentString();
                String androidOSString = webView.getSettings().getUserAgentString().substring(ua.indexOf("("), ua.indexOf(")") + 1);
                newUserAgent = webView.getSettings().getUserAgentString().replace(androidOSString, "(X11; Linux x86_64)");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            newUserAgent = null;
        }

        webView.getSettings().setUserAgentString(newUserAgent);
        webView.getSettings().setUseWideViewPort(enabled);
        webView.getSettings().setLoadWithOverviewMode(enabled);
        performSearch(search.getText().toString().trim());
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack())
            webView.goBack();
        else if (home_cons.getVisibility() == View.VISIBLE) {
            System.exit(0);
        } else {
            search.setText("");
            this.recreate();
        }
    }


    @Override
    public void applyTexts(String name, String link) {

        if (name.isEmpty() || link.isEmpty()) {
            //TODO:
            Toasty.error(this, R.string.bookmark_empty, Toast.LENGTH_SHORT).show();

        } else {

            Bookmark bookmark = new Bookmark(name, link);
            new BookmarkViewModel(this.getApplication()).insert(bookmark);
            Toasty.success(this, R.string.url_added_as_bookmark, Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void link_clicked_social(int name) {
        if (checkNetwork()) {
            anim_text.setVisibility(View.GONE);
            anm.setVisibility(View.GONE);
            animation_cons.setVisibility(View.GONE);
            performSearch(list.get(name).getWeb_link());
        } else {
            home_cons.setVisibility(View.GONE);
            anim_text.setVisibility(View.VISIBLE);
            anm.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);
            animation_cons.setVisibility(View.VISIBLE);
        }
    }
}