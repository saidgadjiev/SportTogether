-target 1.7

#-dontobfuscate
#-dontoptimize
-verbose
#-ignorewarnings

# print mapping
-printmapping proguard-mapping.txt

#-optimizations !field/removal/writeonly,!field/marking/private,!code/allocation/variable
-optimizationpasses 2

#-keepattributes SourceFile, LineNumberTable, EnclosingMethod, InnerClasses
#-keep,allowshrinking,allowoptimization class * { <methods>; }

-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}


-keep class sun.misc.Unsafe { *; }


-keep enum * {
	*;
}

-keep class * implements android.os.Parcelable {
	*;
}
-keep class * implements java.io.Serializable {
	*;
}

-keep class * implements Parcelable {
    *;
}


-keepnames class * implements android.os.Parcelable {
 public static final ** CREATOR;
}

-keep class * extends java.util.ListResourceBundle {
 protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
 public static final *** NULL;
}

-keepnames class * implements android.os.Parcelable {
 public static final ** CREATOR;
}

-keepclassmembers class * implements android.os.Parcelable {
 android.os.Parcelable$Creator CREATOR;
}

## Support library
-dontwarn android.support.v7.**
#-keep class android.support.v7.** { *; }
#-keep interface android.support.v7.** { *; }

#-keep class android.support.v4.** { *; }
#-dontwarn android.support.v4.**
#-keep class android.support.v4.** { *; }
#-keep interface android.support.v4.app.** { *; }
#-keep public class * extends android.support.v4.**
#-keep public class * extends android.app.Fragment

# support design
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }

-dontwarn org.slf4j.**

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep class * extends android.view.View
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.Service
-keep public class * implements android.os.IInterface
-keep class * extends android.content.BroadcastReceiver
-keepattributes *Annotation*


#Warnings to be removed. Otherwise maven plugin stops, but not dangerous
-dontwarn android.support.**
-dontwarn com.sun.xml.internal.**
-dontwarn com.sun.istack.internal.**
-dontwarn javax.security.**
-dontwarn javax.xml.**
-dontwarn java.util.**
-dontwarn org.w3c.dom.**
-dontwarn com.google.common.**

## Gson SERIALIZER SETTINGS
# See https://code.google.com/p/google-gson/source/browse/trunk/examples/android-proguard-example/proguard.cfg
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-dontwarn sun.misc.Unsafe


# rxjava
-dontwarn sun.misc.Unsafe
-dontwarn rx.**
-dontwarn com.squareup.okhttp.*

#retrofit

-keep class com.squareup.okhttp.** { *; }
#-keep class retrofit.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-keep class com.google.appengine.api.urlfetch.** { *; }
-dontwarn com.google.appengine.api.urlfetch.*
-dontwarn org.codehaus.mojo.**
-dontwarn java.nio.file.*

-keep class com.google.android.gms.** { *; }

-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

#-dontwarn com.google.android.gms.iid.InstanceID
#-dontwarn com.google.android.gms.gcm.GoogleCloudMessaging

# If in your rest service interface you use methods with Callback argument.
-keepattributes Exceptions

# If your rest service methods throw custom exceptions, because you've defined an ErrorHandler.
-keepattributes Signature

-keepattributes *Annotation*,Signature

-dontnote junit.framework.**
-dontnote junit.runner.**

#-dontwarn android.test.**
#-dontwarn android.support.test.**
#-dontwarn org.junit.**
#-dontwarn org.hamcrest.**


#Rx
-keep interface rx.observers.Subscribers.**  { *; }
-keep class rx.observers.Subscribers.**  { *; }

-dontwarn sun.misc.**

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}


-dontwarn android.databinding.**
-keep class android.databinding.** { *; }

-keep class com.google.android.gms.flags.impl.** { *; }
-keep class kotlin.io.** { *; }

-keep class android.support.v4.app.** { *; }
-keep class android.support.v4.view.** { *; }
-keep class android.support.v4.media.** { *; }
-keep class android.support.v7.widget.** { *; }
-keep class kotlin.text.** { *; }

-keep class rx.observers.** { *; }
-keep interface rx.observers.*

-keep class rx.internal.operators.OperatorElementAt { *; }
-keep class kotlin.collections.CollectionsKt*

-keep class rx.functions.Action*
-keep interface rx.functions.Action*


-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

-keep class android.support.v4.animation.ValueAnimatorCompat
-keep class android.support.v4.content.Loader
-keep class android.support.v4.util.ArrayMap
-keep class android.support.v4.util.SimpleArrayMap
-keep class android.support.v7.view.menu.MenuPresenter*
-keep class android.support.v7.view.menu.MenuView*
-keep class okio.Sink
-keep class com.google.android.gms.internal.** { *; }
-keep class android.content.Context,com.google.android.gms.common.** { *; }


-keep class rx.** { *; }
-keep class kotlin.jvm.functions.** { *; }
-keep interface kotlin.jvm.functions.** { *; }

-keep class com.facebook.** { *; }

# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions