package ying.cao.simplepermissions.example;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import ying.cao.simplepermissions.IGrantResult;
import ying.cao.simplepermissions.IRationaleClickListener;
import ying.cao.simplepermissions.IRequestResult;
import ying.cao.simplepermissions.SimplePermissions;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.requestRuntimePermission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void requestRuntimePermission() {
        SimplePermissions simplePermissions = new SimplePermissions(this);
        simplePermissions.request(new IRequestResult() {
            @Override
            public void showRationaleContextUI(IRationaleClickListener rationaleOnClickListener) {
                Log.d(SimplePermissions.TAG, "showRationaleContextUI: ");
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Request permission").setMessage("Permission Audio / SD")
                        .setPositiveButton(getString(R.string.ok), (dialog, which) -> rationaleOnClickListener.clickOK())
                        .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {})
                        .setNeutralButton(getString(R.string.skip), (dialog, which) -> {}).create().show();
            }

            @Override
            public void granted() {
                Toast.makeText(MainActivity.this, "Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void denied() {
                Toast.makeText(MainActivity.this, "Denied", Toast.LENGTH_SHORT).show();
            }
        }, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void requestRuntimePermission2() {
        SimplePermissions simplePermissions = new SimplePermissions(MainActivity.this);
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
        if (simplePermissions.isGranted(permissions)) {
            Toast.makeText(MainActivity.this, "Granted", Toast.LENGTH_SHORT).show();
            return;
        }
        if (simplePermissions.shouldShowRequestPermissionRationale(permissions)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Request permission")
                    .setMessage("Permission " + permissions.toString())
                    .setPositiveButton(getString(R.string.ok), (dialog, which) -> requestPermission(simplePermissions, permissions))
                    .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
                    })
                    .setNeutralButton(getString(R.string.skip), (dialog, which) -> {
                    })
                    .create().show();
        } else {
            requestPermission(simplePermissions, permissions);
        }
    }

    void requestPermission(SimplePermissions simplePermissions, final String... permissions) {
        simplePermissions.request(new IGrantResult() {
            @Override
            public void granted() {
                Toast.makeText(MainActivity.this, "Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void denied() {
                Toast.makeText(MainActivity.this, "Denied", Toast.LENGTH_SHORT).show();
            }
        }, permissions);

    }
}