package kh.edu.rupp.homeworkmanagerapp

import android.content.Context
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader

/**
 * HomeworkStorage is a helper object for managing homework data in internal storage.
 * It handles saving, reading, and deleting tasks from a private text file.
 */
object HomeworkStorage {

    private const val FILE_NAME = "homework.txt"

    /**
     * Saves a homework entry to the internal file.
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
     * Since we use a text file, we must read all tasks, remove the target, and rewrite the file.
     */
    fun deleteHomework(context: Context, targetHomework: HomeworkModel) {
        // 1. Read all existing homework tasks as raw strings
        val allTasks = readHomework(context)

        // 2. Convert the target model back into the string format used in the file
        val targetString = "${targetHomework.subject}|${targetHomework.title}|${targetHomework.deadline}|${targetHomework.status}"

        // 3. Filter the list to exclude the task we want to delete
        val updatedTasks = allTasks.filter { it != targetString }

        // 4. Overwrite the entire file with the updated list
        var fileOutputStream: FileOutputStream? = null
        try {
            // MODE_PRIVATE replaces the file content instead of adding to it
            fileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
            for (task in updatedTasks) {
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
