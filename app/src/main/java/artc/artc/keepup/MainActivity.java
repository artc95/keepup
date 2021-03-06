package artc.artc.keepup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.text.TextUtils;
import android.widget.TextView;
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

    EditText sharesTimestamp, sharesTicker, sharesPrice, sharesQty;
    Button buttonAdd, buttonPortfolio;
    TextView textUser;

    DatabaseReference databaseShares;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get user_email and user_id from LoginActivity and restrict access
        final String user_email = getIntent().getStringExtra("user_email");
        final String user_id = getIntent().getStringExtra("user_id");
        textUser = findViewById(R.id.textUser);
        textUser.setText(user_email);

        databaseShares = FirebaseDatabase.getInstance().getReference(user_id);

        sharesTimestamp = findViewById(R.id.sharesTimestamp);
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
                OpenPortfolio(user_email, user_id);
            }
        });

    }

    private void AddTrip() {

        String timestamp = sharesTimestamp.getText().toString().trim();
        if (!TextUtils.isEmpty(timestamp)){
            sharesTimestamp.setText("");
        } else {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("ddMMyy,HHmmss", Locale.ROOT);
            timestamp = formatter.format(calendar.getTime());
        }

        String ticker = sharesTicker.getText().toString().trim();
        sharesTicker.setText("");

        String price = sharesPrice.getText().toString().trim();
        sharesPrice.setText("");

        String qty = sharesQty.getText().toString().trim();
        sharesQty.setText("");

        if (!TextUtils.isEmpty(price)) {

            Shares shares = new Shares(timestamp, ticker, price, qty);
            databaseShares.child(timestamp).setValue(shares);

            Toast.makeText(this, "Shares added!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Enter a Price!", Toast.LENGTH_LONG).show();
        }
    }

    public void OpenPortfolio(String user_email, String user_id){
        Intent intent = new Intent(this, PortfolioActivity.class);
        intent.putExtra("user_email", user_email);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
    }

}