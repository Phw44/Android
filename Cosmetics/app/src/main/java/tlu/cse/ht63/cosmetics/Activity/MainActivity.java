package tlu.cse.ht63.cosmetics.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.List;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import tlu.cse.ht63.cosmetics.Adapter.PopularAdapter;
import tlu.cse.ht63.cosmetics.Model.ItemsPopularModel;
import tlu.cse.ht63.cosmetics.R;
//note
import tlu.cse.ht63.cosmetics.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    Button button;
    FirebaseUser user;
    FirebaseAuth auth;
    TextView textView;

    private ActivityMainBinding binding;
    private PopularAdapter adapter;
    private List<ItemsPopularModel> items;
    private List<ItemsPopularModel> originalItems;
    private DatabaseReference database;
    EditText edtSearch;
    TextView txtExplorer, txtCart, txtProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

//        button = findViewById(R.id.btnLogout);
//        textView = findViewById(R.id.textView);
//
//        // Khởi tạo FirebaseAuth
//        auth = FirebaseAuth.getInstance();
//        user = auth.getCurrentUser();
//
//        // Kiểm tra nếu người dùng chưa đăng nhập
//        if (user == null) {
//            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//            startActivity(intent);
//            finish();
//        } else {
//            // Hiển thị email của người dùng
//            textView.setText(user.getEmail());
//        }
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Đăng xuất người dùng
//                auth.signOut();
//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        items = new ArrayList<>();
        originalItems = new ArrayList<>(); // Initialize the originalItems list
        adapter = new PopularAdapter((ArrayList<ItemsPopularModel>) items);

        binding.rvItemsProduct.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rvItemsProduct.setAdapter(adapter);

        database = FirebaseDatabase.getInstance().getReference();

        initItemsPopular();
        initUI();
        initLisner();

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterItems(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    adapter.updateList(originalItems); // Reset to original items
                }
            }
        });
    }

    private void initLisner() {
        txtProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initItemsPopular() {
        DatabaseReference myRef = database.child("Items");

        binding.progressBarItemsProduct.setVisibility(View.VISIBLE);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    items.clear();  // Clear the existing items
                    originalItems.clear(); // Clear the original items
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        ItemsPopularModel item = issue.getValue(ItemsPopularModel.class);
                        if (item != null) {
                            items.add(item);
                            originalItems.add(item);
                        }
                    }
                    adapter.notifyDataSetChanged(); // Notify the adapter of data changes
                }
                binding.progressBarItemsProduct.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void filterItems(String query) {
        List<ItemsPopularModel> filteredList = new ArrayList<>();

        for (ItemsPopularModel item : originalItems) { // Sử dụng danh sách gốc để lọc
            if (item.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.updateList(filteredList);
    }


    private void initUI() {
        edtSearch = findViewById(R.id.edtSearch);
        txtExplorer = findViewById(R.id.txtExplorer);
        txtCart = findViewById(R.id.txtCart);
        txtProfile = findViewById(R.id.txtProfile);
    }
}
