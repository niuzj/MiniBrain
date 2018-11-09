package com.niuzj.minibrain.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebHistoryItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.niuzj.corelibrary.views.TitleBar;
import com.niuzj.minibrain.R;
import com.niuzj.minibrain.bean.UrlBean;

import butterknife.BindView;


public class WebViewActivity extends MiniBaseActivity {

    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.content_loading_progressbar)
    ContentLoadingProgressBar mContentLoadingProgressBar;
    @BindView(R.id.title_bar)
    TitleBar mTitleBar;

    private UrlBean mUrlBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_webview;
    }

    private void initData() {

        Intent intent = getIntent();
        if (intent == null) {
            finish();
        } else {
            mUrlBean = intent.getParcelableExtra("url_bean");
        }

    }

    private void initView() {
        if (mUrlBean == null || TextUtils.isEmpty(mUrlBean.url)) {
            finish();
        } else {
            new WebViewHelper().initWebView(webview);
            webview.loadUrl(mUrlBean.url);
            mTitleBar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
                @Override
                public void clickLeft() {
                    finish();
                }

                @Override
                public void clickRight() {
                    WebBackForwardList webBackForwardList = webview.copyBackForwardList();
                    WebHistoryItem currentItem = webBackForwardList.getCurrentItem();
                    String url = currentItem.getUrl();
                    UrlBean urlBean = new UrlBean();
                    urlBean.url = url;
                    Intent intent = new Intent(WebViewActivity.this, AddActivity.class);
                    intent.putExtra("url_bean", urlBean);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        webview.resumeTimers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webview.pauseTimers();
    }

    @Override
    protected void onDestroy() {
        //防止内存泄漏
        if (webview != null) {
            webview.clearHistory();
            ((ViewGroup) webview.getParent()).removeView(webview);
            webview.destroy();
            webview = null;
        }
        super.onDestroy();
    }

    public class WebViewHelper {

        public void initWebView(WebView webView) {
            WebSettings webSettings = webView.getSettings();
            //支持缩放，默认为true。
            webSettings.setSupportZoom(false);
            //调整图片至适合webview的大小
            webSettings.setUseWideViewPort(true);
            // 缩放至屏幕的大小
            webSettings.setLoadWithOverviewMode(true);
            //设置默认编码
            webSettings.setDefaultTextEncodingName("utf-8");
            //设置自动加载图片
            webSettings.setLoadsImagesAutomatically(true);

            //多窗口
            webSettings.supportMultipleWindows();
            //获取触摸焦点
            webView.requestFocusFromTouch();
            //允许访问文件
            webSettings.setAllowFileAccess(true);
            //开启javascript
            webSettings.setJavaScriptEnabled(true);
            //支持通过JS打开新窗口
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            //提高渲染的优先级
            webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
            //支持内容重新布局
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            //关闭webview中缓存
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

            webView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });

            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                    mContentLoadingProgressBar.setProgress(newProgress);
                    if (newProgress == 100) {
                        mContentLoadingProgressBar.setVisibility(View.GONE);
                    } else {
                        mContentLoadingProgressBar.setVisibility(View.VISIBLE);
                    }
                }


            });


        }

    }


}
