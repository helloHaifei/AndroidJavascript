package com.haifei.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity {

	private String TAG = "test";
	private WebView mWebView = null;
	JavascriptInterface mJavascriptInterface;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mWebView = (WebView) findViewById(R.id.webview);
		//配置webview
		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setAllowFileAccess(true);
		settings.setSaveFormData(true);
		settings.setSavePassword(true);
		//屏幕自适应方案一 
		settings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS); 
		
		// js与原生互调桥
		mJavascriptInterface = new JavascriptInterface();
		mWebView.addJavascriptInterface(mJavascriptInterface, "app");
		
		// ** 方式一 加载 
		//mWebView.loadUrl("file:///android_asset/localindex.html");
		
		//方式 二加载 
		String data = HfFileUtil.readAssetsByName(this, "localindex.html", "UTF-8");
		mWebView.loadDataWithBaseURL("file:///android_asset/", data, "text/html", "UTF-8","");
		
		mWebView.setWebViewClient(new MyWebViewClient());
		mWebView.setWebChromeClient(new Chrome());
		
		findViewById(R.id.button).setOnClickListener(new OnClickListener() {
			@Override
			@android.webkit.JavascriptInterface
			public void onClick(View v) {
				//mJavascriptInterface.show();
				mWebView.loadUrl("javascript:window.clickTest('"+"898989"+"')");
			}
		});
	}

	private class JavascriptInterface{
		// JavaScript调用此方法
		@android.webkit.JavascriptInterface
		public void showToast(String text) {
			Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
		}

		// Html调用此方法传递数据
		@android.webkit.JavascriptInterface
		public void show() {
			String json = "hello success";
			// 调用JS中的方法
			mWebView.loadUrl("javascript:clickTest('"+json+"')");
		}
	}
	
	class Chrome extends WebChromeClient{
		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			Log.i(TAG, "onJsAlert message="+message);
			return super.onJsAlert(view, url, message, result);
		}
		@Override
		public boolean onJsPrompt(WebView view, String url, String message,
				String defaultValue, JsPromptResult result) {
			Log.i(TAG, "onJsPrompt message="+message);
			return super.onJsPrompt(view, url, message, defaultValue, result);
		}
		@Override
		public boolean onJsConfirm(WebView view, String url, String message,
				JsResult result) {
			Log.i(TAG, "onJsConfirm message="+message);
			return super.onJsConfirm(view, url, message, result);
		}
		@Override
		public boolean onJsBeforeUnload(WebView view, String url,
				String message, JsResult result) {
			Log.i(TAG, "onJsBeforeUnload message="+message);
			return super.onJsBeforeUnload(view, url, message, result);
		}
		@Override
		public boolean onJsTimeout() {
			Log.i(TAG, "onJsTimeout ");
			return super.onJsTimeout();
		}
	}

	// 监听
	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			// html加载完成之后，添加监听图片的点击js函数
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			view.getSettings().setJavaScriptEnabled(true);
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if(mWebView != null && mWebView.canGoBack()){
				mWebView.goBack();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
		
	}

}