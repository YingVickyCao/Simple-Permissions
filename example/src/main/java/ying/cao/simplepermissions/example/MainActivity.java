package ying.cao.simplepermissions.example;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import ying.cao.simplepermissions.SimplePermissions;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.requestRuntimePermission).setOnClickListener(v -> {
            requestRuntimePermission();
        });
    }

    private void requestRuntimePermission() {
        SimplePermissions simplePermissions = new SimplePermissions(this);
        simplePermissions.request(SimplePermissions.SHOULD, new SimplePermissions.IPermissionCallback() {
            @Override
            public void showRationaleContextUI(SimplePermissions.OnRationaleClickListener rationaleOnClickListener) {
                Log.d(SimplePermissions.TAG, "showRationaleContextUI: ");
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Request permission").setMessage("Permission Audio / SD")
                        .setPositiveButton(getString(R.string.ok), (dialog, which) -> rationaleOnClickListener.onClick())
                        .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
                        })
                        .setNeutralButton(getString(R.string.skip), (dialog, which) -> {
                        }).create().show();
            }

            @Override
            public void granted() {
                Toast.makeText(MainActivity.this, "Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void denied() {
                Toast.makeText(MainActivity.this, "Denied", Toast.LENGTH_SHORT).show();
            }
        }, Manifest.permission.RECORD_AUDIO);
    }
}