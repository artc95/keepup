package artc.artc.keepup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.Collections;

public class PortfolioActivity extends AppCompatActivity {

    RecyclerView recyclerPortfolio;
    FirebaseRecyclerOptions<Shares> options;
    FirebaseRecyclerAdapter<Shares, PortfolioViewHolder> adapterRecycler;

    DatabaseReference databaseShares;
    Query databaseSearch;

    TextView totalQty;

    Spinner spinnerTickers;

    @Override
    protected void onStart() {
        super.onStart();
        adapterRecycler.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterRecycler.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);

        // Get user_email and user_id from LoginActivity and restrict access
        final String user_id = getIntent().getStringExtra("user_id");
        databaseShares = FirebaseDatabase.getInstance().getReference().child(user_id);

        // 1) Populate Spinner with Tickers in portfolio
        spinnerTickers = findViewById(R.id.spinnerTickers);
        databaseShares.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> arrayTickersRaw = new ArrayList<>();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Shares shares = data.getValue(Shares.class);
                    String ticker = shares.getSharesTicker();
                    arrayTickersRaw.add(ticker);
                }

                // arrayTickersRaw may have duplicates, remove by creating new arrayTickers
                ArrayList<String> arrayTickers = new ArrayList<>();
                for (String element : arrayTickersRaw) {
                    if (!arrayTickers.contains(element)) {
                        arrayTickers.add(element);
                    }
                }
                arrayTickers.add("*All");
                Collections.sort(arrayTickers);
                ArrayAdapter<String> adapterTickers = new ArrayAdapter<String>(PortfolioActivity.this, android.R.layout.simple_spinner_item, arrayTickers);
                adapterTickers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerTickers.setAdapter(adapterTickers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        // 2) Populate Recycler with portfolio details
        recyclerPortfolio = findViewById(R.id.recyclerPortfolio);
        recyclerPortfolio.setHasFixedSize(true);
        recyclerPortfolio.setLayoutManager(new LinearLayoutManager(this));
        DisplayPortfolio(databaseShares);

        // 3) Filter by Spinner selection
        spinnerTickers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                String filter_ticker = spinnerTickers.getSelectedItem().toString();

                if (filter_ticker.equals("*All")) {
                    // Filter "All" displays entire portfolio
                    databaseShares = FirebaseDatabase.getInstance().getReference(user_id);
                    DisplayPortfolio(databaseShares);
                } else {
                    databaseSearch = databaseShares.orderByChild("sharesTicker").equalTo(filter_ticker);
                    DisplayPortfolio(databaseSearch);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void DisplayPortfolio(Query database) {
        // a) populate recyclerPortfolio with entire/filtered portfolio
        options = new FirebaseRecyclerOptions.Builder<Shares>().setQuery(database, Shares.class).build();
        adapterRecycler = new FirebaseRecyclerAdapter<Shares, PortfolioViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PortfolioViewHolder holder, int position, @NonNull Shares shares) {
                holder.sharesTimestamp.setText(shares.getSharesTimestamp());
                holder.sharesTicker.setText(shares.getSharesTicker());
                holder.sharesPrice.setText(shares.getSharesPrice());
                holder.sharesQty.setText(shares.getSharesQty());
            }

            @NonNull
            @Override
            public PortfolioViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return new PortfolioViewHolder(LayoutInflater.from(PortfolioActivity.this).inflate(R.layout.portfolio_item_layout, viewGroup, false));
            }
        };
        recyclerPortfolio.setAdapter(adapterRecycler);
        adapterRecycler.startListening();

        // b) calculate and display overall information (i.e. Total Qty, Total Value) for entire/filtered portfolio
        totalQty = findViewById(R.id.totalQty);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int total_qty = 0;

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Shares shares = data.getValue(Shares.class);
                    int qty = Integer.parseInt(shares.getSharesQty());
                    total_qty = total_qty + qty;
                }

                totalQty.setText(String.format("%d", total_qty));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}

