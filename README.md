# RedPointManager
## 效果图
![test](pic/test_red_point.gif)

## 介绍
Android红点显示解决方案。

## 使用方法

`build.gradle`中添加:
```
dependencies {
    compile 'com.xfy.redpoint:library:1.1'
}
```

代码中:
```
//在targetView右上角上显示红点
RedPointManager.bindActivity(activity).showRedPoint(targetView, marginRight, marginTop);

//在targetView左下角显示红点
RedPointManager.bindActivity(activity).showRedPoint(targetView, Gravity.LEFT|Gravity.BOTTOM, marginLeft, marginBottom);

//隐藏targetView上的红点
RedPointManager.bindActivity(activity).hideRedPoint(targetView);

//删除targetView上的红点
RedPointManager.bindActivity(activity).removeRedPoint(targetView);

//让红点以targetView中心点为中心旋转
RedPointManager.bindActivity(activity).rotateRedPoint(targetView, degree);

//让红点横向移动tx，纵向移动ty
RedPointManager.bindActivity(activity).translateRedPoint(targetView,tx, ty);

//重设红点，若view改变了位置，可调用此方法
RedPointManager.bindActivity(activity).resetRedPointBaseOnView(targetView);

//销毁
RedPointManager.unbindActivity(activity);
```

## 注意
请在View改变属性（translate、rotate、scale）前显示红点。
## 最后
欢迎提出意见及建议。

* email: s18810577589@sina.com