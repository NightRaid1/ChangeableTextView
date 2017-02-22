package lx.changeabletextview;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private ChangeableTextView changeableTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeableTextView = (ChangeableTextView) findViewById(R.id.changeableTextView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                changeableTextView.addText("我是红色文本").setColor(Color.RED)
                        .addText("我是绿色的大号文本").setColor("#FF00FF00").setTextSize(30)
                        .addText("我是粗体").setBold()
                        .addText("我是斜体").setItalic()
                        .addText("我拥有删除线并且我很大").setTextSize(33).setDeleteLine()
                        .addText("我有下划线并且我是蓝色的并且我很大~").setUnderline().setColor(Color.BLUE).setTextSize(25)
                        .addText("我是一个超链接").setHyperLinks("https://www.baidu.com",null).setColor("#aabbff")
                        .addText("我拥有点击事件~")
                        .setOnTextClickListener(new OnTextClickListener() {
                            @Override
                            public void onClick(View widget) {
                                Toast.makeText(MainActivity.this,"我拥有点击事件~",Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public int getColor() {
                                return Color.RED;
                            }
                        }).setTextSize(34);
            }
        },10000);

    }
}
