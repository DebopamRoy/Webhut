package project.mapobed.webhut.activity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
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
import android.webkit.DownloadListener;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import project.mapobed.webhut.R;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;

public class IncognitoActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private static final int EXTERNAL_STORAGE = 1;
    private static final int MIC_REQCODE = 3;
    private static final int ADD_BOOKMARK = 2;
    private ImageView home, more;
    private EditText search;
    private WebView webView;
    private boolean desktop_enable=false;
    private ProgressBar progressBar;
    private String executableJavaScript;
    private ConstraintLayout incognito_initial_preview;
    private SharedPreferences sharedPreferences;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView close;
    private TextView anim_text;
    private LottieAnimationView anm;
    private ConstraintLayout anim_cons;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MIC_REQCODE:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    search.setText(result.get(0));
                    performSearch(search.getText().toString().trim());
                }
                break;
            case ADD_BOOKMARK:
                if (resultCode == RESULT_OK) {
                    if (checkNetwork()) {
                        anim_text.setVisibility(View.GONE);
                        anm.setVisibility(View.GONE);
                        anim_cons.setVisibility(View.GONE);
                        String url = data.getStringExtra("book_url");
                        performSearch(url);
                    } else {
                        //_cons.setVisibility(View.GONE);
                        anim_text.setVisibility(View.VISIBLE);
                        anm.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setVisibility(View.GONE);
                        anim_cons.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incognito);

        inflateItems();

        closeKeyboard();

        webSettings();

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
                        startActivityForResult(micIntent, MIC_REQCODE);
                    } else {
                        Toasty.error(IncognitoActivity.this, R.string.doesnt_support_voice_input, Toast.LENGTH_SHORT).show();
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

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.setVisibility(View.GONE);
                incognito_initial_preview.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setVisibility(View.GONE);
                search.setText("");
                close.setImageResource(R.drawable.ic_round_mic_none_24);
                IncognitoActivity.this.recreate();
            }
        });

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (ContextCompat.checkSelfPermission(IncognitoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(IncognitoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE);
                } else {
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    request.setMimeType(mimetype);

                    String cookies = CookieManager.getInstance().getCookie(url);
                    request.addRequestHeader("cookie", cookies);
                    request.addRequestHeader("User-Agent", userAgent);
                    request.setDescription(getString(R.string.starting_download));

                    Toasty.success(IncognitoActivity.this, R.string.starting_download, Toast.LENGTH_SHORT).show();

                    request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimetype));
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                }
            }
        });


        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu incog_PopupMenu = new PopupMenu(IncognitoActivity.this, v);
                incog_PopupMenu.setOnMenuItemClickListener(IncognitoActivity.this);
                incog_PopupMenu.inflate(R.menu.incog_more);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    incog_PopupMenu.setForceShowIcon(true);
                incog_PopupMenu.show();
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
                        anim_cons.setVisibility(View.GONE);
                        performSearch(search.getText().toString().trim());
                    } else {
                        incognito_initial_preview.setVisibility(View.GONE);
                        anim_text.setVisibility(View.VISIBLE);
                        anm.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setVisibility(View.GONE);
                        anim_cons.setVisibility(View.VISIBLE);
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

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

            }
        });


    }

    private void performSearch(String url) {
        if (Patterns.WEB_URL.matcher(url).matches()) {
            if (url.startsWith("www")) {
                url = "http://" + url;
            } else if (url.startsWith("https://") || (url.startsWith("http://"))) {

            } else
                url = "http://" + url;

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
        anim_cons.setVisibility(View.GONE);
        incognito_initial_preview.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
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
        webSettings.setDomStorageEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportMultipleWindows(false);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setUserAgentString(webSettings.getUserAgentString().replace("; wv", ""));
        webSettings.setDatabaseEnabled(true);
        webSettings.setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        executableJavaScript = getIntent().getStringExtra("executableJavaScript");
    }

    private void inflateItems() {
        sharedPreferences = getSharedPreferences(getString(R.string.shared_preference), MODE_PRIVATE);
        home = findViewById(R.id.incognito_search_home);
        more = findViewById(R.id.incognito_search_more_menu);
        close = findViewById(R.id.incognito_search_close);
        progressBar = findViewById(R.id.incognito_progress_bar);
        anim_cons = findViewById(R.id.incognito_no_internet);
        anm = findViewById(R.id.incognito_animationView);
        anim_text = findViewById(R.id.incognito_anim_text);
        search = findViewById(R.id.incognito_search_edit_text);
        webView = findViewById(R.id.incognito_web_view);
        incognito_initial_preview = findViewById(R.id.incognito_initial_preview);
        swipeRefreshLayout = findViewById(R.id.incognito_swipe_refresh_layout);
    }

    private boolean checkNetwork() {
        ConnectivityManager con_manager = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);

        return (con_manager.getActiveNetworkInfo() != null
                && con_manager.getActiveNetworkInfo().isAvailable()
                && con_manager.getActiveNetworkInfo().isConnected());
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack())
            webView.goBack();
        else if (incognito_initial_preview.getVisibility() == View.VISIBLE) {
            Toasty.success(this, getString(R.string.incognito_mode_closed), Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        } else {
            search.setText("");
            webView.setVisibility(View.GONE);
            anim_cons.setVisibility(View.GONE);
            incognito_initial_preview.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.incog_share:
                if (search.getText().toString().trim().length() > 0) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, search.getText().toString().trim());
                    startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_url_via)));
                } else {
                    Toasty.warning(this, R.string.select_valid_url, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.incog_new_tab:
                finish();
                break;
            case R.id.incog_new_incognito:
                this.recreate();
                break;
            case R.id.incog_desktop:
                if (desktop_enable) {
                    setDesktopMode(webView, true);
                    desktop_enable = false;
                } else {
                    setDesktopMode(webView, false);
                    desktop_enable = true;
                }
                break;
            case R.id.incog_reload:
                if (search.getText().toString().trim().isEmpty())
                    Toasty.error(this, R.string.url_empty, Toast.LENGTH_SHORT).show();
                else
                    webView.reload();
                break;
            case R.id.incog_bookmark:
                startActivityForResult(new Intent(IncognitoActivity.this, BookmarkActivity.class), ADD_BOOKMARK);
                break;
            case R.id.incog_exit:
                finish();
                Toasty.success(this, R.string.incognito_mode_closed, Toast.LENGTH_SHORT).show();
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

}