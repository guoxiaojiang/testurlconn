package com.guo.androidtest;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

public class TestHttpImageActivity extends Activity {
	
	private ImageView imgview;
	private MyBean mbean;
	private TextView tv;
	
	private Handler MyHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (imgview != null && mbean != null && mbean.bitmap != null) {
				imgview.setImageBitmap(mbean.bitmap);
//				StringBuilder sb = new StringBuilder();
//				sb.append("ContentLength:" + mbean.contentLength).append("\n").append("ContentEnconding:").append(mbean.contentEnconding);
				tv.setText(mbean.headerStr);
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		imgview = (ImageView) findViewById(R.id.myimg);
		tv = (TextView) findViewById(R.id.mytxt);
		getImage();
	}

	
	private void getImage() {
		new Thread() {
			public void run() {
				try {
				 URL url = new URL("http://qr.cp31.ott.cibntv.net/qr?tiny=VqBVr5j&size=528&logosize=0&prefix=http%3A//qr.cp31.ott.cibntv.net");
//				 url = new URL("http://news.xinhuanet.com/photo/2012-02/09/122675973_51n.jpg");
		            HttpURLConnection conn= (HttpURLConnection) url.openConnection();  
		            conn.setRequestMethod("GET");  
		            conn.addRequestProperty("Accept-Encoding", "identity");
		            conn.setConnectTimeout(5*1000);  
		            conn.connect();  
		            mbean = new MyBean();
		            mbean.contentLength = conn.getContentLength();
//		            mbean.contentType = conn.getContentType();
//		            mbean.contentEnconding = conn.getContentEncoding();
		            StringBuilder sb = new StringBuilder();
		            Map<String, List<String>> headerFields = conn.getHeaderFields();
		            for (String headerName: headerFields.keySet()) {
		            	List<String> header = headerFields.get(headerName);
		            	sb.append(headerName).append(":");
		            	for (String string : header) {
							sb.append(string).append(";");
						}
		            	sb.append("\n");
		            }
		            mbean.headerStr = sb.toString();
		            InputStream in=conn.getInputStream();  
		            ByteArrayOutputStream bos=new ByteArrayOutputStream();  
		            byte[] buffer=new byte[1024];  
		            int len = 0;  
		            while((len=in.read(buffer))!=-1){  
		                bos.write(buffer,0,len);  
		            }  
		            byte[] dataImage=bos.toByteArray();  
		            bos.close();  
		            in.close();  
		            
		            mbean.bitmap = BitmapFactory.decodeByteArray(dataImage, 0, dataImage.length);  
		            //Drawable drawable=BitmapDrawable.  
		            MyHandler.sendEmptyMessage(0);
				} catch(Exception e) {
					
				}
			}
		}.start();
	}
	
	class MyBean {
		Bitmap bitmap;
		int contentLength;
//		String contentType;
//		String contentEnconding;
		String headerStr;
	}
}
