package tlu.cse.ht63.cosmetics.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Locale;

import tlu.cse.ht63.cosmetics.Adapter.CartAdapter;
import tlu.cse.ht63.cosmetics.Helper.ManagmentCart;
import tlu.cse.ht63.cosmetics.Model.ItemsPopularModel;
import tlu.cse.ht63.cosmetics.R;
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
        calculateCart();
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
        double total = (managmentCart.getTotalFee() + tax + delivery) * 100 / 100;
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

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật lại danh sách giỏ hàng khi quay lại từ CheckoutActivity (hoặc bất kỳ hoạt động nào khác cần cập nhật lại)
        initCartList();
    }


    // chuyen doi ngon ngu
    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        if (!language.isEmpty()) {
            setLocale(language);
        }
    }

    private void setLocale(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", langCode);
        editor.apply();

        updateTexts(); // Update UI texts directly instead of recreating the activity
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadLocale(); // Kiểm tra và cập nhật ngôn ngữ mỗi khi MainActivity được khởi động lại
    }


    private void updateTexts() {
        binding.tvCart.setText(R.string.my_cart);
        binding.edtCoupon.setText(R.string.edtCoupon);
        binding.btnApply.setText(R.string.btn_apply);
        binding.textView2.setText(R.string.Subtotal);
        binding.textView3.setText(R.string.Delivery);
        binding.textView4.setText(R.string.tax);
        binding.textView5.setText(R.string.total);
        binding.checkOutBtn.setText(R.string.checkout);
    }
}
