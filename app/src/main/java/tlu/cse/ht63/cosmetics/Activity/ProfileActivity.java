package tlu.cse.ht63.cosmetics.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

//import com.google.firebase.auth.FirebaseAuth;
////import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

import tlu.cse.ht63.cosmetics.R;
import tlu.cse.ht63.cosmetics.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
//    private FirebaseAuth auth;
//    FirebaseUser user;

    public static final String[] languages = {"Select Language", "English", "Vietnamese"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sử dụng ViewBinding để thay thế findViewById
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo FirebaseAuth
//        auth = FirebaseAuth.getInstance();
//        user = auth.getCurrentUser();
//
//        if (user != null) {
//            // Hiển thị email của người dùng
//            binding.txtUserName.setText(user.getEmail());
//        }
//
//        // Đăng xuất người dùng
//        binding.txtLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                auth.signOut();
//                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });

        binding.backBtn.setOnClickListener(v -> finish());

        loadLocale(); // Load locale after initializing UI elements

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, languages);
        binding.spinner.setAdapter(adapter);

        binding.spinner.setSelection(0);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLang = parent.getItemAtPosition(position).toString();

                if (selectedLang.equals("English")) {
                    setLocaleAndRestart("en");
                } else if (selectedLang.equals("Vietnamese")) {
                    setLocaleAndRestart("vi");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "en");
        if (!language.isEmpty()) {
            setLocale(language);
        }
    }

    private void setLocaleAndRestart(String langCode) {
        // Lưu ngôn ngữ đã chọn vào Shared Preferences
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", langCode);
        editor.apply();

        // Cập nhật ngôn ngữ trong ứng dụng
        setLocale(langCode);

        // Khởi động lại MainActivity để áp dụng ngay lập tức ngôn ngữ mới
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Xóa tất cả các Activity trước đó
        startActivity(intent);
        finish();
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

    private void updateTexts() {
        binding.txtMember.setText(R.string.text_member);
        binding.txtUpdateProfile.setText(R.string.update_profile);
        binding.txtSettings.setText(R.string.settings);
        binding.txtAboutUs.setText(R.string.about_us);
        binding.txtLogout.setText(R.string.logout);
    }

}
