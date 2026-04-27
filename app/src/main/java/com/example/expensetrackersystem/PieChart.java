package com.example.expensetrackersystem;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetrackersystem.model.expenseModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;

public class PieChart extends AppCompatActivity {

    private List<String> xData = new ArrayList<>();
    private HashMap<String, String> map;
    private PieChartView pieChartView;
    private List<SliceValue> pieData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        pieChartView = findViewById(R.id.chart);

        DatabaseHandlerExpense databaseHandlerExpense = new DatabaseHandlerExpense(PieChart.this);

        addData(databaseHandlerExpense);
        getEntries();

        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true).setValueLabelTextSize(14);
        pieChartData.setHasCenterCircle(true).setCenterText1("Expenses").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#0097A7"));
        pieChartView.setPieChartData(pieChartData);

        pieChartView.setOnValueTouchListener(new PieChartOnValueTouchListener());
    }

    private class PieChartOnValueTouchListener implements PieChartOnValueSelectListener {
        @Override
        public void onValueSelected(int arcIndex, SliceValue value) {
            String type = xData.get(arcIndex); // Get the type of the selected slice
            int amount = (int) value.getValue(); // Get the amount of the selected slice
            String toastMessage = "Type: " + type + "\nAmount: " + amount;
            Toast.makeText(PieChart.this, toastMessage, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // No action needed on deselection
        }
    }

    private void addData(DatabaseHandlerExpense databaseHandlerExpense) {
        List<expenseModel> expenseModelList = databaseHandlerExpense.getAllExpense();

        for (expenseModel model : expenseModelList) {
            xData.add(model.getType());
        }

        map = new HashMap<>();
        for (expenseModel model : expenseModelList) {
            int amount = Integer.parseInt(model.getAmount());
            if (map.containsKey(model.getType())) {
                int a = Integer.parseInt(map.get(model.getType()));
                map.put(model.getType(), String.valueOf(a + amount));
            } else {
                map.put(model.getType(), model.getAmount());
            }
        }
    }

    private void getEntries() {
        pieData = new ArrayList<>();

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.MAGENTA);
        colors.add(Color.BLUE);
        colors.add(Color.YELLOW);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.CYAN);
        colors.add(Color.GRAY);




        int totalAmount = 0;
        for (String type : xData) {
            totalAmount += Integer.parseInt(map.get(type));
        }

        int i = 0;
        for (String type : xData) {
            int amount = Integer.parseInt(map.get(type));
            float percentage = (float) amount / totalAmount * 100;
            pieData.add(new SliceValue(amount, colors.get(i % 5)).setLabel(type + " (" + String.format("%.2f", percentage) + "%)"));
            i++;
        }
    }
}
