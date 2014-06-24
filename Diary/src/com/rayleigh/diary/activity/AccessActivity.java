package com.rayleigh.diary.activity;
import com.rayleigh.diary.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class AccessActivity extends Activity {
	private Button access;
	private SharedPreferences sp=null;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.access);
		access = (Button)this.findViewById(R.id.access);
		access.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sp=getSharedPreferences("pass", Context.MODE_PRIVATE);
				String passWay=sp.getString("passway", null);
				Intent intent = null;
				if (passWay!=null) {
					if (passWay.equals("graphicpass")) {
						intent =new Intent(AccessActivity.this,CheckPassActivity.class);
						startActivity(intent);
						overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
						AccessActivity.this.finish();
					}
					else {
						intent = new Intent(AccessActivity.this, MainActivity.class);  
				        startActivity(intent);
				        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
				        AccessActivity.this.finish();  
					}
				}
				else {
					intent = new Intent(AccessActivity.this, MainActivity.class);  
			        startActivity(intent);
			        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
			        AccessActivity.this.finish();
				}
			}
		});
	}
}
