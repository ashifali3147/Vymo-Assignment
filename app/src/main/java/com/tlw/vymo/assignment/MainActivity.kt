package com.tlw.vymo.assignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tlw.vymo.assignment.designsystem.theme.VymoTheme
import com.tlw.vymo.assignment.lead.LeadFormRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VymoTheme {
                LeadFormRoute()
            }
        }
    }
}
