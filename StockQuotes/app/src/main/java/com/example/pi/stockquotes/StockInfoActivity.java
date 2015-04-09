package com.example.pi.stockquotes;

import android.content.Intent;
import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.renderscript.Element;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class StockInfoActivity extends ActionBarActivity {

    private static final String TAG = "StockQuote";
    TextView companyNameTextView;
    TextView daysHighTextView;
    TextView daysLowTextView;
    TextView yearHighTextView;
    TextView yearLowTextView;
    TextView lastPriceTextView;
    TextView changeTextView;
    TextView dailyPriceRangeTextView;



    String name = "";
    String dayslow = "";
    String dayshigh = "";
    String yearlow ="";
    String yearhigh = "";
    String lasttradepriceonly = "";
    String change = "";
    String daysrange = "";

    String yahooURLFirst = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quote%20where%20symbol%20in%20(%22";
    String yahooURLSecond = "%22)&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

    String[][] xmlPullParserArray = {{"AverageDailyVolume","0"},{"Change","0"},{"DaysLow","0"},{"DaysHigh","0"},{"YearLow","0"},{"YearHigh","0"},{"MarketCapitalization","0"},{"LastTradePriceOnly","0"},{"DaysRange","0"},{"Name","0"},{"Symbol","0"},{"Volume","0"},{"StockExchange","0"}};
    int parserArrayIncrement = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_info);
        Intent intent = getIntent();
        String stockSymbol = intent.getStringExtra(MainActivity.Stock_Symbol);

        companyNameTextView = (TextView)findViewById(R.id.companyNameTextView);
        daysHighTextView = (TextView)findViewById(R.id.daysHighTextView);
        daysLowTextView = (TextView)findViewById(R.id.daysLowTextView);
        yearHighTextView = (TextView)findViewById(R.id.yearHighTextView);
        yearLowTextView = (TextView)findViewById(R.id.yearLowTextView);
        lastPriceTextView =(TextView)findViewById(R.id.lastPriceTextView);
        changeTextView = (TextView)findViewById(R.id.changeTextView);
        dailyPriceRangeTextView = (TextView)findViewById(R.id.dailyPriceRangeTextView);

        Log.d(TAG, "Before URL Creation" + stockSymbol);
        final String URL = yahooURLFirst + stockSymbol + yahooURLSecond;

        new MyAsyncTask().execute(URL);

    }

     private class MyAsyncTask extends AsyncTask <String, String, String> {

         protected String doInBackground(String... args) { //String... arg0 is just a string array like String[] argo

            try{
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(new InputStreamReader(getURLData(args[0])));
                beginDocument(parser,"query");

                int eventType = parser.getEventType();

                do{
                    nextElement(parser);
                    parser.next();
                    eventType = parser.getEventType();
                    if(eventType == XmlPullParser.TEXT ){
                        String valueFromXml = parser.getText();
                        xmlPullParserArray[parserArrayIncrement++][1] = valueFromXml;
                    }
                }while(eventType != XmlPullParser.END_DOCUMENT);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }

             return null;
         }

         public InputStream getURLData(String URL) throws IOException, URISyntaxException, ClientProtocolException{
             DefaultHttpClient client = new DefaultHttpClient();
             HttpGet method = new HttpGet(URI.create(URL));
             HttpResponse res = client.execute(method);
             return res.getEntity().getContent();
         }

         public final void beginDocument(XmlPullParser parser, String firstElementName)throws XmlPullParserException, IOException{
            int type;
            while((type = parser.next()) != parser.START_TAG && type != parser.END_DOCUMENT){
                ;
            }

            if(type != parser.START_TAG){
                throw new XmlPullParserException("No Start Tag Found");
            }
            if(!parser.getName().equals(firstElementName)){
                throw new XmlPullParserException("Unexpected Tag Name Found " + parser.getName()+", Expected "+ firstElementName );
            }
         }

         public final void nextElement(XmlPullParser parser) throws XmlPullParserException, IOException{
             int type;
             while((type = parser.next()) != parser.START_TAG && type != parser.END_DOCUMENT){
                 ;
             }
         }
         protected void onPostExecute(String result){
             companyNameTextView.setText(xmlPullParserArray[9][1]);
             daysLowTextView.setText("Days Low:" + xmlPullParserArray[2][1]);
             daysHighTextView.setText("Days High:"+xmlPullParserArray[3][1]);
             yearHighTextView.setText("Year High:"+xmlPullParserArray[5][1]);
             yearLowTextView.setText("Year Low:"+xmlPullParserArray[4][1]);
             changeTextView.setText("Change:"+xmlPullParserArray[1][1]);
             lastPriceTextView.setText("Last Price:"+xmlPullParserArray[7][1]);
             dailyPriceRangeTextView.setText("Daily Price Range:"+xmlPullParserArray[8][1]);
         }



     }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stock_info, menu);
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
