package com.sellba.app.sellbatery.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.sellba.app.sellbatery.Class.Articulo;
import com.sellba.app.sellbatery.Database.Database;
import com.sellba.app.sellbatery.Fragments.CompraFragment;
import com.sellba.app.sellbatery.Fragments.InteresFragment;
import com.sellba.app.sellbatery.Fragments.VentaFragment;
import com.sellba.app.sellbatery.R;
import com.sellba.app.sellbatery.Utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private VentaFragment ventaFragment;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        sessionManager=new SessionManager(this);
        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Setting ViewPager for each Tabs
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        snack();
        TabLayout.Tab tabCall = tabs.getTabAt(0);
        tabCall.setIcon(R.drawable.home_selector);
        TabLayout.Tab tabCall2 = tabs.getTabAt(1);
        tabCall2.setIcon(R.drawable.interes_selector);
        TabLayout.Tab tabCall3 = tabs.getTabAt(2);
        tabCall3.setIcon(R.drawable.venta_selector);
    }
    private  Adapter adapter;
    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        adapter = new Adapter(getSupportFragmentManager());
        ventaFragment=new VentaFragment();
        adapter.addFragment(new CompraFragment(),"Compra");
        adapter.addFragment(ventaFragment,"Venta");
        adapter.addFragment(new InteresFragment(),"Interes");
        viewPager.setAdapter(adapter);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Articulo passedItem = (Articulo) data.getExtras().get("passed_item");

            Database database=new Database(getApplication());
            database.open();
            database.insertArticulo(passedItem.getNombre(),passedItem.getPrecio(),passedItem.getDescripcion(), String.valueOf(data.getExtras().get("photo")),sessionManager.getid());
            database.close();
            // deal with the item yourself
            ventaFragment.noti();
            snackAdd();
        }
    }
    private void snack(){

        Snackbar snackbar =Snackbar.make(findViewById(R.id.placeSnackBar),"Bienvenido "+ sessionManager.getUserDetails().get("name"),Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)      view.getLayoutParams();
        params.gravity = Gravity.FILL_HORIZONTAL | Gravity.BOTTOM;
        view.setLayoutParams(params);

        snackbar.show();
    }    private void snackAdd(){

        Snackbar snackbar =Snackbar.make(findViewById(R.id.placeSnackBar),"Ha añadido un nuevo producto "+ sessionManager.getUserDetails().get("name"),Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)      view.getLayoutParams();
        params.gravity = Gravity.FILL_HORIZONTAL | Gravity.BOTTOM;
        view.setLayoutParams(params);

        snackbar.show();
    }
    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id== R.id.logout){
            sessionManager.logoutUser();
            finish();
        }
        if(id== R.id.add){
            Intent intent=new Intent(this, AddArticuloActivity.class);
            startActivityForResult(intent,REQUEST_CODE);
        }
        if( id==R.id.perfil){
            Intent intent1= new Intent(this,ProfileActivity.class);
            startActivityForResult(intent1,REQUEST_CODE);
        }
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }
}
