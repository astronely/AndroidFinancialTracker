package com.susu.bankapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private HomeFragment homeFragment;
    private IncomesFragment incomeFragment;
    private ExpensesFragment expenseFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        homeFragment = new HomeFragment();
        incomeFragment = new IncomesFragment();

        expenseFragment = new ExpensesFragment();
        setFragment(homeFragment);

        BottomNavigationView navigationView = findViewById(R.id.bottomNavigation);
        navigationView.setSelectedItemId(R.id.nav_main);

        navigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_incomes:
                    setFragment(incomeFragment);
                    return true;
                case R.id.nav_expenses:
                    setFragment(expenseFragment);
                    return true;
                case R.id.nav_main:
                    setFragment(homeFragment);
                    return true;
                default:
                    return false;
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}