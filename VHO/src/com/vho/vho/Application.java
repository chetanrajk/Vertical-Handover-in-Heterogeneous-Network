package com.vho.vho;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Application extends Activity{
	private static int type;
	private Button save;

	public static int getType() {
		return type;
	}

	public static void setType(int type) {
		Application.type = type;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.application);

		save=(Button)findViewById(R.id.save);
		save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				RadioGroup selectOption = (RadioGroup) findViewById(R.id.selectOption);
			    RadioButton application = (RadioButton) findViewById(selectOption.getCheckedRadioButtonId());
		        
			    // Get Application Type Id Selected
			    type = application.getId();
		        type=type%10;
		        
		        // Application Type
		        String apptype=application.getText().toString();
		        Toast.makeText(getApplicationContext(), "selected application :"+apptype, Toast.LENGTH_LONG).show();        		             
		        Intent intent=new Intent("com.vho.vho.MANAGER");
				startActivity(intent);
				 
			}
		});
	}

	
}
