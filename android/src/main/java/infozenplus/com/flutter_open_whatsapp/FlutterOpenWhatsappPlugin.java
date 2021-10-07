package infozenplus.com.flutter_open_whatsapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.app.Activity;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** FlutterOpenWhatsappPlugin */
public class FlutterOpenWhatsappPlugin implements MethodCallHandler, FlutterPlugin, ActivityAware {

  Activity activity;
  MethodChannel methodChannel;
  private Context context;

  /** Plugin registration. */
  public FlutterOpenWhatsappPlugin() {
      // All Android plugin classes must support a no-args
      // constructor. A no-arg constructor is provided by
      // default without declaring one, but we include it here for
      // clarity.
      //
      // At this point your plugin is instantiated, but it
      // isn't attached to any Flutter experience. You should not
      // attempt to do any work here that is related to obtaining
      // resources or manipulating Flutter.
  }

  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_open_whatsapp");
    channel.setMethodCallHandler(new FlutterOpenWhatsappPlugin(registrar.activity(), channel));
  }

  public FlutterOpenWhatsappPlugin(Activity activity, MethodChannel methodChannel) {
    this.activity = activity;
    this.methodChannel = methodChannel;
    this.methodChannel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else if(call.method.equalsIgnoreCase("sendSingleMessage")) {

      PackageManager packageManager = activity.getPackageManager();
      Intent i = new Intent(Intent.ACTION_VIEW);
      try {
        String mobileNo = call.argument("mobileNo");
        String message = call.argument("message");
        //https://wa.me/919167370647?text=Yes%20We'll%20do%20this%20in%20frag4%20inOCW
        String url = "https://wa.me/" + mobileNo.trim() + "?text=" + message.trim();
        i.setPackage("com.whatsapp");
        i.setData(Uri.parse(url));
        if (i.resolveActivity(packageManager) != null) {
            activity.startActivity(i);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      result.notImplemented();
    }
  }

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        methodChannel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "flutter_open_whatsapp");
        methodChannel.setMethodCallHandler(this);
        context= flutterPluginBinding.getApplicationContext();
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        methodChannel.setMethodCallHandler(null);
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding activityPluginBinding) {
        activity= activityPluginBinding.getActivity();
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        activity=null;
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding activityPluginBinding) {
        activity = activityPluginBinding.getActivity();
    }

    @Override
    public void onDetachedFromActivity() {
        activity=null;
    }
}
