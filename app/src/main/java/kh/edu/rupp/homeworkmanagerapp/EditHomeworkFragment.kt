package kh.edu.rupp.homeworkmanagerapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 * EditHomeworkFragment allows users to modify an existing homework task.
 * It receives the current data and pre-fills the input fields.
 */
class EditHomeworkFragment : Fragment() {

    // These variables will hold the old data passed from the list
    private var oldSubject: String? = null
    private var oldTitle: String? = null
    private var oldDeadline: String? = null
    private var oldStatus: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Retrieve the data passed through arguments
        arguments?.let {
            oldSubject = it.getString("subject")
            oldTitle = it.getString("title")
            oldDeadline = it.getString("deadline")
            oldStatus = it.getString("status")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 1. Inflate the layout for this fragment (connect to fragment_edit_homework.xml)
        val view = inflater.inflate(R.layout.fragment_edit_homework, container, false)

        // 2. Find the input fields and buttons
        val etSubject: EditText = view.findViewById(R.id.etEditSubject)
        val etTitle: EditText = view.findViewById(R.id.etEditHomeworkTitle)
        val etDeadline: EditText = view.findViewById(R.id.etEditDeadline)
        val btnUpdate: Button = view.findViewById(R.id.btnUpdateHomework)
        val btnBack: Button = view.findViewById(R.id.btnBackEdit)

        // 3. Fill the EditTexts with the existing data
        etSubject.setText(oldSubject)
        etTitle.setText(oldTitle)
        etDeadline.setText(oldDeadline)

        // 4. Handle the "Back" button click
        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 5. Handle the "Update" button click
        btnUpdate.setOnClickListener {
            val newSubject = etSubject.text.toString().trim()
            val newTitle = etTitle.text.toString().trim()
            val newDeadline = etDeadline.text.toString().trim()

            if (newSubject.isNotEmpty() && newTitle.isNotEmpty() && newDeadline.isNotEmpty()) {
                
                // Construct the old and new models for identification
                val oldModel = HomeworkModel(oldSubject!!, oldTitle!!, oldDeadline!!, oldStatus!!)
                val newFormattedData = "$newSubject|$newTitle|$newDeadline|$oldStatus"

                // A. Delete the old version from storage
                HomeworkStorage.deleteHomework(requireContext(), oldModel)
                
                // B. Save the new updated version
                HomeworkStorage.saveHomework(requireContext(), newFormattedData)

                Toast.makeText(requireContext(), "Homework Updated", Toast.LENGTH_SHORT).show()
                
                // Go back to the list
                parentFragmentManager.popBackStack()
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    /**
     * Helper companion object to create a new instance of this fragment with data.
     */
    companion object {
        fun newInstance(model: HomeworkModel): EditHomeworkFragment {
            val fragment = EditHomeworkFragment()
            val args = Bundle()
            args.putString("subject", model.subject)
            args.putString("title", model.title)
            args.putString("deadline", model.deadline)
            args.putString("status", model.status)
            fragment.arguments = args
            return fragment
        }
    }
}
