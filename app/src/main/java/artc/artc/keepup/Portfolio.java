package artc.artc.keepup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import java.util.ArrayList;

public class Portfolio extends AppCompatActivity {

    RecyclerView recyclerPortfolio;
    ArrayList<Shares> arrayList;
    FirebaseRecyclerOptions<Shares> options;
    FirebaseRecyclerAdapter<Shares, PortfolioViewHolder> adapter;

    DatabaseReference databaseShares;

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

        databaseShares = FirebaseDatabase.getInstance().getReference();

        recyclerPortfolio = findViewById(R.id.recyclerPortfolio);
        recyclerPortfolio.setHasFixedSize(true);
        recyclerPortfolio.setLayoutManager(new LinearLayoutManager(this));

        arrayList = new ArrayList<Shares>();

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

    }
}
