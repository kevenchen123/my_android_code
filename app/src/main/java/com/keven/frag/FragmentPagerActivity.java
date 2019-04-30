package com.keven.frag;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;

import com.keven.R;

public class FragmentPagerActivity extends AppCompatActivity {

    ArrayList<Fragment> fragmentList =  new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_pager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        testViewPage();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void testViewPage() {
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);

        fragmentList.add(TestFragment.newInstance(0));
        fragmentList.add(TestFragment.newInstance(1));
        fragmentList.add(new PrefsFragment());

        NFragmentPagerAdapter pagerAdapter = new NFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(pagerAdapter);

        Log.d("TAG", "**********   pagerAdapter="+pagerAdapter+"   fragmentList="+fragmentList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            for (int i = 0; i < fragmentList.size() - 1; i++) {
                Log.d("TAG", "********** add="+ fragmentList.get(i).isAdded());
                if (fragmentList.get(i).isAdded()) {
                    ((TestFragment)fragmentList.get(i)).setState("FRAGMENT_INDEX = " + i +"  changed");
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}