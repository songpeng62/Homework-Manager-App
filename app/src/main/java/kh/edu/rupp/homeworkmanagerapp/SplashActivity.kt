package kh.edu.rupp.homeworkmanagerapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

/**
 * SplashActivity is the first screen the user sees.
 * It introduces the app and provides a "Get Started" button.
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Sets the UI layout from activity_splash.xml
        setContentView(R.layout.activity_splash)

        // Hide the top action bar for a clean full-screen splash look
        supportActionBar?.hide()

        // Find the "Get Started" button by its ID
        val getStartedButton: Button = findViewById(R.id.btnGetStarted)

        // Set a click listener to the button
        getStartedButton.setOnClickListener {
            // Create an Intent to navigate to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            
            // Finish SplashActivity so the user can't go back to it
            finish()
        }
    }
}
