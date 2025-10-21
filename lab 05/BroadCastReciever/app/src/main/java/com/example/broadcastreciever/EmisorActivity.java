package com.example.broadcastreciever;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class EmisorActivity extends AppCompatActivity {

    private EditText editTextMessage;
    private Button buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emisor);

        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBroadcast();
            }
        });
    }

    private void sendMessage() {
        String message = editTextMessage.getText().toString().trim();
        
        if (!message.isEmpty()) {
            Intent intent = new Intent(MessageReceiver.ACTION_MESSAGE_SENT);
            intent.putExtra("message", message);
            intent.setPackage(getPackageName()); // Ensure broadcast stays within the app
            
            sendBroadcast(intent);
            Log.d(TAG, "Mensaje enviado: " + message);
            
            // Clear the input field
            editTextMessage.setText("");
        }
    }
}