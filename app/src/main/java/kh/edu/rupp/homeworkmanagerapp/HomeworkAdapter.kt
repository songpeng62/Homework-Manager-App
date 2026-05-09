package kh.edu.rupp.homeworkmanagerapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * HomeworkAdapter connects our list of homework data to the RecyclerView.
 * It manages how each item in the list is created and updated, including Edit and Delete actions.
 */
class HomeworkAdapter(
    private val homeworkList: List<HomeworkModel>,
    private val editListener: (HomeworkModel) -> Unit,   // Function to handle Edit clicks
    private val deleteListener: (HomeworkModel) -> Unit // Function to handle Delete clicks
) : RecyclerView.Adapter<HomeworkAdapter.HomeworkViewHolder>() {

    /**
     * ViewHolder holds references to the views for a single list item.
     * This helps the list scroll smoothly.
     */
    class HomeworkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSubject: TextView = itemView.findViewById(R.id.tvSubject)
        val tvHomeworkTitle: TextView = itemView.findViewById(R.id.tvHomeworkTitle)
        val tvDeadline: TextView = itemView.findViewById(R.id.tvDeadline)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        
        // Connect the Edit and Delete buttons from item_homework.xml
        val btnEdit: Button = itemView.findViewById(R.id.btnEditHomework)
        val btnDelete: Button = itemView.findViewById(R.id.btnDeleteHomework)
    }

    /**
     * Called when the RecyclerView needs a new row layout to be created.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeworkViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_homework, parent, false)
        return HomeworkViewHolder(view)
    }

    /**
     * Called to display data in a specific row.
     */
    override fun onBindViewHolder(holder: HomeworkViewHolder, position: Int) {
        val homework = homeworkList[position]
        
        // Bind the data from the model to the text views
        holder.tvSubject.text = homework.subject
        holder.tvHomeworkTitle.text = homework.title
        holder.tvDeadline.text = homework.deadline
        holder.tvStatus.text = "Status: ${homework.status}"

        // 1. Handle Edit button click
        holder.btnEdit.setOnClickListener {
            // Pass the selected homework item to the edit listener
            editListener(homework)
        }

        // 2. Handle Delete button click
        holder.btnDelete.setOnClickListener {
            // Pass the selected homework item to the delete listener
            deleteListener(homework)
        }
    }

    /**
     * Returns the total number of items in the list.
     */
    override fun getItemCount(): Int {
        return homeworkList.size
    }
}
