package com.vitaliimalone.simpletodo.presentation.utils.duedatepopup

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import com.google.android.material.textview.MaterialTextView
import com.vitaliimalone.simpletodo.R
import com.vitaliimalone.simpletodo.presentation.models.HomeTab
import com.vitaliimalone.simpletodo.presentation.utils.DateTimeUtils
import com.vitaliimalone.simpletodo.presentation.utils.Dialogs
import com.vitaliimalone.simpletodo.presentation.utils.Res
import com.vitaliimalone.simpletodo.presentation.views.DefaultDividerItemDecoration
import kotlinx.android.synthetic.main.pick_due_date_popup.view.*
import org.threeten.bp.OffsetDateTime

class DueDatePopup(
        context: Context, private val anchorView: View, private val onDatePicked: ((OffsetDateTime) -> Unit)
) : PopupWindow(context) {
    private val screenPosition = IntArray(2)

    init {
        anchorView.getLocationOnScreen(screenPosition)
        screenPosition[0] += getAdditionalStartPadding(anchorView)
        isFocusable = true
        isOutsideTouchable = false
        contentView = LayoutInflater.from(context).inflate(R.layout.pick_due_date_popup, null, false)
    }

    override fun setContentView(contentView: View?) {
        super.setContentView(contentView)
        contentView?.let {
            it.apply {
                rootView.setOnClickListener { dismiss() }
                dueDatePopupRecyclerView.addItemDecoration(
                        DefaultDividerItemDecoration(
                                context,
                                marginLeft = Res.dimen(R.dimen.m_spacing),
                                marginRight = Res.dimen(R.dimen.m_spacing)))
                dueDatePopupRecyclerView.adapter = DueDatePopupAdapter { popupItem ->
                    when (popupItem) {
                        DueDatePopupAdapter.DueDatePopupItem.TODAY -> {
                            onDatePicked.invoke(OffsetDateTime.now())
                        }
                        DueDatePopupAdapter.DueDatePopupItem.TOMORROW -> {
                            onDatePicked.invoke(OffsetDateTime.now().plusDays(1))
                        }
                        DueDatePopupAdapter.DueDatePopupItem.END_OF_WEEK -> {
                            onDatePicked.invoke(DateTimeUtils.getStartEndDateForTab(HomeTab.WEEK).second)
                        }
                        DueDatePopupAdapter.DueDatePopupItem.PICK -> {
                            Dialogs.showDatePickerDialog(context, OffsetDateTime.now()) { date ->
                                onDatePicked.invoke(date)
                            }
                        }
                    }
                    dismiss()
                }
            }
        }
        showAtLocation(anchorView, Gravity.NO_GRAVITY, screenPosition[0], screenPosition[1])
    }

    private fun getAdditionalStartPadding(anchorView: View): Int {
        var drawableWidth = 0
        if (anchorView is MaterialTextView) {
            drawableWidth = anchorView.compoundDrawables[0]?.intrinsicWidth ?: 0 + anchorView.compoundDrawablePadding
        }
        return drawableWidth + anchorView.paddingStart
    }
}