package tlu.cse.ht63.cosmetics.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import tlu.cse.ht63.cosmetics.Adapter.CartAdapter;
import tlu.cse.ht63.cosmetics.Helper.ManagmentCart;
import tlu.cse.ht63.cosmetics.Model.ItemsPopularModel;
import tlu.cse.ht63.cosmetics.databinding.ActivityCartBinding;

public class CartActivity extends BaseActivity {

    private ActivityCartBinding binding;
    private ManagmentCart managmentCart;
    private double tax; // Khai báo biến tax ở mức lớp để sử dụng trong setVariable()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);
        initViews();
        initCartList();
        setVariable(); // Gọi phương thức setVariable() để thiết lập các sự kiện cho nút
    }

    private void initViews() {
        binding.cartView.setLayoutManager(new LinearLayoutManager(this));
        binding.emptyTxt.setVisibility(View.GONE);
        binding.scrollViewCart.setVisibility(View.VISIBLE);
    }

    private void initCartList() {
        CartAdapter cartAdapter = new CartAdapter(managmentCart.getListCart(), this, this::calculateCart);
        binding.cartView.setAdapter(cartAdapter);

        updateUI(); // Cập nhật giao diện ngay khi khởi tạo danh sách giỏ hàng
    }

    private void calculateCart() {
        double percentTax = 0.02;
        double delivery = 10;
        tax = Math.round((managmentCart.getTotalFee() * percentTax * 100.0)) / 100.0;
        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) / 100;
        double itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100;

        binding.totalFeeTxt.setText("$" + itemTotal);
        binding.taxTxt.setText("$" + tax);
        binding.deliveryTxt.setText("$" + delivery);
        binding.totalTxt.setText("$" + total);
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
        binding.checkOutBtn.setOnClickListener(v -> {
            if (!managmentCart.getListCart().isEmpty()) {
                // Tạo danh sách tên sản phẩm từ danh sách giỏ hàng
                StringBuilder productNamesBuilder = new StringBuilder();
                for (ItemsPopularModel item : managmentCart.getListCart()) {
                    productNamesBuilder.append(item.getTitle()).append(", ");
                }
                String productNames = productNamesBuilder.toString().trim();
                if (productNames.endsWith(",")) {
                    productNames = productNames.substring(0, productNames.length() - 1); // Xóa dấu phẩy cuối cùng nếu có
                }

                // Tính tổng giá tiền
                double totalPrice = managmentCart.getTotalFee() + tax + 10;

                // Chuyển sang CheckoutActivity và gửi dữ liệu
                Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                intent.putExtra("productNames", productNames);
                intent.putExtra("totalPrice", totalPrice);
                Log.d("CartActivity", "Starting CheckoutActivity with productNames: " + productNames + ", totalPrice: " + totalPrice);
                startActivity(intent);

                // Kết thúc hoạt động CartActivity
                finish();
            } else {
                Toast.makeText(CartActivity.this, "Giỏ hàng của bạn đang trống!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI() {
        if (managmentCart.getListCart().isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
        }
    }
}
