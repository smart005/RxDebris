# 自定义Toast消息提示
###### 系统主题
![](assets/15565301746917.jpg)
```java
public static void show(Context context, CharSequence message, int icon)
public static void show(Context context, CharSequence message)
public static void show(Context context, @StringRes int message, int icon)
public static void show(Context context, @StringRes int message)
```
###### error消息提示
![](assets/15565304364330.jpg)
```java
public static void showError(Context context, CharSequence message)
public static void showError(Context context, @StringRes int message)
```
###### success消息提示
![](assets/15565305074471.jpg)
```java
public static void showSuccess(Context context, CharSequence message)
public static void showSuccess(Context context, int message)
```
###### warning消息提示 
![](assets/15565305771485.jpg)
```java
public static void showWarning(Context context, CharSequence message)
public static void showWarning(Context context, @StringRes int message)
```