package com.example.broadcastreciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MessageReceiver extends BroadcastReceiver {
    private static final String TAG = "MessageReceiver";
    public static final String ACTION_MESSAGE_SENT = "com.example.broadcastreciever.MESSAGE_SENT";
    
    // Interface para notificar a las actividades
    public interface MessageListener {
        void onMessageReceived(String message);
    }
    
    private static MessageListener listener;
    
    public static void setMessageListener(MessageListener listener) {
        MessageReceiver.listener = listener;
    }
    
    public static void removeMessageListener() {
        MessageReceiver.listener = null;
    }
    
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Broadcast recibido con acción: " + intent.getAction());
        
        if (ACTION_MESSAGE_SENT.equals(intent.getAction())) {
            String message = intent.getStringExtra("message");
            Log.d(TAG, "Mensaje recibido: " + message);
            
            if (message != null) {
                // Notificar al listener si está disponible
                if (listener != null) {
                    Log.d(TAG, "Notificando al listener");
                    listener.onMessageReceived(message);
                } else {
                    Log.w(TAG, "No hay listener registrado para recibir el mensaje");
                }
            } else {
                Log.w(TAG, "El mensaje recibido es null");
            }
        }
    }
}