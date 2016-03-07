# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\user\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# Enable our custom annotations at keep package
#-keep class hk.mapp.zfmemo.util.keep.*
#-keepnames @hk.mapp.zfmemo.util.keep.KeepName class *
#-keepclassmembers @hk.mapp.zfmemo.util.keep.KeepMethods class * {
#    <methods>;
#}
#-keepclassmembernames @hk.mapp.zfmemo.util.keep.KeepFieldNames class * {
#    <fields>;
#}

# Strip debug logs
-assumenosideeffects class hk.mapp.scaffold.core.base.Log {
    public *** debug(...);
}


-dontwarn sun.misc.Unsafe # Gson/Rx
-keepattributes Signature # Gson/Retrofit
-dontwarn org.luaj.** # LuaJ

# Retrolambda
-dontwarn java.lang.invoke.*

# Guava
-dontwarn javax.annotation.**
-dontwarn com.google.j2objc.annotations.Weak
-dontwarn java.lang.ClassValue

# Retrofit, still too verbose
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepattributes Exceptions
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}
-keepnames class rx.Single # adapter use
# Okio
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Rx
-keepclassmembernames class rx.internal.util.unsafe.** {
    <fields>;
}

# Jackson
-dontwarn com.fasterxml.jackson.databind.**
-dontwarn com.fasterxml.jackson.dataformat.yaml.snakeyaml.**
-dontwarn org.yaml.snakeyaml.**


# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
