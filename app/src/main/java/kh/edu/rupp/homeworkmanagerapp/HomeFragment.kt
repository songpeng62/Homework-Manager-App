package kh.edu.rupp.homeworkmanagerapp

import android.content.Intent
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
 * It displays homework summary, motivational quote,
 * weather updates, and education news.
 */
class HomeFragment : Fragment() {

    companion object {
        private var cachedQuote: String? = null
        private var cachedWeather: String? = null
        private var cachedNews: String? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate layout
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Connect buttons
        val btnViewHomework: Button =
            view.findViewById(R.id.btnViewHomework)

        val btnAddHomework: Button =
            view.findViewById(R.id.btnAddHomework)

        val btnLogout: TextView =
            view.findViewById(R.id.btnLogout)

        // Connect text views
        val tvTaskSummary: TextView =
            view.findViewById(R.id.tvTaskSummary)

        val tvQuoteContent: TextView =
            view.findViewById(R.id.tvQuoteContent)

        val tvWeatherContent: TextView =
            view.findViewById(R.id.tvWeatherContent)

        val tvNewsContent: TextView =
            view.findViewById(R.id.tvNewsContent)

        // Open Homework List screen
        btnViewHomework.setOnClickListener {

            parentFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    HomeworkListFragment()
                )
                .addToBackStack(null)
                .commit()
        }

        // Open Add Homework screen
        btnAddHomework.setOnClickListener {

            parentFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    AddHomeworkFragment()
                )
                .addToBackStack(null)
                .commit()
        }

        // Logout
        btnLogout.setOnClickListener {

            val intent = Intent(
                requireContext(),
                LoginActivity::class.java
            )

            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }

        // Update pending homework count
        updateTaskSummary(tvTaskSummary)

        // Load Quote (cached if already loaded)
        if (cachedQuote != null) {

            tvQuoteContent.text = cachedQuote

        } else {

            loadMotivationalQuote(tvQuoteContent)
        }

        // Load Weather (cached if already loaded)
        if (cachedWeather != null) {

            tvWeatherContent.text = cachedWeather

        } else {

            loadWeatherData(tvWeatherContent)
        }

        // Load News (cached if already loaded)
        if (cachedNews != null) {

            tvNewsContent.text = cachedNews

        } else {

            loadEducationNews(tvNewsContent)
        }

        return view
    }

    /**
     * Update pending homework task summary
     */
    private fun updateTaskSummary(textView: TextView) {

        val rawDataList =
            HomeworkStorage.readHomework(requireContext())

        var pendingCount = 0

        for (line in rawDataList) {

            val parts = line.split("|")

            if (
                parts.size == 4 &&
                parts[3].trim() == "Pending"
            ) {

                pendingCount++
            }
        }

        textView.text =
            "You have $pendingCount pending tasks"
    }

    /**
     * Load motivational quote from API
     */
    private fun loadMotivationalQuote(
        textView: TextView
    ) {

        val apiService = QuoteApiService.create()

        apiService.getRandomQuote()
            .enqueue(object : Callback<QuoteResponse> {

                override fun onResponse(
                    call: Call<QuoteResponse>,
                    response: Response<QuoteResponse>
                ) {

                    if (response.isSuccessful) {

                        cachedQuote =
                            "\"${response.body()?.content}\""

                        textView.text = cachedQuote

                    } else {

                        cachedQuote =
                            "Focus on your goals today!"

                        textView.text = cachedQuote
                    }
                }

                override fun onFailure(
                    call: Call<QuoteResponse>,
                    t: Throwable
                ) {

                    cachedQuote =
                        "Stay organized and keep moving forward!"

                    textView.text = cachedQuote
                }
            })
    }

    /**
     * Load weather data from API
     */
    private fun loadWeatherData(
        textView: TextView
    ) {

        val apiService = WeatherApiService.create()

        val apiKey =
            "88fe09458dd46e0a2c0af2c5af6e94cb"

        apiService.getCurrentWeather(
            "Phnom Penh",
            "metric",
            apiKey
        ).enqueue(object : Callback<WeatherResponse> {

            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {

                if (response.isSuccessful) {

                    val weather = response.body()

                    if (weather != null) {

                        val temp =
                            weather.main.temp.toInt()

                        val desc =
                            weather.weather[0].description

                        cachedWeather =
                            "$temp°C, $desc"

                        textView.text = cachedWeather
                    }

                } else {

                    cachedWeather =
                        "Weather unavailable"

                    textView.text = cachedWeather
                }
            }

            override fun onFailure(
                call: Call<WeatherResponse>,
                t: Throwable
            ) {

                cachedWeather =
                    "Failed to load weather"

                textView.text = cachedWeather
            }
        })
    }

    /**
     * Load education news from API
     */
    private fun loadEducationNews(
        textView: TextView
    ) {

        val apiService = NewsApiService.create()

        val apiKey =
            "e22f8febc4f543dabc726177e0ddf035"

        apiService.getEducationNews(
            "education",
            "en",
            "publishedAt",
            apiKey
        ).enqueue(object : Callback<NewsResponse> {

            override fun onResponse(
                call: Call<NewsResponse>,
                response: Response<NewsResponse>
            ) {

                if (response.isSuccessful) {

                    val news = response.body()

                    if (
                        news != null &&
                        news.articles.isNotEmpty()
                    ) {

                        cachedNews =
                            news.articles[0].title

                        textView.text = cachedNews

                    } else {

                        cachedNews =
                            "No news found at the moment"

                        textView.text = cachedNews
                    }

                } else {

                    cachedNews =
                        "News temporarily unavailable"

                    textView.text = cachedNews
                }
            }

            override fun onFailure(
                call: Call<NewsResponse>,
                t: Throwable
            ) {

                cachedNews =
                    "Failed to load news"

                textView.text = cachedNews
            }
        })
    }
}