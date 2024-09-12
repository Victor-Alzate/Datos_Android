package com.example.sqlitep2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sqlitep2.data.dao.ProductDao;
import com.example.sqlitep2.data.db.DatabaseManager;
import com.example.sqlitep2.data.model.Product;

public class CreateProductActivity extends AppCompatActivity {
    private DatabaseManager dbManager;
    private ProductDao productDao;
    private EditText editTextProductName;
    private EditText editTextProductPrice;
    private Button buttonCreateProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        // Inicializar vistas
        editTextProductName = findViewById(R.id.editTextProductName);
        editTextProductPrice = findViewById(R.id.editTextProductPrice);
        buttonCreateProduct = findViewById(R.id.buttonCreateProduct);

        // Inicializar DatabaseManager y ProductDao
        dbManager = DatabaseManager.getInstance(this);
        productDao = new ProductDao(dbManager.openDatabase());

        // Configurar botón para crear producto
        buttonCreateProduct.setOnClickListener(v -> createProduct());

        // Configurar botón de regreso
        Button buttonReturn = findViewById(R.id.buttonReturnPro);
        buttonReturn.setOnClickListener(v -> finish());
    }

    private void createProduct() {
        String name = editTextProductName.getText().toString();
        String priceString = editTextProductPrice.getText().toString();

        // Validar campos
        if (name.isEmpty() || priceString.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Precio inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear nuevo producto
        Product product = new Product(name, price);

        // Insertar producto en la base de datos
        long id = productDao.insert(product);

        if (id > 0) {
            Toast.makeText(this, "Producto creado con éxito", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK); // Indica que se ha creado un producto exitosamente
            finish(); // Cierra la actividad actual
        } else {
            Toast.makeText(this, "Error al crear producto", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbManager.closeDatabase();
    }
}
