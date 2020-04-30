package artc.artc.keepup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText sharesTicker;
    EditText sharesPrice;
    EditText sharesQty;
    Button buttonAdd;

    Button buttonPortfolio;

    DatabaseReference databaseShares;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseShares = FirebaseDatabase.getInstance().getReference();

        sharesTicker = findViewById(R.id.sharesTicker);
        sharesPrice = findViewById(R.id.sharesPrice);
        sharesQty = findViewById(R.id.sharesQty);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTrip();
            }
        });

        buttonPortfolio = findViewById(R.id.buttonPortfolio);
        buttonPortfolio.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                OpenPortfolio();
            }
        });

    }

    private void AddTrip() {
        String ticker = sharesTicker.getText().toString().trim();
        String price = sharesPrice.getText().toString().trim();
        String qty = sharesQty.getText().toString().trim();

        if (!TextUtils.isEmpty(price)) {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("ddMMyy,HHmm", Locale.ROOT);

            String timestamp = formatter.format(calendar.getTime());

            Shares shares = new Shares(timestamp, ticker, price, qty);
            databaseShares.child(timestamp).setValue(shares);
            sharesTicker.setText("");
            sharesPrice.setText("");
            sharesQty.setText("");
            Toast.makeText(this, "Shares added!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Enter a Price!", Toast.LENGTH_LONG).show();
        }
    }

    public void OpenPortfolio(){
        Intent intent = new Intent(this, Portfolio.class);
        startActivity(intent);
    }

}