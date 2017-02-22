# ChangeableTextView

- 控件功能介绍（**可以作用于TextView单个文字**）
1，文本未设置时的加载动画
2，设置颜色（可以设置Alpha透明度）
3，设置字体大小
4，设置粗体
5，设置斜体
6，设置删除线
7，设置下划线
8，设置超链接
9，设置点击事件
10，一段文字是可以设置多种样式的，比如对一段文字同时设置粗体以及斜体以及下划线。
11，屏蔽关键字（设置多个需要屏蔽的文字以英文“，”分割，可以替换成自己想要的字符）
- 用法
layout：
```<lx.changeabletextview.ChangeableTextView
        android:paddingTop="5dp"
        android:paddingBottom="5dp"
        app:openAnim="true"//是否开启动画
        app:minLines="5"//动画作用的行数
        app:maskword="色" //需屏蔽的关键字 多个请用英文逗号分割
        app:replacetext="【bi~】"//关键字替换的文字
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:id="@+id/changeableTextView"
        />
```
class：
```
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
        },2000);
```
- 注意
1，先设置文字，再去配置颜色以及各种属性。
2，所有操作只对上一次设置的文本片段生效。
