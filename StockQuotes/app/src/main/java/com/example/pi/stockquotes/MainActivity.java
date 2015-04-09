package com.example.pi.stockquotes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.zip.Inflater;


public class MainActivity extends ActionBarActivity {


    public static final String Stock_Symbol = "com.example.stockquote.STOCK";
    private SharedPreferences stockSymbolsEntered;
    private TableLayout stockTableScrollView;
    private EditText stockSymbolEditText;
    private Button enterStockSymbolButton;
    private Button deleteStocksButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stockSymbolsEntered = getSharedPreferences("stockList",MODE_PRIVATE);

        stockTableScrollView =  (TableLayout)findViewById(R.id.tableLayout2);
        stockSymbolEditText = (EditText)findViewById(R.id.stockSymbolEditText);
        enterStockSymbolButton = (Button)findViewById(R.id.enterStockSymbolButton);
        deleteStocksButton = (Button)findViewById(R.id.deleteStocksButton);

        enterStockSymbolButton.setOnClickListener(enterStockButtonListener);
        deleteStocksButton.setOnClickListener(deleteStocksButtonListener);
        updateSavedStockList(null);
    }


   public View.OnClickListener enterStockButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(stockSymbolEditText.getText().length() > 0){
                saveStockSymbol(stockSymbolEditText.getText().toString());
                stockSymbolEditText.setText("");
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(stockSymbolEditText.getWindowToken(),0);
            }else{
                AlertDialog.Builder Builder = new AlertDialog.Builder(MainActivity.this);
                Builder.setTitle(R.string.invalid_stock_symbol);
                Builder.setPositiveButton(R.string.ok,null);
                Builder.setMessage(R.string.missing_stock_symbol);
                AlertDialog theAlertDialog = Builder.create();
                theAlertDialog.show();
            }
        }
    };

    public View.OnClickListener deleteStocksButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            stockTableScrollView.removeAllViews();
            SharedPreferences.Editor preferenceseditor = stockSymbolsEntered.edit();
            preferenceseditor.clear();
            preferenceseditor.apply();
        }
    };

    private void updateSavedStockList(String newStockSymbol){
        String[] stocks = stockSymbolsEntered.getAll().keySet().toArray(new String[0]);
        Arrays.sort(stocks,String.CASE_INSENSITIVE_ORDER);

        if (newStockSymbol != null){
            insertStockInScrollView(newStockSymbol, Arrays.binarySearch(stocks, newStockSymbol));
        }else{
            for(int i = 0; i < stocks.length; i++){
                insertStockInScrollView(stocks[i], i);
            }
        }

    }

    private void saveStockSymbol(String newStock){
        String isTheStockNew = stockSymbolsEntered.getString(newStock,null);
        SharedPreferences.Editor preferenceEditor = stockSymbolsEntered.edit();
        preferenceEditor.putString(newStock,newStock);
        preferenceEditor.apply();
        if(isTheStockNew == null){
            updateSavedStockList(newStock);

        }
    }

    private void insertStockInScrollView(String stock , int arrayIndex){
        LayoutInflater inflater  = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newStockRow = inflater.inflate(R.layout.stock_quote_row,null);
        TextView newStockTextView = (TextView)newStockRow.findViewById(R.id.stockSymbolTextView);
        newStockTextView.setText(stock);
        Button stockQuoteButton  = (Button)newStockRow.findViewById(R.id.stockQuoteButton);
        stockQuoteButton.setOnClickListener(getStockactivityListener);
        Button quoteFromWebButton = (Button)newStockRow.findViewById(R.id.quoteFromWebButton);
        quoteFromWebButton.setOnClickListener(getStockFromWebsiteListener);

        stockTableScrollView.addView(newStockRow,arrayIndex);

    }

    public View.OnClickListener getStockactivityListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isConnectedToInternet()) {
                TableRow tablerow = (TableRow) v.getParent();
                TextView stockTextView = (TextView) tablerow.findViewById(R.id.stockSymbolTextView);
                String stockSymbol = stockTextView.getText().toString();
                Intent intent = new Intent(MainActivity.this, StockInfoActivity.class);
                intent.putExtra(Stock_Symbol, stockSymbol);
                startActivity(intent);
            }else{
                internetWarningDialogBox();
            }
        }
    };

    public View.OnClickListener getStockFromWebsiteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isConnectedToInternet()) {
                TableRow tablerow = (TableRow) v.getParent();
                TextView stockTextView = (TextView) tablerow.findViewById(R.id.stockSymbolTextView);
                String stockSymbol = stockTextView.getText().toString();
                String stockURL = getString(R.string.yahoo_stock_url) + stockSymbol;
                Intent getStockWebPage = new Intent(Intent.ACTION_VIEW, Uri.parse(stockURL));
                startActivity(getStockWebPage);
            }else{
                internetWarningDialogBox();
            }
        }
    };

    public Boolean isConnectedToInternet(){
        ConnectivityManager connectivitymanager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivitymanager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void internetWarningDialogBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Connectivity");
        builder.setMessage("No Internet Connectivity");
        builder.setPositiveButton(R.string.ok,null);
        AlertDialog internetwarningdialog = builder.create();
        internetwarningdialog.show();


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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
