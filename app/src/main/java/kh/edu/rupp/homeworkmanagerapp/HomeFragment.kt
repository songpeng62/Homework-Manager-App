package kh.edu.rupp.homeworkmanagerapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * HomeFragment is the main dashboard for the user.
 * It displays a summary and provides a motivational quote fetched from an API.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 1. Inflate the layout for this fragment (fragment_home.xml)
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // 2. Connect the UI elements to our code
        val btnViewHomework: Button = view.findViewById(R.id.btnViewHomework)
        val btnAddHomework: Button = view.findViewById(R.id.btnAddHomework)
        val tvQuoteContent: TextView = view.findViewById(R.id.tvQuoteContent)

        // 3. Set up the View Homework button to switch to the list screen
        btnViewHomework.setOnClickListener {
            val listFragment = HomeworkListFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, listFragment)
                .addToBackStack(null) // This lets the user go back to Home
                .commit()
        }

        // 4. Set up the Add Homework button to switch to the add screen
        btnAddHomework.setOnClickListener {
            val addFragment = AddHomeworkFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, addFragment)
                .addToBackStack(null) // This lets the user go back to Home
                .commit()
        }

        // 5. Load a motivational quote using Retrofit
        loadMotivationalQuote(tvQuoteContent)

        return view
    }

    /**
     * This function fetches a random quote from the internet and displays it.
     */
    private fun loadMotivationalQuote(textView: TextView) {
        // Initialize the Retrofit service
        val apiService = QuoteApiService.create()

        // Make an asynchronous network call
        apiService.getRandomQuote().enqueue(object : Callback<QuoteResponse> {
            override fun onResponse(call: Call<QuoteResponse>, response: Response<QuoteResponse>) {
                // If the server responds successfully
                if (response.isSuccessful) {
                    val quoteBody = response.body()
                    // Display the quote text in the TextView
                    textView.text = "\"${quoteBody?.content}\""
                } else {
                    // Show a standard error message if the response failed
                    textView.text = "Failed to load quote"
                }
            }

            override fun onFailure(call: Call<QuoteResponse>, t: Throwable) {
                // Show error text if there is no internet or a connection error
                textView.text = "Failed to load quote"
            }
        })
    }
}
