package kh.edu.rupp.homeworkmanagerapp

/**
 * QuoteResponse is a data class used to hold the information received from the Quote API.
 * In Kotlin, 'data class' is perfect for objects that only store data.
 */
data class QuoteResponse(
    // The main text of the motivational quote
    val content: String
)
