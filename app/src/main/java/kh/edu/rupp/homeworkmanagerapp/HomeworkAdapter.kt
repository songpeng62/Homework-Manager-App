package kh.edu.rupp.homeworkmanagerapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * HomeworkAdapter connects the list of homework data to the RecyclerView.
 * It now includes a deleteListener to handle when a user wants to remove a task.
 */
class HomeworkAdapter(
    private val homeworkList: List<HomeworkModel>,
    private val deleteListener: (HomeworkModel) -> Unit // Requirement: Add delete click listener parameter
) : RecyclerView.Adapter<HomeworkAdapter.HomeworkViewHolder>() {

    /**
     * ViewHolder holds the visual elements for a single row.
     */
    class HomeworkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSubject: TextView = itemView.findViewById(R.id.tvSubject)
        val tvHomeworkTitle: TextView = itemView.findViewById(R.id.tvHomeworkTitle)
        val tvDeadline: TextView = itemView.findViewById(R.id.tvDeadline)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val btnDelete: Button = itemView.findViewById(R.id.btnDeleteHomework) // Find the Delete button
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeworkViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_homework, parent, false)
        return HomeworkViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeworkViewHolder, position: Int) {
        val homework = homeworkList[position]
        
        // Bind the data from the model to the views
        holder.tvSubject.text = homework.subject
        holder.tvHomeworkTitle.text = homework.title
        holder.tvDeadline.text = homework.deadline
        holder.tvStatus.text = "Status: ${homework.status}"

        // Handle the Delete button click
        holder.btnDelete.setOnClickListener {
            // Requirement: Pass selected HomeworkModel item when delete button clicked
            deleteListener(homework)
        }
    }

    override fun getItemCount(): Int {
        return homeworkList.size
    }
}
