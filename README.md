# SimplePermissions

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
    implementation 'ying.cao.permissions:SimplePermissions:0.1'
}
```

# 2 Usage

## 2.1 Create a `SimplePermissions` instance :

```java
// where this is an Activity or Fragment instance
final SimplePermissions simplePermissions = new SimplePermissions(this);
```

**NOTE:**

1. `new SimplePermissions(this)` the `this` parameter can be a FragmentActivity or a Fragment.<br/>

2) When in a fragment, should use `new SimplePermissions(this)` instead of `(fragment.getActivity())`, or may have `java.lang.IllegalStateException: FragmentManager is already executing transactions`.

## 2.2 Example : if want to request `Manifest.permission.RECORD_AUDIO`

```java
// Example 1 :// when request runtime permission, show rational to user when should

simplePermissions.request(new SimplePermissions.IPermissionCallback() {

    // If needs show the rational context UI, override showRationaleContextUI to show your custom rational to user.
    // This is also Android recommended method: show rational to user when should.
    @Override
    public void showRationaleContextUI(SimplePermissions.OnRationaleClickListener rationaleOnClickListener) {
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

```java
// when request runtime permission, you can  also use this way if want to show rational to user when should.
simplePermissions.request(SimplePermissions.SHOULD, new SimplePermissions.IPermissionCallback() {
    @Override
    public void showRationaleContextUI(SimplePermissions.OnRationaleClickListener rationaleOnClickListener) {
       // disolay rational context UI to user
    }

    @Override
    public void granted() {
        // TODO:  do your thing after granted permission
    }

    @Override
    public void denied() {
        // TODO:  do your thing after denied permission
    }
}, Manifest.permission.RECORD_AUDIO);
```

```java
// Example 2 : when request runtime permission, never show rationale context UI to user
simplePermissions.request(SimplePermissions.NEVER, new SimplePermissions.IPermissionCallback() {
    @Override
    public void granted() {
        // TODO:  do your thing after granted permission
    }

    @Override
    public void denied() {
        // TODO:  do your thing after denied permission
    }
}, Manifest.permission.RECORD_AUDIO);

```

```java
// Example 3 : when request runtime permission, always show rationale context UI to user
simplePermissions.request(SimplePermissions.ALWAYS, new SimplePermissions.IPermissionCallback() {
    @Override
    public void showRationaleContextUI(SimplePermissions.OnRationaleClickListener rationaleOnClickListener) {
       // disolay rational context UI to user
    }

    @Override
    public void granted() {
        // TODO:  do your thing after granted permission
    }

    @Override
    public void denied() {
        // TODO:  do your thing after denied permission
    }
}, Manifest.permission.RECORD_AUDIO);
```

```java
// Example 4 : You can also use simplePermissions step by step
private void requestRuntimePermission2() {
    SimplePermissions simplePermissions = new SimplePermissions(this);
    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
    if (simplePermissions.isGranted(permissions)) {
        Toast.makeText(MainActivity.this, "Granted", Toast.LENGTH_SHORT).show();
        return;
    }
    if (simplePermissions.shouldShowRequestPermissionRationale(permissions)) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activityContext);
        builder.setTitle("Request permission")
                .setMessage("Permission " + permissions.toString())
                .setPositiveButton(getString(R.string.ok), (dialog, which) -> requestPermission(simplePermissions, permissions))
                .setNegativeButton(getString(R.string.cancel), null)
                .setNeutralButton(getString(R.string.skip), null)
                .create().show();
    } else {
        requestPermission(simplePermissions, permissions);
    }
}

void requestPermission(SimplePermissions simplePermissions, final String... permissions) {
    simplePermissions.request(new SimplePermissions.IPurePermissionCallback() {
        @Override
        public void granted() {
            // TODO:  do your thing after granted permission
        }

        @Override
        public void denied() {
            // TODO:  do your thing after denied permission
        }
    }, permissions);
}
```

# License

Copyright is ` Apache License`, see [LICENSE](./LICENSE),
and visit website http://www.apache.org/licenses/LICENSE-2.0
