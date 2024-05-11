package com.comp4521_project_gp4.ui.activity

import ToolbarFragment
import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.comp4521_project_gp4.R
import com.comp4521_project_gp4.ui.adapters.DashboardAdapter
import com.comp4521_project_gp4.viewmodel.DashboardViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate

class DashboardActivity : AppCompatActivity() {
  private val dashboardViewModel: DashboardViewModel by viewModels()
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_dashboard)
    
    // Dynamically add the Toolbar Fragment
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.toolbar_container, ToolbarFragment())
        .commit()
    }
    
    // Set up RecyclerView
    val recyclerView = findViewById<RecyclerView>(R.id.dashboard_recyclerview)
    val adapter = DashboardAdapter(emptyList())  // Initialize adapter with an empty list
    recyclerView.adapter = adapter
    recyclerView.layoutManager = LinearLayoutManager(this)
    
    // Observe the ViewModel's LiveData for changes and update the adapter
    dashboardViewModel.dashboardItems.observe(this) { items ->
      adapter.updateData(items)
    }
    
    val barChart = findViewById<BarChart>(R.id.barChartt)
    barChart.setDrawBarShadow(false)
    barChart.setDrawValueAboveBar(true)
    barChart.description.isEnabled = false
    barChart.setMaxVisibleValueCount(50)
    barChart.setPinchZoom(false)
    barChart.setDrawGridBackground(true)
    barChart.setBackgroundColor(Color.WHITE)
    /*
    * setDrawBarShadow(false): 禁用條形陰影。
    setDrawValueAboveBar(true): 設定條形圖上方顯示數值。
    description.isEnabled = false: 禁用圖表描述標籤。
    setMaxVisibleValueCount(50): 設定最多顯示50個條目，超過後會滾動。
    setPinchZoom(false): 禁用捏合縮放功能。
    setDrawGridBackground(true): 在圖表背景繪製網格。
    *
    使用數據集BarDataSet來封裝數據點，並為其設定一個標籤"Clients"。
    setColors: 設定條形圖使用的顏色（從預定義的材料設計顏色中選取）。
    valueTextColor同valueTextSize: 設定數據點上數字嘅顏色同字體大小。
     */
    
    
    val xAxisLabels = arrayOf("Ben", "May", "Tom", "Ana", "Ivy")
    val dataValues = listOf(
      BarEntry(0f, 80f),
      BarEntry(1f, 60f),
      BarEntry(2f, 70f),
      BarEntry(3f, 50f),
      BarEntry(4f, 90f)
    )
    
    val barDataSet = BarDataSet(dataValues, "")
    barDataSet.setColors(*ColorTemplate.MATERIAL_COLORS)
    barDataSet.valueTextColor = Color.BLACK
    barDataSet.valueTextSize = 16f
    
    val barData = BarData(barDataSet)
    barChart.data = barData
    barChart.animateY(3000)
    
    val xAxis = barChart.xAxis
    xAxis.position = XAxis.XAxisPosition.BOTTOM
    xAxis.setDrawGridLines(false)
    xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels)
    xAxis.granularity = 1f
    xAxis.labelCount = xAxisLabels.size
    xAxis.textColor = Color.GRAY
    
    
    val leftAxis = barChart.axisLeft
    leftAxis.isEnabled = false
    
    val rightAxis = barChart.axisRight
    //rightAxis.isEnabled = false
    rightAxis.textColor = Color.GRAY
    
    val legend = barChart.legend
    legend.isEnabled = false
    barChart.invalidate()
  }
}