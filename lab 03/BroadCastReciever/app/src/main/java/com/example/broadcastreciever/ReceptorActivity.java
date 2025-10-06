package com.example.broadcastreciever;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ReceptorActivity extends AppCompatActivity implements MessageReceiver.MessageListener {
    private static final String TAG = "ReceptorActivity";
    private TextView messageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receptor);
        
        messageTextView = findViewById(R.id.messageTextView);
        Log.d(TAG, "ReceptorActivity creada");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Registrando listener para mensajes");
        MessageReceiver.setMessageListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Desregistrando listener para mensajes");
        MessageReceiver.removeMessageListener();
    }

    @Override
    public void onMessageReceived(String message) {
        Log.d(TAG, "Mensaje recibido en la actividad: " + message);
        runOnUiThread(() -> {
            messageTextView.setText("Mensaje recibido: " + message);
            Log.d(TAG, "UI actualizada con el mensaje: " + message);
        });
    }
}