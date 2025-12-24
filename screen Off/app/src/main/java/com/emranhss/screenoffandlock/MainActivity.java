package com.emranhss.screenoffandlock;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private ComponentName cn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        DevicePolicyManager dmp = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        cn = new ComponentName(this, MyDeviceAdminReceiver.class);
        //Ekhane likhle app click korle screen of hoye jabe.
        toLock();
        finish();
        if (dmp.isAdminActive(cn)) {
            dmp.lockNow();
            finish();
        }

        findViewById(R.id.btnLockScreen).setOnClickListener(v -> {
            //Ekhane likhle app er vetor dhuke button click korle screen of hoye jabe.
//            toLock();
//            finish();
//            if (dmp.isAdminActive(cn)) {
//                dmp.lockNow();
//                finish();
//            }


        });

    } //oncreat end

    private void toLock() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, cn);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Now");
        startActivity(intent);
    }

} //main end