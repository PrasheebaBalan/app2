package com.example.app2;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_SMS = 123;
    EditText phonenumber, message;
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        send = findViewById(R.id.button);
        phonenumber = findViewById(R.id.editText);
        message = findViewById(R.id.editText2);

        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS);
                } else {
                    sendMessage();
                }
            }
        });
    }


    private void sendMessage() {
        String number = phonenumber.getText().toString();
        String msg = message.getText().toString();
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, msg, null, null);
            showNotification("Message Sent", "1 new message.");
        } catch (Exception e) {
            showNotification("Failed to send message", "Failed to send your message.");
        }
    }

    private void showNotification(String title, String message) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Message_Notification";
            CharSequence channelName = "Message Notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(channel);
        }

        Notification.Builder builder = new Notification.Builder(getApplicationContext(), "Message_Notification")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message);

        notificationManager.notify(0, builder.build());
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendMessage();
            } else {
                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}
