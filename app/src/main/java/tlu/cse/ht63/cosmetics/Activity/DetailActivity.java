package tlu.cse.ht63.cosmetics.Activity;

import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;

import tlu.cse.ht63.cosmetics.Adapter.SliderAdapter;
import tlu.cse.ht63.cosmetics.Fragment.DescriptionFragment;
import tlu.cse.ht63.cosmetics.Fragment.ReviewFragment;
import tlu.cse.ht63.cosmetics.Fragment.SoldFragment;
import tlu.cse.ht63.cosmetics.Model.SliderItems;

import java.util.ArrayList;
import java.util.List;

import tlu.cse.ht63.cosmetics.Helper.ManagmentCart;
import tlu.cse.ht63.cosmetics.Model.SliderItems;
import tlu.cse.ht63.cosmetics.R;
import tlu.cse.ht63.cosmetics.Model.ItemsPopularModel;
import tlu.cse.ht63.cosmetics.databinding.ActivityDetailBinding;

public class DetailActivity extends BaseActivity {
    ActivityDetailBinding binding;
    private ItemsPopularModel object;
    private int numberOrder=1;
    private ManagmentCart managmentCart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart=new ManagmentCart(this);

        getBundles();
        banners();
        setupViewPager();

    }

    private void banners() {
        ArrayList<SliderItems> sliderItems=new ArrayList<>();
        for (int i = 0; i < object.getPicUrl().size(); i++) {
            sliderItems.add(new SliderItems(object.getPicUrl().get(i)));
            
        }
        binding.viewpageSlider.setAdapter(new SliderAdapter(sliderItems, binding.viewpageSlider));
        binding.viewpageSlider.setClipToPadding(false);
        binding.viewpageSlider.setClipChildren(false);
        binding.viewpageSlider.setOffscreenPageLimit(3);
        binding.viewpageSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

    }

    private void getBundles(){
        object=(ItemsPopularModel) getIntent().getSerializableExtra("object");
        binding.titleTxt.setText(object.getTitle());
        binding.priceTxt.setText("$"+object.getPrice());

        binding.addTocartBtn.setOnClickListener(v -> {
            object.setNumberInCart(numberOrder);
            managmentCart.insertItem(object);
        });
        binding.backBtn.setOnClickListener(v -> finish());
    }

    private void setupViewPager(){
        ViewPagerAdapter adapter=new ViewPagerAdapter(getSupportFragmentManager());
        DescriptionFragment tab1 = new DescriptionFragment();
        ReviewFragment tab2 = new ReviewFragment();
        SoldFragment tab3 = new SoldFragment();

        Bundle bundle1=new Bundle();
        Bundle bundle2=new Bundle();
        Bundle bundle3=new Bundle();

        bundle1.putString("description", object.getDescription());

        tab1.setArguments(bundle1);
        tab2.setArguments(bundle2);
        tab3.setArguments(bundle3);

        adapter.addFrag(tab1, "Description");
        adapter.addFrag(tab2, "Review");
        adapter.addFrag(tab3, "Sold");

        binding.viewpager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewpager);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter{
        private final List<Fragment> mFragmentsList=new ArrayList<>();
        private final List<String> mFragmentsTitleList=new ArrayList<>();
        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentsList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentsList.size();
        }

        private void addFrag(Fragment fragment,String title){
            mFragmentsList.add(fragment);
            mFragmentsTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position){
            return mFragmentsTitleList.get(position);
        }
    }

}