package tlu.cse.ht63.cosmetics.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import tlu.cse.ht63.cosmetics.Adapter.CartAdapter;
import tlu.cse.ht63.cosmetics.Helper.ChangeNumberItemsListener;
import tlu.cse.ht63.cosmetics.Model.ItemsPopularModel;
import tlu.cse.ht63.cosmetics.Model.Order;
import tlu.cse.ht63.cosmetics.databinding.ActivityCheckoutBinding;

public class CheckoutActivity extends BaseActivity {

    private RecyclerView cartRecyclerView;
    private ActivityCheckoutBinding binding;
    private List<ItemsPopularModel> productList;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();
        setupRecyclerView();
        setupOrderButton();
        setupBackButton();

        // Lấy dữ liệu sản phẩm từ Intent
        Intent intent = getIntent();
        String productNames = intent.getStringExtra("productNames");
        double totalPrice = intent.getDoubleExtra("totalPrice", 0.0);

        // Hiển thị thông tin sản phẩm và tổng giá tiền
        binding.productNameTxt.setText(productNames);
        binding.totalPriceTxt.setText("$" + totalPrice);
    }

    private void initViews() {
        cartRecyclerView = binding.cartView;
    }

    private void setupRecyclerView() {
        productList = new ArrayList<>();
        cartAdapter = new CartAdapter(productList, this, new ChangeNumberItemsListener() {
            @Override
            public void changed() {
                updateTotalPrice();
            }
        });
        cartRecyclerView.setAdapter(cartAdapter);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupOrderButton() {
        binding.orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String customerName = binding.customerNameTxt.getText().toString().trim();
                String phoneNumber = binding.customerPhoneTxt.getText().toString().trim();
                String address = binding.customerAddressTxt.getText().toString().trim();

                // Kiểm tra thông tin khách hàng
                if (customerName.isEmpty() || phoneNumber.isEmpty() || address.isEmpty()) {
                    Toast.makeText(CheckoutActivity.this, "Vui lòng điền đầy đủ thông tin khách hàng", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Tính tổng giá tiền
                double totalPrice = calculateTotalPrice();

                // Lưu thông tin khách hàng và đơn hàng vào Firebase
                saveOrderToDatabase(customerName, phoneNumber, address, totalPrice);
            }
        });
    }

    private void setupBackButton() {
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckoutActivity.this, CartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish(); // Kết thúc ProfileActivity để nó không còn trong stack
            }
        });
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void updateTotalPrice() {
        double totalPrice = calculateTotalPrice();
        binding.totalPriceTxt.setText("Total Price: $" + totalPrice);
    }

    private double calculateTotalPrice() {
        double totalPrice = 0;
        for (ItemsPopularModel item : productList) {
            totalPrice += item.getNumberInCart() * item.getPrice();
        }
        return totalPrice;
    }

    private void saveOrderToDatabase(String customerName, String phoneNumber, String address, double totalPrice) {

        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("orders");
        String orderId = ordersRef.push().getKey();

        List<String> productNames = new ArrayList<>();
        for (ItemsPopularModel item : productList) {
            productNames.add(item.getTitle());
        }

        // Chuyển đổi totalPrice từ double sang String
        Order order = new Order(orderId, customerName, phoneNumber, address, productNames, String.valueOf(totalPrice));

        ordersRef.child(orderId).setValue(order, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    // Lưu thành công
                    Toast.makeText(CheckoutActivity.this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CheckoutActivity.this, MainActivity.class));
                    finish();
                } else {
                    // Lỗi khi lưu
                    Toast.makeText(CheckoutActivity.this, "Đặt hàng thất bại. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                    error.toException().printStackTrace();
                    Log.e("FirebaseError", "Lỗi khi lưu đơn hàng: ", error.toException());
                }
            }
        });
    }

}