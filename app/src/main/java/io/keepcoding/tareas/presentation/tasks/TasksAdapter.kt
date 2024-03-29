package io.keepcoding.tareas.presentation.tasks

import android.animation.ValueAnimator
import android.content.Intent
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.keepcoding.tareas.R
import io.keepcoding.tareas.domain.model.Task
import io.keepcoding.tareas.presentation.add_task.AddTaskActivity
import io.keepcoding.tareas.presentation.detail.DetailActivity
import kotlinx.android.synthetic.main.item_task.view.*
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import java.util.*

class TasksAdapter(val listener: Listener) : ListAdapter<Task, TasksAdapter.TaskViewHolder>(TaskDiffUtil() ) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }



    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(task: Task) {
            with(itemView) {
                cardContentText.text = task.content
                cardDescriptionTask.text = task.description


                taskFinishedCheck.isChecked = task.isFinished

                if (task.isFinished) {
                    applyStrikeThrough(cardContentText, task.content)
                } else {
                    removeStrikeThrough(cardContentText, task.content)
                }

                val formatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(
                        Locale.UK
                ).withZone(ZoneId.systemDefault())

                cardDate.text = formatter.format(task.createdAt).toString()

                if (task.isHighPriority) {
                    cardPriority.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent))
                }

                taskFinishedCheck.setOnClickListener {
                    listener.onClick(task)

                    if (taskFinishedCheck.isChecked) {
                        applyStrikeThrough(cardContentText, task.content, animate = true)
                    } else {
                        removeStrikeThrough(cardContentText, task.content, animate = true)
                    }
                }

                cardView.setOnClickListener {

                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra("obj", task)
                    intent.putExtra("id", task.id)
                    startActivity(itemView.context, intent, null)
                }


                ibDelete.setOnClickListener {
                   listener.onDelete(task)
                    notifyDataSetChanged()
                }


                ibEdit.setOnClickListener {

                    val intent = Intent(itemView.context, AddTaskActivity::class.java)
                    intent.putExtra("obj", task)
                    intent.putExtra("destiny", "edit")
                    startActivity(itemView.context, intent, null)

                }
            }
        }





        private fun applyStrikeThrough(view: TextView, content: String, animate: Boolean = false) {
            val span = SpannableString(content)
            val spanStrike = StrikethroughSpan()

            if (animate) {
                ValueAnimator.ofInt(content.length).apply {
                    duration = 300
                    interpolator = FastOutSlowInInterpolator()
                    addUpdateListener {
                        span.setSpan(spanStrike, 0, it.animatedValue as Int, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        view.text = span
                    }
                }.start()
            } else {
                span.setSpan(spanStrike, 0, content.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                view.text = span
            }
        }

        private fun removeStrikeThrough(view: TextView, content: String, animate: Boolean = false) {
            val span = SpannableString(content)
            val spanStrike = StrikethroughSpan()

            if (animate) {
                ValueAnimator.ofInt(content.length, 0).apply {
                    duration = 300
                    interpolator = FastOutSlowInInterpolator()
                    addUpdateListener {
                        span.setSpan(spanStrike, 0, it.animatedValue as Int, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        view.text = span
                    }
                }.start()
            } else {
                view.text = content
            }
        }




    }

    /*fun removeItem(position: Int) {
        list.removeAt(position)
    }*/

}