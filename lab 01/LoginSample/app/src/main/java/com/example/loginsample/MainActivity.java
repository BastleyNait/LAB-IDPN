package com.example.loginsample;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.loginsample.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private List<AccountEntity> registeredAccounts;
    private ActivityResultLauncher<Intent> accountActivityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        
        // Inicializar lista de cuentas registradas
        registeredAccounts = new ArrayList<>();
        loadRegisteredAccounts();
        
        // Configurar ActivityResultLauncher
        setupActivityResultLauncher();
        
        // Configurar vistas y listeners
        setupViews();
    }
    
    private void setupActivityResultLauncher() {
        accountActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            boolean success = data.getBooleanExtra("success", false);

                            
                            if (success) {
                                AccountEntity newAccount = (AccountEntity) data.getSerializableExtra("account");
                                if (newAccount != null) {
                                    registeredAccounts.add(newAccount);
                                    String message =  "Registro invalido";
                                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    } else if (result.getResultCode() == RESULT_CANCELED) {
                        Intent data = result.getData();
                        if (data != null) {
                            String message =  "Registro cancelado";
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        );
    }
    
    private void setupViews() {
        EditText edtUsername = binding.edtUsername;
        EditText edtPassword = binding.edtPassword;
        Button btnLogin = binding.btnLogin;
        Button btnRegister = binding.btnRegister;
        
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString();
                
                if (validateLogin(username, password)) {
                    Toast.makeText(MainActivity.this, "Bienvenido a mi app", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Login exitoso para: " + username);
                } else {
                    Toast.makeText(MainActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Login fallido para: " + username);
                }
            }
        });
        
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                accountActivityLauncher.launch(intent);
            }
        });
    }
    
    private boolean validateLogin(String username, String password) {
        // Verificar credenciales por defecto
        if (username.equals("admin") && password.equals("admin")) {
            return true;
        }
        
        // Verificar cuentas registradas
        for (AccountEntity account : registeredAccounts) {
            if (account.getUsername().equals(username) && account.getPassword().equals(password)) {
                return true;
            }
        }
        
        return false;
    }
    
    private void loadRegisteredAccounts() {
        FileHelper fileHelper = new FileHelper();
        registeredAccounts = fileHelper.loadAccounts(this);
    }
}