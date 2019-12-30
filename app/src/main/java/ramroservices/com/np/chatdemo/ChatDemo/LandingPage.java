package ramroservices.com.np.chatdemo.ChatDemo;

import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import ramroservices.com.np.chatdemo.R;

public class LandingPage extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    DatabaseReference databaseReference;
FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        tabLayout = findViewById(R.id.tlone);
        viewPager = findViewById(R.id.vpone);
        ViewpageAdapter viewpageAdapter = new ViewpageAdapter(getSupportFragmentManager());
        viewpageAdapter.addFragment(new UserFragment(),"Users");
        viewpageAdapter.addFragment(new ChatFragment(),"Recent Chat");
        viewPager.setAdapter(viewpageAdapter);
        tabLayout.setupWithViewPager(viewPager);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


    }

    class ViewpageAdapter extends FragmentPagerAdapter{
        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewpageAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();

        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment,String title){
            fragments.add(fragment);
            titles.add(title);
        }

//        public void addFragment(Fragment fragment){
//            fragments.add(fragment);
//           // titles.add(title);
//        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    private  void status(String status){
        databaseReference = FirebaseDatabase.getInstance().getReference("members").child(firebaseUser.getUid());
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("status",status);
        databaseReference.updateChildren(hashMap);



    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        status("offline");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menulogout,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.navlogout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(LandingPage.this,LoginActivity.class));
                finish();
                return true;
        }
        return false;
    }
}
