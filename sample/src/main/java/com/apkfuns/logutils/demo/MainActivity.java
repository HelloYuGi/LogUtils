package com.apkfuns.logutils.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;

import com.apkfuns.logutils.LogUtils;
import com.apkfuns.logutils.demo.model.Child;
import com.apkfuns.logutils.demo.helper.DataHelper;
import com.apkfuns.logutils.demo.model.FakeBounty;
import com.apkfuns.logutils.demo.model.Man;
import com.apkfuns.logutils.demo.model.MulObject;
import com.apkfuns.logutils.demo.model.Person;
import com.apkfuns.logutils.demo.service.MultiProcessService;
import com.apkfuns.logutils.demo.utils.ThreadLog;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import hugo.weaving.DebugLog;


public class MainActivity extends Activity implements View.OnClickListener {

    @DebugLog
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewGroup container = findViewById(R.id.container);
        for (int i = 0; i < container.getChildCount(); i++) {
            container.getChildAt(i).setTag(i);
            container.getChildAt(i).setOnClickListener(this);
        }
        // 进行多进程测试
        startService(new Intent(this, MultiProcessService.class));
    }

    @Override
    public void onClick(View v) {
        switch (((int) v.getTag())) {
            case 0:
                XLog.d("12345!!");
                break;
            case 1:
                LogUtils.i(DataHelper.getBigString(this));
                break;
            case 2:
                new ThreadLog("thread1", "msg from 1").start();
                new ThreadLog("thread2", "msg from 2").start();
                new ThreadLog("thread3", "msg from 3").start();
                break;
            case 3:
                LogUtils.getLog2FileConfig().flushAsync();
                break;
            default:
                break;
        }
    }

    static class XLog {
        static void d(String msg) {
            LogUtils.d(msg);
        }
    }

    @DebugLog
    private void log() {
        LogUtils.d("12345");
        LogUtils.d("12%s3%s45", "a", "b");
        LogUtils.d(new NullPointerException("12345"));
        LogUtils.d(DataHelper.getObject());
        LogUtils.d(null);

        LogUtils.json(DataHelper.getJson());
        // 打印List
        LogUtils.d(DataHelper.getStringList());

// 支持数据集合
        LogUtils.d(DataHelper.getObjectList());
//
////        // 支持map输出
        LogUtils.d(DataHelper.getObjectMap());

        // Bundle支持
        Bundle bundle = new Bundle();
        bundle.putInt("abc", 1);
        bundle.putString("abc2", "text");
        bundle.putSerializable("abc3", DataHelper.getObject());
        bundle.putStringArray("abc4", DataHelper.getStringArray());
        bundle.putSerializable("abc7", DataHelper.getStringArray2());
        bundle.putSerializable("abc8", DataHelper.getStringArray3());
        bundle.putSerializable("abc5", DataHelper.getObject());
        bundle.putSerializable("abc6", DataHelper.getObjectArray());
        bundle.putSerializable("abc9", DataHelper.getStringMap());
        bundle.putSerializable("abc10", DataHelper.getBigString(this));
        LogUtils.e(bundle);


        // 对象测试
        LogUtils.e(DataHelper.getMan());
        LogUtils.e(DataHelper.getObject());
        LogUtils.e(DataHelper.getOldMan());

        new Thread(new Runnable() {
            @Override
            public void run() {
                LogUtils.e("run on new Thread()");
                LogUtils.tag("自定义tag888 (*^__^*)")
                        .d("我是自定义tag O(∩_∩)O哈哈~");
            }
        }).start();


        // 大文本输出
        LogUtils.e(DataHelper.getBigString(this));

        // Intent测试
        Intent it = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        it.putExtra("aaaa", "12345");
        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        LogUtils.d(it);


        Person p = DataHelper.getObject();
        WeakReference<Person> wp = new WeakReference<Person>(p);
        LogUtils.e(wp);

        SoftReference<Person> sr = new SoftReference<Person>(p);
        LogUtils.e(sr);

        List<WeakReference> l = new ArrayList<>();
        l.add(wp);
        l.add(wp);
        l.add(wp);
        LogUtils.e(l);

        LogUtils.tag("自定义tag (*^__^*)")
                .d("我是自定义tag O(∩_∩)O哈哈~");

        Child<Man> child = new Child<>("张三");
        child.setParent(DataHelper.getMan());
//        LogUtils.d(child);

        LogUtils.d(new MulObject(5));

        LogUtils.tag("自定义tag2345 (*^__^*)")
                .d("我是自定义tag O(∩_∩)O哈哈~");

        LogUtils.d(DataHelper.getNode("左结点", "右结点"));

        LogUtils.d("12345678910");


        LogUtils.tag("lalal").xml(DataHelper.getXml());

        LogUtils.tag("lalal").e(new FakeBounty());

        // 测试Message
        Message message = new Message();
        message.setData(new Bundle());
        message.obj = new Man(20);
        message.arg1 = 1;
        message.arg2 = 2;
        message.what = 1232;
        LogUtils.e(message);

        LogUtils.d(Message.obtain());

        LogUtils.getLogConfig().configAllowLog(true);
        LogUtils.e("test 12345#");
    }
}
