package com.example.sqlitep2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sqlitep2.data.dao.UserDao;
import com.example.sqlitep2.data.db.DatabaseManager;
import com.example.sqlitep2.data.model.User;

public class CresateUserActivity extends AppCompatActivity {
    DatabaseManager dbManager;
    private UserDao userDao;
    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextImageUrl;
    private Button buttonCreateUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cresate_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar vistas
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextImageUrl = findViewById(R.id.editTextImageUrl);
        buttonCreateUser = findViewById(R.id.buttonCreateUser);

        dbManager = DatabaseManager.getInstance(this);
        userDao = new UserDao(dbManager.openDatabase());

        buttonCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

        // Configurar listener para el botón "Regresar"
        Button buttonReturn = findViewById(R.id.buttonReturnU);
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cierra la actividad actual
            }
        });
    }

    private void createUser() {
        String username = editTextUsername.getText().toString();
        String email = editTextEmail.getText().toString();
        String imageUrl = editTextImageUrl.getText().toString();

        // Validar campos
        if (username.isEmpty() || email.isEmpty() || imageUrl.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear nuevo usuario
        User user = new User(username, email, imageUrl);

        // Insertar usuario en la base de datos
        long id = userDao.insert(user);

        if (id > 0) {
            Toast.makeText(this, "Usuario creado con éxito", Toast.LENGTH_SHORT).show();
            finish(); // Cierra la actividad actual
        } else {
            Toast.makeText(this, "Error al crear usuario", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbManager.closeDatabase();
    }
}