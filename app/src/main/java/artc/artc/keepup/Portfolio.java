package artc.artc.keepup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

public class Portfolio extends AppCompatActivity {

    RecyclerView recyclerPortfolio;
    FirebaseRecyclerOptions<Shares> options;
    FirebaseRecyclerAdapter<Shares, PortfolioViewHolder> adapter;

    DatabaseReference databaseShares;
    Query databaseSearch;

    EditText filterTicker;
    Button buttonFilter;

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);

        recyclerPortfolio = findViewById(R.id.recyclerPortfolio);
        recyclerPortfolio.setHasFixedSize(true);
        recyclerPortfolio.setLayoutManager(new LinearLayoutManager(this));

        databaseShares = FirebaseDatabase.getInstance().getReference();

        options = new FirebaseRecyclerOptions.Builder<Shares>().setQuery(databaseShares, Shares.class).build();

        adapter = new FirebaseRecyclerAdapter<Shares, PortfolioViewHolder>(options) {
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
                return new PortfolioViewHolder(LayoutInflater.from(Portfolio.this).inflate(R.layout.portfolio_item_layout, viewGroup, false));
            }
        };

        recyclerPortfolio.setAdapter(adapter);

        filterTicker = findViewById(R.id.filterTicker);

        buttonFilter = findViewById(R.id.buttonFilter);
        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.stopListening();
                FilterTicker();
            }
        });

    }

    private void FilterTicker() {
        String filter_ticker = filterTicker.getText().toString();
        filterTicker.setText("");
        databaseSearch = databaseShares.orderByChild("sharesTicker").equalTo(filter_ticker);

        options = new FirebaseRecyclerOptions.Builder<Shares>().setQuery(databaseSearch, Shares.class).build();

        adapter = new FirebaseRecyclerAdapter<Shares, PortfolioViewHolder>(options) {
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
                return new PortfolioViewHolder(LayoutInflater.from(Portfolio.this).inflate(R.layout.portfolio_item_layout, viewGroup, false));
            }
        };

        recyclerPortfolio.setAdapter(adapter);
        adapter.startListening();
    }
}
