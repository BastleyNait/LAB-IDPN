package com.example.loginsample;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginsample.databinding.ActivityAccountBinding;
import com.google.android.material.textfield.TextInputEditText;

public class AccountActivity extends AppCompatActivity {
    private ActivityAccountBinding binding;
    private TextInputEditText edtFullName, edtUsername, edtEmail, edtPhone, edtPassword, edtConfirmPassword;
    private Button btnRegister, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        edtFullName = binding.edtFullName;
        edtUsername = binding.edtUsername;
        edtEmail = binding.edtEmail;
        edtPhone = binding.edtPhone;
        edtPassword = binding.edtPassword;
        edtConfirmPassword = binding.edtConfirmPassword;
        btnRegister = binding.btnRegister;
        btnCancel = binding.btnCancel;
    }

    private void setupClickListeners() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelRegistration();
            }
        });
    }

    private void registerUser() {
        String fullName = edtFullName.getText().toString().trim();
        String username = edtUsername.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String password = edtPassword.getText().toString();
        String confirmPassword = edtConfirmPassword.getText().toString();

        // Validaciones
        if (!validateInputs(fullName, username, email, password, confirmPassword)) {
            return;
        }

        // Crear objeto AccountEntity
        AccountEntity newAccount = new AccountEntity(username, password, email, fullName, phone);

        // Guardar cuenta usando FileHelper
        if (saveAccount(newAccount)) {
            // Preparar resultado para devolver a MainActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("account", newAccount);
            resultIntent.putExtra("success", true);
            resultIntent.putExtra("message", "Cuenta creada exitosamente");
            
            setResult(RESULT_OK, resultIntent);
            Toast.makeText(this, "Cuenta registrada exitosamente", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al guardar la cuenta", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs(String fullName, String username, String email, String password, String confirmPassword) {
        // Validar nombre completo
        if (TextUtils.isEmpty(fullName)) {
            binding.tilFullName.setError("El nombre completo es requerido");
            edtFullName.requestFocus();
            return false;
        } else {
            binding.tilFullName.setError(null);
        }

        // Validar username
        if (TextUtils.isEmpty(username)) {
            binding.tilUsername.setError("El nombre de usuario es requerido");
            edtUsername.requestFocus();
            return false;
        } else if (username.length() < 3) {
            binding.tilUsername.setError("El nombre de usuario debe tener al menos 3 caracteres");
            edtUsername.requestFocus();
            return false;
        } else {
            binding.tilUsername.setError(null);
        }

        // Validar email
        if (TextUtils.isEmpty(email)) {
            binding.tilEmail.setError("El correo electrónico es requerido");
            edtEmail.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.setError("Ingrese un correo electrónico válido");
            edtEmail.requestFocus();
            return false;
        } else {
            binding.tilEmail.setError(null);
        }

        // Validar contraseña
        if (TextUtils.isEmpty(password)) {
            binding.tilPassword.setError("La contraseña es requerida");
            edtPassword.requestFocus();
            return false;
        } else if (password.length() < 6) {
            binding.tilPassword.setError("La contraseña debe tener al menos 6 caracteres");
            edtPassword.requestFocus();
            return false;
        } else {
            binding.tilPassword.setError(null);
        }

        // Validar confirmación de contraseña
        if (TextUtils.isEmpty(confirmPassword)) {
            binding.tilConfirmPassword.setError("Confirme su contraseña");
            edtConfirmPassword.requestFocus();
            return false;
        } else if (!password.equals(confirmPassword)) {
            binding.tilConfirmPassword.setError("Las contraseñas no coinciden");
            edtConfirmPassword.requestFocus();
            return false;
        } else {
            binding.tilConfirmPassword.setError(null);
        }

        return true;
    }

    private boolean saveAccount(AccountEntity account) {
        FileHelper fileHelper = new FileHelper();
        return fileHelper.saveAccount(this, account);
    }

    private void cancelRegistration() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("success", false);
        resultIntent.putExtra("message", "Registro cancelado");
        
        setResult(RESULT_CANCELED, resultIntent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}