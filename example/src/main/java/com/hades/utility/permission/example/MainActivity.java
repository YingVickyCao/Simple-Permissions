package com.hades.utility.permission.example;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.hades.utility.permission.OnContextUIListener;
import com.hades.utility.permission.OnResultCallback;
import com.hades.utility.permission.PermissionsTool;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.requestRuntimePermission).setOnClickListener(v -> {
            requestRuntimePermission();
        });
    }

    private void requestRuntimePermission() {
        PermissionsTool permissionsTool = new PermissionsTool(this);
        permissionsTool.request(new String[]{Manifest.permission.RECORD_AUDIO}, new OnResultCallback() {
            @Override
            public void showInContextUI(OnContextUIListener listener) {
                Log.d(TAG, "showRationaleContextUI: ");
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Request permission").setMessage("Permission Audio / SD")
                        .setPositiveButton(getString(R.string.ok), (dialog, which) -> listener.ok())
                        .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
                            listener.cancel();
                        })
                        .setNeutralButton(getString(R.string.skip), (dialog, which) -> {
                            listener.cancel();
                        }).create().show();
            }

            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied() {
                Toast.makeText(MainActivity.this, "Denied", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRequestError(String message) {
                Log.e(TAG, "onError: " + message);
            }
        });
    }
}