package com.example.foodscanbykamm;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;


public class ProductActivity extends AppCompatActivity {

    // Declare variables
    ListView listView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);


        listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
        new Connection().execute();

    }

    class Connection extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = "";

            String host = "http://10.0.2.2:8080/products/product.php";
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(host));
                HttpResponse response = client.execute(request);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                StringBuffer stringBuffer = new StringBuffer("");

                String line = "";
                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line);
                    break;
                }
                reader.close();
                result = stringBuffer.toString();
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            //parse json data from product table here
            try {
                JSONObject jsonResult = new JSONObject(result);
                int success = jsonResult.getInt("success");
                if (success == 1) {
                    JSONArray product = jsonResult.getJSONArray("product");
                    for (int i = 0; i < product.length(); i++) {
                        JSONObject products = product.getJSONObject(i);
                        int id = products.getInt("id");
                        String barcode_id = products.getString("barcode_id");
                        String product_name = products.getString("product_name");
                        int calories = products.getInt("calories");
                        double fat_amount = products.getDouble("fat_amount");
                        double saturated_fat_amount = products.getDouble("saturated_fat_amount");
                        double trans_fat = products.getDouble("trans_fat");
                        double polyunsat_fat = products.getDouble("polyunsat_fat");
                        double monounsat_fat = products.getDouble("monounsat_fat");
                        double cholesterol = products.getDouble("cholesterol");
                        double sodium = products.getDouble("sodium");
                        double carbohydrate_amount = products.getDouble("carbohydrate_amount");
                        double dietary_fiber = products.getDouble("dietary_fiber");
                        double total_sugars = products.getDouble("total_sugars");
                        double incl_added_sugars = products.getDouble("incl_added_sugars");
                        double protein_amount = products.getLong("protein_amount");
                        String line = "id: " + id + "\n" +
                                "Barcode: " + barcode_id + "\n" +
                                "Product: " + product_name + "\n" +
                                "Calories: " + calories + "\n" +
                                "Total Fat: " + fat_amount + "g\n" +
                                "Saturated Fat: " + saturated_fat_amount + "g\n" +
                                "Trans Fat: " + trans_fat + "g\n" +
                                "Polyunsaturated Fat: " + polyunsat_fat + "g\n" +
                                "Monounsaturated Fat: " + monounsat_fat + "g\n" +
                                "Cholesterol: " + cholesterol + "mg\n" +
                                "Sodium: " + sodium + "mg\n" +
                                "Total Carbohydrates: " + carbohydrate_amount + "g\n" +
                                "Dietary Fiber: " + dietary_fiber + "g\n" +
                                "Total Sugars:" + total_sugars + "g\n" +
                                "Incl. Added Sugars: " + incl_added_sugars + "g\n" +
                                "Protein: " + protein_amount + "g";
                        adapter.add(line);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No product info yet", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void goToLogin(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


}

