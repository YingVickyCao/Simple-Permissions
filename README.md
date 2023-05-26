# SimplePermissions

[![](https://jitpack.io/v/yingvickycao/simplepermissions.svg)](https://jitpack.io/#yingvickycao/simplepermissions)


This library allows to request runtime permission.

# 1 Setup

To use this library your `minSdkVersion` must be >= Android 8 (API 26).

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.yingvickycao:simplepermissions:0.2'
}
```

Ref : https://jitpack.io/#yingvickycao/simplepermissions/v0.2

# 2 Usage

- Step 1 : Create a `SimplePermissions` instance :

```java
// where this is an Activity or Fragment instance
final SimplePermissions simplePermissions = new SimplePermissions(this);
```

**NOTE:**  
(1) `new SimplePermissions(this)` the `this` parameter can be a FragmentActivity or a Fragment.  
(2) When in a fragment, should use `new SimplePermissions(this)` instead of `(fragment.getActivity())`, or may have `java.lang.IllegalStateException: FragmentManager is already executing transactions`.

- Step 2 : Example : if want to request `Manifest.permission.RECORD_AUDIO`

```xml
<!-- First, Add RECORD_AUDIO permission to your AndroidManifest.xml -->
<uses-permission android:name="android.permission.RECORD_AUDIO"/>
```

```java
// Example 1 :// when request runtime permission, show rational to user when should

simplePermissions.request(SimplePermissions.SHOULD, new SimplePermissions.IPermissionCallback() {
    @Override
    public void showRationaleContextUI(SimplePermissions.OnRationaleClickListener rationaleOnClickListener) {
        // If needs show the rational context UI, override showRationaleContextUI to show your custom rational to user.
        // This is also Android recommended method: show rational to user when should.
        // disolay rational context UI to user
        Log.d(SimplePermissions.TAG, "showRationaleContextUI: ");
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("your title")
                .setMessage("your message")
                .setPositiveButton(getString(R.string.ok), (dialog, which) -> ationaleOnClickListener.onClick())
                .setNegativeButton(getString(R.string.cancel),null)
                .setNeutralButton(getString(R.string.skip), null)
                .create()
                .show();
    }

    @Override
    public void granted() {
        // TODO:  do your thing after granted permission
    }

    @Override
    public void denied() {
        // TODO:  do your thing after denied permission
        // Default it is no no need to implement denied method. You can also do something, such as shows a toast to notify user.
        Toast.makeText(this.getContext, "Gant failed", Toast.LENGTH_SHORT).show();
    }
}, Manifest.permission.RECORD_AUDIO);
```

# [Releases](./doc/Releases.md)

# [FAQ](./doc/FAQ.md)

# License

Copyright is ` Apache License`, see [LICENSE](./LICENSE),
and visit website http://www.apache.org/licenses/LICENSE-2.0
