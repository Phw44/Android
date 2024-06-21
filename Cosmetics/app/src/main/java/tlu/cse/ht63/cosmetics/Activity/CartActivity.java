package tlu.cse.ht63.cosmetics.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import tlu.cse.ht63.cosmetics.Adapter.CartAdapter;
import tlu.cse.ht63.cosmetics.Helper.ChangeNumberItemsListener;
import tlu.cse.ht63.cosmetics.Helper.ManagmentCart;
import tlu.cse.ht63.cosmetics.R;
import tlu.cse.ht63.cosmetics.databinding.ActivityCartBinding;

public class CartActivity extends BaseActivity {

    ActivityCartBinding binding;
    private double tax;
    private ManagmentCart managmentCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());

        setContentView(R.layout.activity_cart);
        managmentCart = new ManagmentCart(this);
        caculatorCart();
        setVariable();
        initCartList();

    }
    private void initCartList(){
        if (managmentCart.getListCart().isEmpty()){
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        }else{
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
        }
        binding.cartView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.cartView.setAdapter(new CartAdapter(managmentCart.getListCart(), this, () -> caculatorCart()));
    }

    private void setVariable(){
        binding.backBtn.setOnClickListener(v -> finish());
    }

    private void caculatorCart(){
        double percentTax = 0.02;
        double delivery = 10;
        tax = Math.round((managmentCart.getTotalFee()*percentTax*100.0))/100.0;
        double total = Math.round((managmentCart.getTotalFee()+tax+delivery)*100)/100;
        double itemTotal = Math.round(managmentCart.getTotalFee()*100)/100;

        binding.totalFeeTxt.setText("$" + itemTotal);
        binding.taxTxt.setText("$" + tax);
        binding.deliveryTxt.setText("$" + delivery);
        binding.totalFeeTxt.setText("$" + total);

    }
}
