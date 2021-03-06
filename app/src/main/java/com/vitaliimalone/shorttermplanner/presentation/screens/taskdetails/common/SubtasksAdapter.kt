package com.vitaliimalone.shorttermplanner.presentation.screens.taskdetails.common

import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.vitaliimalone.shorttermplanner.R
import com.vitaliimalone.shorttermplanner.domain.models.Subtask
import com.vitaliimalone.shorttermplanner.domain.models.Task
import com.vitaliimalone.shorttermplanner.presentation.utils.Res
import com.vitaliimalone.shorttermplanner.presentation.utils.extensions.clearFocusOnDoneClick
import com.vitaliimalone.shorttermplanner.presentation.utils.extensions.showKeyboard
import com.vitaliimalone.shorttermplanner.presentation.utils.extensions.showStrikeThrough
import com.vitaliimalone.shorttermplanner.presentation.utils.extensions.trimmed
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.task_details_add_new_subtasks_list_item.*
import kotlinx.android.synthetic.main.task_details_subtasks_list_item.*

class SubtasksAdapter(
    private val task: Task,
    private val updateTask: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var subtaskListItems = task.subtasks.map { SubtaskListItem(it) }.toMutableList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        SubtaskViewType.SUBTASK.ordinal -> {
            TaskViewHolder(inflateView(R.layout.task_details_subtasks_list_item, parent))
        }
        SubtaskViewType.ADDNEW.ordinal -> {
            AddNewTaskViewHolder(inflateView(R.layout.task_details_add_new_subtasks_list_item, parent))
        }
        else -> {
            throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (holder) {
        is TaskViewHolder -> holder.bind(subtaskListItems[position], position)
        is AddNewTaskViewHolder -> holder.bind()
        else -> throw IllegalArgumentException()
    }

    override fun getItemCount() = subtaskListItems.size + 1

    override fun getItemViewType(position: Int) = when (position) {
        subtaskListItems.size -> SubtaskViewType.ADDNEW.ordinal
        else -> SubtaskViewType.SUBTASK.ordinal
    }

    private fun inflateView(@LayoutRes resource: Int, parent: ViewGroup) =
        LayoutInflater.from(parent.context).inflate(resource, parent, false)

    private fun addSubtask() {
        subtaskListItems.forEach { it.isFocused = false }
        val subtask = Subtask()
        task.subtasks.add(subtask)
        subtaskListItems.add(SubtaskListItem(subtask, true))
        notifyItemInserted(subtaskListItems.lastIndex)
        if (subtaskListItems.lastIndex - 1 >= 0) {
            notifyItemChanged(subtaskListItems.lastIndex - 1)
        }
        updateTask.invoke()
    }

    private fun deleteSubtask(subtaskListItem: SubtaskListItem) {
        val deletedIndex = subtaskListItems.indexOf(subtaskListItem)
        subtaskListItems.removeAll { it.subtask.id == subtaskListItem.subtask.id }
        task.subtasks.removeAll { it.id == subtaskListItem.subtask.id }
        subtaskListItems.forEach { it.isFocused = false }
        notifyItemRemoved(deletedIndex)
        if (deletedIndex == subtaskListItems.lastIndex + 1) {
            notifyItemChanged(subtaskListItems.lastIndex)
        }
        updateTask.invoke()
    }

    inner class TaskViewHolder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(subtaskListItem: SubtaskListItem, position: Int) {
            subtaskTitleEditText.setRawInputType(InputType.TYPE_CLASS_TEXT)
            subtaskTitleEditText.setText(subtaskListItem.subtask.title)
            subtaskTitleEditText.hint = Res.string(R.string.add_subtask_hint)
            subtaskTitleEditText.showStrikeThrough(subtaskListItem.subtask.isDone)
            if (subtaskListItem.isFocused) {
                subtaskTitleEditText.post {
                    subtaskTitleEditText.requestFocus()
                    subtaskTitleEditText.showKeyboard()
                }
            }
            subtaskTitleEditText.clearFocusOnDoneClick()
            subtaskTitleEditText.addTextChangedListener {
                subtaskListItem.subtask.title = it.trimmed
            }
            doneCheckBox.isChecked = subtaskListItem.subtask.isDone
            doneCheckBox.setOnCheckedChangeListener { _, isChecked ->
                subtaskListItem.subtask.isDone = isChecked
                subtaskTitleEditText.showStrikeThrough(isChecked)
            }
            botLineView.isVisible = subtaskListItems.lastIndex != position
            deleteImageView.setOnClickListener {
                subtaskTitleEditText.clearFocus()
                deleteSubtask(subtaskListItem)
            }
        }
    }

    inner class AddNewTaskViewHolder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind() {
            addSubtaskContainer.setOnClickListener {
                addSubtask()
            }
            addSubtaskTextView.hint = Res.string(R.string.add_subtask_hint)
        }
    }

    enum class SubtaskViewType {
        SUBTASK, ADDNEW
    }
}
