package lqf.com.hookdemo;

import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;

public class HookViewClickUtil {

    public static HookViewClickUtil getInstance() {
        return UtilHolder.mHookViewClickUtil;
    }

    private static class UtilHolder {
        private static HookViewClickUtil mHookViewClickUtil = new HookViewClickUtil();
    }

    public static void hookView(View view) {
        try {
            Class viewClazz = Class.forName("android.view.View");
            //反射获取私有的方法
            Method listenerInfoMethod = viewClazz.getDeclaredMethod("getListenerInfo");
            if (!listenerInfoMethod.isAccessible()) {
                //强制访问
                listenerInfoMethod.setAccessible(true);
            }
            //获取ListenerInfo对象
            Object listenerInfoObj = listenerInfoMethod.invoke(view);
             //获取ListenerInfo的.class
            Class listenerInfoClazz = Class.forName("android.view.View$ListenerInfo");
            //获取ListenerInfo的mOnClickListener成员变量
            Field onClickListenerField = listenerInfoClazz.getDeclaredField("mOnClickListener");

            if (!onClickListenerField.isAccessible()) {
                onClickListenerField.setAccessible(true);
            }
            //获取ListenerInfo的mOnClickListener成员变量的值
            View.OnClickListener mOnClickListener = (View.OnClickListener) onClickListenerField.get(listenerInfoObj);
            //自定义代理事件监听器
            View.OnClickListener onClickListenerProxy = new OnClickListenerProxy(mOnClickListener);
            //更换mOnClickListener成员变量的值为我们的代理对象
            onClickListenerField.set(listenerInfoObj, onClickListenerProxy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //自定义的代理事件监听器
    private static class OnClickListenerProxy implements View.OnClickListener {

        private View.OnClickListener object;

        private int MIN_CLICK_DELAY_TIME = 1000;

        private long lastClickTime = 0;

        private OnClickListenerProxy(View.OnClickListener object) {
            this.object = object;
        }

        @Override
        public void onClick(View v) {
            //点击时间控制
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                if (object != null) object.onClick(v);
                Log.e("lqf", "onClick: "+lastClickTime);
            }
        }
    }
}