# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
#-dontpreverify
-dontwarn
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

# android proguard -------------------------------------
-keep public class * extends android.app.Application
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.app.Fragment
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.support.v4.view.ViewPager
-keep public class * extends android.os.HandlerThread

#模型识别
-keep class com.innovationai.pigweight.camera.**{*;}
-keep class com.innovationai.pigweight.event.**{*;}
-keep class com.innovationai.pigweight.net.bean.**{*;}
-keep class com.innovationai.pigweight.utils.**{*;}

-keep class com.innovationai.pigweight.AppConfig {*;}
-keep class com.innovationai.pigweight.Constant {*;}
-keep class com.innovationai.pigweight.activitys.SplashActivity {
    public static <fields>;
    public static <methods>;
}

#权限
-keep class com.apeng.permissions.**{*;}

#删除日志
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String,int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

# okhttp 不混淆
-keep class okhttp3.**{*;}
-keep class retrofit2.**{*;}
-keep class io.reactivex.**{*;}
-keep class org.hamcrest.**{*;}
-keep class org.reactivestreams.**{*;}

#避免混淆泛型 如果混淆报错建议关掉
-keepattributes Signature

-keepattributes Exceptions,InnerClasses

#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}

-keepattributes Exceptions,InnerClasses
# Keep names - Native method names. Keep all native class/method names.
-keepclasseswithmembers,allowshrinking class * {
    native <methods>;
}

-keepclasseswithmembers,allowshrinking class * {
    public <init>(android.content.Context,android.util.AttributeSet);
}

-keepclasseswithmembers,allowshrinking class * {
    public <init>(android.content.Context,android.util.AttributeSet,int);
}


-keepclassmembers enum  * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * extends android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

-keep public class * extends android.widget.TextView


-keep class com.alipay.android.app.** {
    public <fields>;
    public <methods>;
}

 -keepnames class * implements android.os.Parcelable {
     public static final ** CREATOR;
}

-keep class com.linkedin.** { *; }
-keepattributes Signature


-keep class **$Properties


# If you do not use RxJava:
-dontwarn rx.**

# eventbus proguard ---------------------------------------
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}


# jjwt proguard -------------------------------------------
-keepnames class com.fasterxml.jackson.databind.** { *; }
-dontwarn com.fasterxml.jackson.databind.*
-keepattributes InnerClasses

-keep class org.bouncycastle.** { *; }
-keepnames class org.bouncycastle.* { *; }
-dontwarn org.bouncycastle.*

-keep class io.jsonwebtoken.** { *; }
-keepnames class io.jsonwebtoken.* { *; }
-keepnames interface io.jsonwebtoken.* { *; }

-dontwarn javax.xml.bind.DatatypeConverter
-dontwarn io.jsonwebtoken.impl.Base64Codec

-keepnames class com.fasterxml.jackson.** { *; }
-keepnames interface com.fasterxml.jackson.** { *; }

#org.apache.http.legacy.jar
-dontwarn android.net.compatibility.**
-dontwarn android.net.http.**
-dontwarn com.android.internal.http.multipart.**
-dontwarn org.apache.commons.**
-dontwarn org.apache.http.**
-keep class android.net.compatibility.**{*;}
-keep class android.net.http.**{*;}
-keep class com.android.internal.http.multipart.**{*;}
-keep class org.apache.commons.**{*;}
-keep class org.apache.http.**{*;}

# JPush proguard ---------------------------------------
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keepattributes Annotation

# gson proguard ----------------------------------------
-dontwarn com.google.**
-keep class com.google.gson.** {*;}

 #高德地图定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

-keep class android.support.** {*;}
-dontwarn android.support.**

# end of android support
##########################################################################


##########################################################################
# annotation.

-keep public class android.annotation.** { *; }
-dontwarn android.annotation.**

# end of annotation
##########################################################################


##########################################################################
# Gson
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.google.gson.** { *;}
-dontwarn com.google.gson.**

## end of Gson
##########################################################################

##########################################################################
# google-play-service

-dontwarn com.google.**.R
-dontwarn com.google.**.R$*

-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keep class com.jdpaysdk.author.protocol.** { *; }

-keep class com.nineoldandroids.**{*;}
-dontwarn com.nineoldandroids.**

#okio
-dontwarn okio.**
-keep class okio.**{*;}
-keep interface okio.**{*;}

#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}
-keep interface okhttp3.**{*;}


