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
 * HomeworkListFragment is the screen that shows all your saved homework.
 * It reads data from a file, displays it in a list, and lets you Edit or Delete tasks.
 */
class HomeworkListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Connect this fragment to the fragment_homework_list.xml layout
        return inflater.inflate(R.layout.fragment_homework_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Set up the Back button to return to the Home dashboard
        val btnBackHome: View = view.findViewById(R.id.btnBackHome)
        btnBackHome.setOnClickListener {
            val homeFragment = HomeFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, homeFragment)
                .commit()
        }

        // 2. Load the list of homework from the file and display it
        loadAndDisplayHomework(view)
    }

    /**
     * This helper function handles reading the data, converting it to objects,
     * and setting up the RecyclerView with Edit and Delete actions.
     */
    private fun loadAndDisplayHomework(view: View) {
        // 3. Read the raw text lines from our internal "homework.txt" file
        val rawDataList = HomeworkStorage.readHomework(requireContext())

        // 4. Convert those text lines into a list of HomeworkModel objects
        // Format in file is: Subject|Title|Deadline|Status
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

        // 5. Find the RecyclerView and set its LayoutManager (to show items in a vertical list)
        val recyclerHomework: RecyclerView = view.findViewById(R.id.recyclerHomework)
        recyclerHomework.layoutManager = LinearLayoutManager(requireContext())

        // 6. Create the Adapter and handle what happens when Edit or Delete is clicked
        val adapter = HomeworkAdapter(
            homeworkList,
            editListener = { selectedHomework ->
                // ACTION: Open the Edit screen and pass the current data
                val editFragment = EditHomeworkFragment.newInstance(selectedHomework)
                
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, editFragment)
                    .addToBackStack(null) // This allows the user to come back to the list
                    .commit()
            },
            deleteListener = { selectedHomework ->
                // ACTION: Remove the task from the file
                HomeworkStorage.deleteHomework(requireContext(), selectedHomework)
                
                // Show a message to confirm deletion
                Toast.makeText(requireContext(), "Homework Deleted", Toast.LENGTH_SHORT).show()
                
                // Refresh the list immediately so the item disappears
                loadAndDisplayHomework(view)
            }
        )

        // 7. Finally, attach the adapter to the RecyclerView
        recyclerHomework.adapter = adapter
    }
}
