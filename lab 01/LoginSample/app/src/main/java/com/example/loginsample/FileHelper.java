package com.example.loginsample;

import android.content.Context;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {
    private static final String ACCOUNTS_FILE = "accounts.csv";

    public boolean saveAccount(Context context, AccountEntity account) {
        try {
            FileOutputStream fos = context.openFileOutput(ACCOUNTS_FILE, Context.MODE_APPEND);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            
            String csvLine = account.getUsername() + "," + 
                           account.getPassword() + "," + 
                           account.getEmail() + "," + 
                           account.getFullName() + "," + 
                           (account.getPhoneNumber() != null ? account.getPhoneNumber() : "") + "\n";
            
            writer.write(csvLine);
            writer.close();
            fos.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<AccountEntity> loadAccounts(Context context) {
        List<AccountEntity> accounts = new ArrayList<>();
        try {
            FileInputStream fis = context.openFileInput(ACCOUNTS_FILE);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    AccountEntity account = new AccountEntity();
                    account.setUsername(parts[0]);
                    account.setPassword(parts[1]);
                    account.setEmail(parts[2]);
                    account.setFullName(parts[3]);
                    if (parts.length > 4 && !parts[4].isEmpty()) {
                        account.setPhoneNumber(parts[4]);
                    }
                    accounts.add(account);
                }
            }
            
            reader.close();
            fis.close();
        } catch (Exception e) {
            // Archivo no existe o error, retorna lista vacía
        }
        return accounts;
    }
}