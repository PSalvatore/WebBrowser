package com.example.lab5;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	private EditText urlEt;
	private Button searchButton;
	private WebView webWindow;
	public String urlHtml;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		urlEt = (EditText) findViewById(R.id.url_textfield);
		searchButton = (Button) findViewById(R.id.search_button);
		webWindow = (WebView) findViewById(R.id.wView);
		
		searchButton.setOnClickListener(buttonListener);
		
		
	}

	private OnClickListener buttonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			try{
		    DownloadUrlInfo dl = new DownloadUrlInfo();
			String urlTxt = (urlEt).getText().toString();
			URL url = new URL(urlTxt);
			dl.execute(url);
			} catch (Exception e) {
				Log.e("Error: ", e.getMessage());
			}
		}
	};

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    // Checks the orientation of the screen
	    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
	        webWindow.loadData(urlHtml, "text/html", "UTF-8");
	    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
	        webWindow.loadData(urlHtml, "text/html", "UTF-8");
	    }
	}
	
	@SuppressLint("HandlerLeak")
	public Handler messageHandler = new Handler() {
        public void handleMessage(Message msg) {
        	urlHtml = (String) msg.obj;
        	webWindow.loadData(urlHtml, "text/html", "UTF-8");
        }
    };
    
    
    private class DownloadUrlInfo extends AsyncTask<URL, Void, String>{
    	private static final int URL_CONTENT = 0;

		@Override
    	protected String doInBackground(URL... url) {
    		String urlContent = "";
    		StringBuilder contentBuilder = new StringBuilder();
    		try {
    			URLConnection yc = (url[0]).openConnection();
    			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
    			
    			String str;
    			while ((str = in.readLine()) != null){
    				contentBuilder.append(str);
    				if(str != null){
    					contentBuilder.append("\n");
    				} 
    			}
    			in.close();
    			
    		} catch (Exception e) {
    			Log.e("Error: ", e.getMessage());
    		}
    		urlContent = contentBuilder.toString();
    		System.out.println("Url Content:\n" + urlContent);
    		return urlContent;
    	}
    	
    	protected void onPostExecute(String result){
    		Message msg = messageHandler.obtainMessage(URL_CONTENT, result);
    		messageHandler.sendMessage(msg);
    	}

    }
	
}




