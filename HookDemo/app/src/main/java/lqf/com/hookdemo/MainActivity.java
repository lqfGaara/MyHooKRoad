package lqf.com.hookdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    int a=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final View btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a+=1;
                Toast.makeText(MainActivity.this,"Button 被点击了"+a,Toast.LENGTH_SHORT).show();
            }
        });
        HookViewClickUtil.hookView(btn);
    }
}
