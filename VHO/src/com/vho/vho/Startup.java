package com.vho.vho;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class Startup extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
    
    	Thread timer=new Thread(){
			public void run()
			{
				try{
					sleep(3000);
				}
				catch(Exception e){
					e.printStackTrace();
				}
				finally{
					Intent intent=new Intent("com.vho.vho.APPLICATION");
					startActivity(intent);
				}
			}
		};
		timer.start();
	
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
