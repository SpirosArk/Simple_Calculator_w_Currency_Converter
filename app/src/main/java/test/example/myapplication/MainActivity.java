package test.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class MainActivity extends AppCompatActivity {

    EditText firstInputEditText, secondInputEditText;
    Double firstInputNumericValue, secondInputNumericValue;

    TextView firstInputShow, secondInputShow, resultShow;


    private static Double convert(String from, String to, String amount) {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder().build();

            Request request = new Request.Builder()
                    .url(String.format("https://api.apilayer.com/fixer/convert?to=%s&from=%s&amount=%s", to, from, amount))
                    .addHeader("apikey", "SUBSCRIBE AND ENTER YOUR API HERE")
                    .method("GET", null)
                    .build();
            Response response = client.newCall(request).execute();

            JSONObject o = new JSONObject(response.body().string());

            if (o.getBoolean("success")) {
                return o.getDouble("result");
            } else {
                return -1.0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1.0;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner_currency=findViewById(R.id.currencySelectorSpinner);
        ArrayAdapter<CharSequence>adapter = ArrayAdapter.createFromResource(this, R.array.currency, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_currency.setAdapter(adapter);
        String state = spinner_currency.getSelectedItem().toString();


        // defining the edit text 1 to e1
        firstInputEditText = (EditText) findViewById(R.id.firstInput);
        // defining the edit text 2 to e2
        secondInputEditText = (EditText) findViewById(R.id.secondInput);

        firstInputShow = (TextView) findViewById(R.id.firstInput);
        secondInputShow = (TextView) findViewById(R.id.secondInput);
        resultShow = (TextView) findViewById(R.id.resultOutput);
    }

    // a public method to get the input numbers
    public boolean getNumbers() {
        // taking input from text box 1
        String s1 = firstInputEditText.getText().toString();

        // taking input from text box 2
        String s2 = secondInputEditText.getText().toString();

        // condition to check if box is not empty
        if (s1.isEmpty() || s2.isEmpty()) {
            Toast.makeText(this, "Please enter a value on both inputs...", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            try {
                // converting string to int.
                firstInputNumericValue = Double.parseDouble(s1);
                // converting string to int.
                secondInputNumericValue = Double.parseDouble(s2);
            } catch (Exception e) {
                Toast.makeText(this, "Only numbers are acceptable characters", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    // a public method to perform addition
    public void doSum(View v) {

        // get the input numbers
        if (getNumbers()) {
            Double sum = firstInputNumericValue + secondInputNumericValue;
            resultShow.setText(Double.toString(sum));
        }
    }

    // a public method to perform subtraction
    public void doSub(View v) {

        // get the input numbers
        if (getNumbers()) {
            Double sum = firstInputNumericValue - secondInputNumericValue;
            resultShow.setText(Double.toString(sum));
        }
    }

    // a public method to perform multiplication
    public void doMul(View v) {

        // get the input numbers
        if (getNumbers()) {
            Double sum = firstInputNumericValue * secondInputNumericValue;
            resultShow.setText(Double.toString(sum));
        }
    }

    // a public method to perform Division
    public void doDiv(View v) {

        // get the input numbers
        if (getNumbers()) {

            // displaying the text in text view assigned as tresult
            double sum = firstInputNumericValue / (secondInputNumericValue * 1.0);
            resultShow.setText(Double.toString(sum));
        }
    }

    // a public method to have a Clear All Button
    public void doClear(View v) {
        firstInputShow.setText(null);
        secondInputShow.setText(null);
        resultShow.setText(null);
    }

    // a public method to manage the API conversion, by Default we consider our Result to have Base Eur
    public void doConvert(View v) {
        //String state = spinner_currency.getSelectedItem().toString();
        // run callout to new thread because android does not allow net callouts on UI thread
        new Thread(() -> {

            Spinner spinner = (Spinner)findViewById(R.id.currencySelectorSpinner);
            String choice = spinner.getSelectedItem().toString();

            resultShow.setText(Double.toString(convert("EUR", choice, resultShow.getText().toString())));
        }).start();
    }


}
