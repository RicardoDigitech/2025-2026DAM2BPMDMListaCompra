package ricardosornosa.a2025_2026dam2bpmdmlistacompra;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import ricardosornosa.a2025_2026dam2bpmdmlistacompra.adapters.ProductoAdapter;
import ricardosornosa.a2025_2026dam2bpmdmlistacompra.databinding.ActivityMainBinding;
import ricardosornosa.a2025_2026dam2bpmdmlistacompra.models.ProductoModel;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayList<ProductoModel> productoList;
    private ProductoAdapter adapter;
    private RecyclerView.LayoutManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        productoList = new ArrayList<>();
        adapter = new ProductoAdapter(
                productoList, R.layout.producto_view_model, this
        );
        lm = new LinearLayoutManager(this);

        binding.contentMain.contenedorMain.setAdapter(adapter);
        binding.contentMain.contenedorMain.setLayoutManager(lm);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearProducto().show();
            }
        });
    }

    private AlertDialog crearProducto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.titulo_alert_crear);
        builder.setCancelable(false);

        View productoViewAlert = LayoutInflater.from(this).inflate(R.layout.producto_view_alert, null);
        EditText txtNombre = productoViewAlert.findViewById(R.id.txtNombreProductoViewAlert);
        EditText txtCantidad = productoViewAlert.findViewById(R.id.txtCantidadProductoViewAlert);
        EditText txtImporte = productoViewAlert.findViewById(R.id.txtImporteProductoViewAlert);
        TextView lblTotal = productoViewAlert.findViewById(R.id.lblTotalProductoViewAlert);
        builder.setView(productoViewAlert);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int cantidad = Integer.parseInt(txtCantidad.getText().toString());
                    float importe = Float.parseFloat(txtImporte.getText().toString());
                    float total = cantidad * importe;

                    NumberFormat nf = NumberFormat.getCurrencyInstance();
                    lblTotal.setText(nf.format(total));
                } catch (Exception e) {
                    Toast.makeText(
                            MainActivity.this,
                            e.getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };

        txtCantidad.addTextChangedListener(textWatcher);
        txtImporte.addTextChangedListener(textWatcher);

        builder.setNegativeButton(R.string.negativo_alert_crear, null);
        builder.setPositiveButton(R.string.positivo_alert_crear, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nombre = txtNombre.getText().toString();
                String cantidadS = txtCantidad.getText().toString();
                String importeS = txtImporte.getText().toString();

                if (!nombre.isEmpty() && !cantidadS.isEmpty() && !importeS.isEmpty()) {
                    ProductoModel p = new ProductoModel(
                            nombre,
                            Integer.parseInt(cantidadS),
                            Float.parseFloat(importeS)
                    );
                    productoList.add(0, p);
                    adapter.notifyItemInserted(0);
                }
            }
        });

        return builder.create();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(getString(R.string.id_lista), productoList);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArrayList<ProductoModel> aux =
                (ArrayList<ProductoModel>)
                        savedInstanceState.getSerializable(getString(R.string.id_lista));
        productoList.addAll(aux);
        adapter.notifyItemRangeInserted(0, productoList.size());
    }
}