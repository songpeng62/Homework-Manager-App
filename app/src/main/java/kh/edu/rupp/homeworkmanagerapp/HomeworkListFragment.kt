package kh.edu.rupp.homeworkmanagerapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * HomeworkListFragment displays the list of homework tasks saved in internal storage.
 * It handles loading data from a file, displaying it in a list, and deleting tasks.
 */
class HomeworkListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Connect this fragment to its XML layout file
        return inflater.inflate(R.layout.fragment_homework_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Handle the "Back" button click to return to the Home dashboard
        val btnBackHome: View = view.findViewById(R.id.btnBackHome)
        btnBackHome.setOnClickListener {
            val homeFragment = HomeFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, homeFragment)
                .commit()
        }

        // 2. Start the process of loading and displaying data
        displayHomeworkList(view)
    }

    /**
     * This helper function reads the saved homework, prepares the list, 
     * and sets up the RecyclerView.
     */
    private fun displayHomeworkList(view: View) {
        // 3. Read the raw text lines from our internal "homework.txt" file
        val rawDataList = HomeworkStorage.readHomework(requireContext())

        // 4. Convert those text lines into a list of HomeworkModel objects
        val homeworkList = mutableListOf<HomeworkModel>()
        for (line in rawDataList) {
            val parts = line.split("|")
            if (parts.size == 4) {
                val model = HomeworkModel(
                    subject = parts[0],
                    title = parts[1],
                    deadline = parts[2],
                    status = parts[3]
                )
                homeworkList.add(model)
            }
        }

        // 5. Find the RecyclerView and set its LayoutManager (Vertical List)
        val recyclerHomework: RecyclerView = view.findViewById(R.id.recyclerHomework)
        recyclerHomework.layoutManager = LinearLayoutManager(requireContext())

        // 6. Initialize the Adapter with our list and a Delete Listener
        val adapter = HomeworkAdapter(homeworkList) { selectedHomework ->
            
            // --- This code runs when the "Delete" button is clicked ---
            
            // A. Remove the homework from the internal file storage
            HomeworkStorage.deleteHomework(requireContext(), selectedHomework)
            
            // B. Show a confirmation Toast message
            Toast.makeText(requireContext(), "Homework Deleted", Toast.LENGTH_SHORT).show()
            
            // C. Refresh the RecyclerView by re-loading the updated data
            displayHomeworkList(view)
        }

        // 7. Connect the Adapter to the RecyclerView to show the items
        recyclerHomework.adapter = adapter
    }
}
