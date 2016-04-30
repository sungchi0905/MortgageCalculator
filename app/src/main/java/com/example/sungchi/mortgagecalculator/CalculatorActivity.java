package com.example.sungchi.mortgagecalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import java.lang.Math;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalculatorActivity extends AppCompatActivity implements OnItemSelectedListener{

    private EditText mHomeValue, mDownPayment, mAPR, mTaxRates;
    private Spinner mTerms;
    private TextView mTotalTaxPaid, mTotalInterestPaid, mMonthlyPayment, mPayOffDate;
    private String selectedTerm;

   // Initializes the app when it is first created.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);


        mHomeValue = (EditText)findViewById(R.id.home_value);
        mDownPayment = (EditText)findViewById(R.id.down_payment);
        mAPR = (EditText)findViewById(R.id.apr);
        mTerms = (Spinner)findViewById(R.id.terms);
        // Spinner click listener
        mTerms.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("15");
        categories.add("20");
        categories.add("25");
        categories.add("30");
        categories.add("40");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        mTerms.setAdapter(dataAdapter);

        mTaxRates = (EditText)findViewById(R.id.tax_rate);

        mTotalTaxPaid = (TextView)findViewById(R.id.total_tax_paid);
        mTotalInterestPaid = (TextView)findViewById(R.id.total_interest_paid);
        mMonthlyPayment = (TextView)findViewById(R.id.monthly_payment);
        mPayOffDate = (TextView)findViewById(R.id.pay_off_date);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reset:
                mHomeValue.setText(null);
                mDownPayment.setText(null);
                mAPR.setText(null);
                mTerms.setSelection(0);
                mTaxRates.setText(null);
                mTotalTaxPaid.setText(null);
                mTotalInterestPaid.setText(null);
                mMonthlyPayment.setText(null);
                mPayOffDate.setText(null);

                Toast.makeText(getApplicationContext(), "Reset Values",Toast.LENGTH_SHORT).show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
       selectedTerm = parent.getItemAtPosition(position).toString();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
    public void showLoanPayments(View clickedButton){

        if (mHomeValue.getText().toString().isEmpty()|| mDownPayment.getText().toString().isEmpty()|| mAPR.getText().toString().isEmpty()||selectedTerm.toString().isEmpty()|| mTaxRates.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "All fields are required",Toast.LENGTH_SHORT).show();
        }
        else if(Integer.parseInt(mAPR.getText().toString()) > 100){
            Toast.makeText(getApplicationContext(), "APR has to be less than or equal to 100%",Toast.LENGTH_SHORT).show();
        }
        else if(Integer.parseInt(mTaxRates.getText().toString()) > 100){
            Toast.makeText(getApplicationContext(), "Tax rate has to be less than or equal to 100%",Toast.LENGTH_SHORT).show();
        }
        else {
            double homeValue = Integer.parseInt(mHomeValue.getText().toString());
            double downPayment = Integer.parseInt(mDownPayment.getText().toString());
            double apr = Integer.parseInt(mAPR.getText().toString());
            double terms = Integer.parseInt(selectedTerm); //n
            double taxRates = Integer.parseInt(mTaxRates.getText().toString());

            double P = homeValue - downPayment; //Principal
            double r = apr / 1200; //monthlyInterestRate
            double n = terms * 12; //loan period
            double r1 = Math.pow(r + 1, n);
            double annualTax = homeValue * (taxRates / 100);//T

            double totalTaxPaid = annualTax * terms;
            double monthlyPayment = ((r + (r / (r1 - 1))) * P) + (annualTax / 12);
            double totalInterestPaid = ((P * ((r * r1) / (r1 - 1))) * (n)) - P;

            Calendar c = Calendar.getInstance();
            int month = c.get(Calendar.MONTH) - 1;
            int year = c.get(Calendar.YEAR) + (int) terms;

            String payOffDate = String.valueOf(Months.values()[month]) + " " + year;

            mTotalTaxPaid.setText("$ " + new DecimalFormat("##.##").format(totalTaxPaid));
            mTotalInterestPaid.setText("$ " + new DecimalFormat("##.##").format(totalInterestPaid));
            mMonthlyPayment.setText("$ " + new DecimalFormat("##.##").format(monthlyPayment));
            mPayOffDate.setText(payOffDate);

        }
    }
}
