package com.sebox.diary.activity;

import com.sebox.diary.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SetPasswordActivity extends Activity {
	private boolean isSet=false;
	private TextView digitalPass=null;
	private TextView graphicPass;
	private TextView noPass;
	private EditText setPass=null;
	private EditText confirmPass=null;
	private SharedPreferences preferences;
	private ImageView back = null;
	private SharedPreferences sharedPreferences = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		init();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setBackground();
	}
	private void init(){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_pass_way);
		sharedPreferences = getSharedPreferences("image", MODE_PRIVATE);
		digitalPass=(TextView)findViewById(R.id.digitalpass);
		digitalPass.setOnClickListener(new DigitalPassListener());
		graphicPass=(TextView)findViewById(R.id.graphicpass);
		graphicPass.setOnClickListener(new GraphicPasslistener());
		noPass=(TextView)findViewById(R.id.nopass);
		noPass.setOnClickListener(new NoPassListener());
		back = (ImageView)this.findViewById(R.id.back_pass_diary);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_below_in, R.anim.push_below_out);
			}
		});
		setBackground();
	}
	
	private void setBackground() {
		// �õ���ǰ����
		LinearLayout layout = (LinearLayout) this.findViewById(R.id.add_pass_way_layout);
		// �õ�id,�˴�id�������ñ����������ģ��˴��ݲ�����
		int id = sharedPreferences.getInt("id", 0);
		if (id == 0) {// id=0˵���ǳ�ʼ��ʱ�ı���
			// ���ñ�������
			layout.setBackgroundResource(R.drawable.diary_view_bg);
		} else if (id == 1) {// id=1˵���û�ѡ���˵�һ��ͼƬ
			layout.setBackgroundResource(R.drawable.diary_view_bg);
		} else if (id == 2) {// id=2˵���û�ѡ���˵ڶ���ͼƬ
			layout.setBackgroundResource(R.drawable.spring);
		} else if (id == 3) {// id=3˵���û�ѡ���˵����ͼƬ
			layout.setBackgroundResource(R.drawable.summer);
		} else if (id == 4) {// id=4˵���û�ѡ���˵��ķ�ͼƬ
			layout.setBackgroundResource(R.drawable.autumn);
		} else if (id == 5) {// id=4˵���û�ѡ���˵��ķ�ͼƬ
			layout.setBackgroundResource(R.drawable.winter);
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.push_below_in, R.anim.push_below_out);
	}
	
	class DigitalPassListener implements OnClickListener{
		public void onClick(View v) {
			// TODO Auto-generated method stub
			LayoutInflater factory=LayoutInflater.from(SetPasswordActivity.this);
			final View textEntry=factory.inflate(R.layout.digital_pass, null);
			AlertDialog.Builder builder=new AlertDialog.Builder(SetPasswordActivity.this)
			.setTitle(getString(R.string.set_password))
			.setView(textEntry)
			.setPositiveButton(getString(R.string.set), new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					setPass=(EditText)textEntry.findViewById(R.id.set_pass);
					confirmPass=(EditText)textEntry.findViewById(R.id.confirm_pass);
					if (!confirmPass.getText().toString().trim().equals("")&&
							confirmPass.getText().toString().trim().equals(setPass.getText().toString().trim())) {
						preferences=getSharedPreferences("pass", Context.MODE_PRIVATE);
						Editor editor=preferences.edit();
						editor.putString("passway", "digitalpass");
						editor.putBoolean("isSet", !isSet);
						editor.putString("password", setPass.getText().toString().trim());
						editor.commit();
						dialog.dismiss();
						Toast.makeText(SetPasswordActivity.this, R.string.pass_set_success, Toast.LENGTH_LONG).show();
					}
					else {
						Toast.makeText(SetPasswordActivity.this, R.string.dif_pass, Toast.LENGTH_LONG).show();
					}
				}
			})
			.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			builder.create().show();
		}
	}
	
	class GraphicPasslistener implements OnClickListener{
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent =new Intent();
			intent.setClass(SetPasswordActivity.this, GraphicPassSetActivity.class);
			startActivity(intent);
		}
	}
	
	class NoPassListener implements OnClickListener{
		public void onClick(View v) {
			preferences=getSharedPreferences("pass", Context.MODE_PRIVATE);
			Editor editor=preferences.edit();
			editor.putString("passway", null);
			editor.commit();
			onBackPressed();
			Toast.makeText(SetPasswordActivity.this, R.string.pass_cancel, Toast.LENGTH_LONG).show();
		}
	}
}
