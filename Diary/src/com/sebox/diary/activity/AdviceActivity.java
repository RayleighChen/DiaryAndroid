package com.sebox.diary.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sebox.diary.R;
import com.sebox.diary.adapter.AutoTextViewAdapter;

public class AdviceActivity extends Activity implements TextWatcher {
	private ImageView back = null;
	private EditText userName = null;
	private AutoCompleteTextView userEmail = null;
	private EditText adviceInfo = null;
	/*private EditText emailPass = null;*/
	private AutoTextViewAdapter adapter = null;
	private SharedPreferences preferences = null;
	private Button submit = null;
	private static final String[] AUTO_EMAILS = { "@163.com", "@sina.com",
			"@qq.com", "@126.com", "@gmail.com", "@apple.com","@sohu.com",
			"@foxmail.com","@sina.cn","@yahoo.com.cn"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.advice);
		preferences = getSharedPreferences("image", MODE_PRIVATE);
		back = (ImageView) this.findViewById(R.id.back_advice);
		userName = (EditText) this.findViewById(R.id.user_name);
		userEmail = (AutoCompleteTextView) this.findViewById(R.id.user_email);
		/*emailPass = (EditText)this.findViewById(R.id.user_email_pass);*/
		adapter = new AutoTextViewAdapter(this);
		userEmail.setAdapter(adapter);
		userEmail.setThreshold(1);// 输入1个字符时就开始检测，默认为2个
		userEmail.addTextChangedListener(this);// 监听autoview的变化
		adviceInfo = (EditText) this.findViewById(R.id.advice_info);
		submit = (Button) this.findViewById(R.id.submit);
		submit.setOnClickListener(new SubmitListener());
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_below_in,
						R.anim.push_below_out);
			}
		});
		setBackground();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setBackground();
	}

	private void setBackground() {
		// 得到当前布局
		LinearLayout layout = (LinearLayout) this
				.findViewById(R.id.advice_layout);
		// 得到id,此处id是在设置背景里面产生的，此处暂不解释
		int id = preferences.getInt("id", 0);
		if (id == 0) {// id=0说明是初始化时的背景
			// 设置背景方法
			layout.setBackgroundResource(R.drawable.diary_view_bg);
		} else if (id == 1) {// id=1说明用户选择了第一幅图片
			layout.setBackgroundResource(R.drawable.diary_view_bg);
		} else if (id == 2) {// id=2说明用户选择了第二幅图片
			layout.setBackgroundResource(R.drawable.spring);
		} else if (id == 3) {// id=3说明用户选择了第三幅图片
			layout.setBackgroundResource(R.drawable.summer);
		} else if (id == 4) {// id=4说明用户选择了第四幅图片
			layout.setBackgroundResource(R.drawable.autumn);
		} else if (id == 5) {// id=4说明用户选择了第四幅图片
			layout.setBackgroundResource(R.drawable.winter);
		}
	}

	class SubmitListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (!userEmail.getText().toString().trim().equals("")
					&& !userName.getText().toString().trim().equals("")
					&& !adviceInfo.getText().toString().trim().equals("")) {
				String myReciver = "seeker199291@gmail.com"; //收件人   
			    String mySubject = getString(R.string.theme); //主题   
			    String myBody = adviceInfo.getText().toString().trim()
			    		+"\n来自于："+userEmail.getText().toString().trim()+"  "+userName.getText().toString().trim();   
			    Intent myIntent=new Intent(android.content.Intent.ACTION_SEND);  
	            myIntent.setType("plain/text");//设置邮件格式   
	            myIntent.putExtra(android.content.Intent.EXTRA_EMAIL, myReciver);  
	            myIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mySubject);   
	            myIntent.putExtra(android.content.Intent.EXTRA_TEXT, myBody);  
	            startActivity(Intent.createChooser(myIntent, getString(R.string.email_choose)));
			}else {
				Toast.makeText(AdviceActivity.this, getString(R.string.detail_info), 0).show();
			}
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.push_below_in, R.anim.push_below_out);
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		String input = s.toString();
		adapter.mList.clear();
		autoAddEmails(input);
		adapter.notifyDataSetChanged();
		userEmail.showDropDown();
	}

	/**
	 * 自动填充邮箱列表
	 * 
	 * @param input
	 */
	private void autoAddEmails(String input) {
		String autoEmail = "";
		if (input.length() > 0) {
			for (int i = 0; i < AUTO_EMAILS.length; ++i) {
				if (input.contains("@")) {// 包含“@”则开始过滤
					String filter = input.substring(input.indexOf("@") + 1,
							input.length());// 获取过滤器，即根据输入“@”之后的内容过滤出符合条件的邮箱
					if (AUTO_EMAILS[i].contains(filter)) {// 符合过滤条件
						autoEmail = input.substring(0, input.indexOf("@"))
								+ AUTO_EMAILS[i];// 用户输入“@”之前的内容加上自动填充的内容即为最后的结果
						adapter.mList.add(autoEmail);
					}
				} else {
					autoEmail = input + AUTO_EMAILS[i];
					adapter.mList.add(autoEmail);
				}
			}
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}
}
