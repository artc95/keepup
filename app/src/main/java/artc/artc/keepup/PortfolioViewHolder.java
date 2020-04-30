package artc.artc.keepup;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PortfolioViewHolder extends RecyclerView.ViewHolder {

    TextView sharesTimestamp, sharesTicker, sharesPrice, sharesQty;

    public PortfolioViewHolder(@NonNull View itemView) {
        super(itemView);

        sharesTimestamp = itemView.findViewById(R.id.sharesTimestamp);
        sharesTicker = itemView.findViewById(R.id.sharesTicker);
        sharesPrice = itemView.findViewById(R.id.sharesPrice);
        sharesQty = itemView.findViewById(R.id.sharesQty);
    }
}
