package kh.edu.rupp.homeworkmanagerapp

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * HomeworkAdapter connects homework data
 * to the RecyclerView.
 */
class HomeworkAdapter(

    private val homeworkList: List<HomeworkModel>,

    private val editListener: (HomeworkModel) -> Unit,

    private val deleteListener: (HomeworkModel) -> Unit

) : RecyclerView.Adapter<HomeworkAdapter.HomeworkViewHolder>() {

    /**
     * ViewHolder holds item views.
     */
    class HomeworkViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {

        val tvSubject: TextView =
            itemView.findViewById(R.id.tvSubject)

        val tvHomeworkTitle: TextView =
            itemView.findViewById(R.id.tvHomeworkTitle)

        val tvDeadline: TextView =
            itemView.findViewById(R.id.tvDeadline)

        val tvStatus: TextView =
            itemView.findViewById(R.id.tvStatus)

        val btnEdit: Button =
            itemView.findViewById(R.id.btnEditHomework)

        val btnDelete: Button =
            itemView.findViewById(R.id.btnDeleteHomework)
    }

    /**
     * Create RecyclerView item layout
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeworkViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_homework,
                parent,
                false
            )

        return HomeworkViewHolder(view)
    }

    /**
     * Bind homework data to item layout
     */
    override fun onBindViewHolder(
        holder: HomeworkViewHolder,
        position: Int
    ) {

        val homework = homeworkList[position]

        // Set text
        holder.tvSubject.text =
            homework.subject

        holder.tvHomeworkTitle.text =
            homework.title

        holder.tvDeadline.text =
            homework.deadline

        holder.tvStatus.text =
            "Status: ${homework.status}"

        // Edit button
        holder.btnEdit.setOnClickListener {

            editListener(homework)
        }

        // Delete button with confirmation dialog
        holder.btnDelete.setOnClickListener {

            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Delete Homework")
                .setMessage(
                    "Are you sure you want to delete this homework?"
                )

                .setPositiveButton("Delete") { _, _ ->

                    deleteListener(homework)
                }

                .setNegativeButton("Cancel", null)

                .show()
        }
    }

    /**
     * Return total item count
     */
    override fun getItemCount(): Int {

        return homeworkList.size
    }
}