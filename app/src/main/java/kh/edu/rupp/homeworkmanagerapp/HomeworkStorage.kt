package kh.edu.rupp.homeworkmanagerapp

import android.content.Context
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader

/**
 * HomeworkStorage is a helper object for managing homework data in internal storage.
 * It handles saving, reading, deleting, and updating tasks from a private text file.
 */
object HomeworkStorage {

    private const val FILE_NAME = "homework.txt"

    /**
     * Saves a new homework entry to the internal file.
     * Use MODE_APPEND to add new data to the end of the file.
     */
    fun saveHomework(context: Context, data: String) {
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_APPEND)
            val dataWithNewline = data + "\n"
            fileOutputStream.write(dataWithNewline.toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                fileOutputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Reads all homework entries from the internal file.
     * Returns a List of Strings, where each string is one homework task.
     */
    fun readHomework(context: Context): List<String> {
        val homeworkList = mutableListOf<String>()
        var fileInputStream: FileInputStream? = null

        try {
            fileInputStream = context.openFileInput(FILE_NAME)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)

            var line: String? = bufferedReader.readLine()
            while (line != null) {
                if (line.isNotBlank()) {
                    homeworkList.add(line)
                }
                line = bufferedReader.readLine()
            }
        } catch (e: IOException) {
            // File not found is expected on the first run
            e.printStackTrace()
        } finally {
            try {
                fileInputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return homeworkList
    }

    /**
     * Deletes a specific homework task from the internal storage.
     */
    fun deleteHomework(context: Context, targetHomework: HomeworkModel) {
        val allTasks = readHomework(context)
        val targetString = "${targetHomework.subject}|${targetHomework.title}|${targetHomework.deadline}|${targetHomework.status}"
        
        // Filter out the task we want to remove
        val updatedTasks = allTasks.filter { it != targetString }
        
        // Rewrite the file with the remaining tasks
        rewriteFile(context, updatedTasks)
    }

    /**
     * Updates an existing homework task with new information.
     * @param oldHomework The existing model used to find the entry in the file.
     * @param newHomeworkString The updated data formatted as a string (Subject|Title|Deadline|Status).
     */
    fun updateHomework(context: Context, oldHomework: HomeworkModel, newHomeworkString: String) {
        // 1. Read all existing homework tasks
        val allTasks = readHomework(context)

        // 2. Convert the old model back into its string format to find it in the list
        val targetString = "${oldHomework.subject}|${oldHomework.title}|${oldHomework.deadline}|${oldHomework.status}"

        // 3. Create a new list where the old task is replaced by the new one
        val updatedTasks = allTasks.map { task ->
            if (task == targetString) newHomeworkString else task
        }

        // 4. Overwrite the entire file with the updated list
        rewriteFile(context, updatedTasks)
    }

    /**
     * Private helper function to overwrite the homework file with a new list of data.
     */
    private fun rewriteFile(context: Context, taskList: List<String>) {
        var fileOutputStream: FileOutputStream? = null
        try {
            // MODE_PRIVATE replaces the file content entirely
            fileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
            for (task in taskList) {
                val line = task + "\n"
                fileOutputStream.write(line.toByteArray())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                fileOutputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
