package kh.edu.rupp.homeworkmanagerapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * MainActivity is the main container
 * for all fragments in the app.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Connect XML layout
        setContentView(R.layout.activity_main)

        // Load HomeFragment only once
        if (savedInstanceState == null) {

            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    HomeFragment()
                )
                .commit()
        }
    }
}