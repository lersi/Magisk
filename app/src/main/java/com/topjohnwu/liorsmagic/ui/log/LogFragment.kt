package com.topjohnwu.liorsmagic.ui.log

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import com.topjohnwu.liorsmagic.R
import com.topjohnwu.liorsmagic.arch.BaseFragment
import com.topjohnwu.liorsmagic.arch.viewModel
import com.topjohnwu.liorsmagic.databinding.FragmentLogMd2Binding
import com.topjohnwu.liorsmagic.ui.MainActivity
import com.topjohnwu.liorsmagic.utils.MotionRevealHelper
import rikka.recyclerview.addEdgeSpacing
import rikka.recyclerview.addItemSpacing
import rikka.recyclerview.fixEdgeEffect

class LogFragment : BaseFragment<FragmentLogMd2Binding>(), MenuProvider {

    override val layoutRes = R.layout.fragment_log_md2
    override val viewModel by viewModel<LogViewModel>()
    override val snackbarView: View?
        get() = if (isLiorsmagicLogVisible) binding.logFilterSuperuser.snackbarContainer
                else super.snackbarView
    override val snackbarAnchorView get() = binding.logFilterToggle

    private var actionSave: MenuItem? = null
    private var isLiorsmagicLogVisible
        get() = binding.logFilter.isVisible
        set(value) {
            MotionRevealHelper.withViews(binding.logFilter, binding.logFilterToggle, value)
            actionSave?.isVisible = !value
            with(activity as MainActivity) {
                invalidateToolbar()
                requestNavigationHidden(value)
                setDisplayHomeAsUpEnabled(value)
            }
        }

    override fun onStart() {
        super.onStart()
        activity?.setTitle(R.string.logs)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.logFilterToggle.setOnClickListener {
            isLiorsmagicLogVisible = true
        }

        binding.logFilterSuperuser.logSuperuser.apply {
            addEdgeSpacing(bottom = R.dimen.l1)
            addItemSpacing(R.dimen.l1, R.dimen.l_50, R.dimen.l1)
            fixEdgeEffect()
        }
    }


    override fun onCreateMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_log_md2, menu)
        actionSave = menu.findItem(R.id.action_save)?.also {
            it.isVisible = !isLiorsmagicLogVisible
        }
    }

    override fun onMenuItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> viewModel.saveLiorsmagicLog()
            R.id.action_clear ->
                if (!isLiorsmagicLogVisible) viewModel.clearLiorsmagicLog()
                else viewModel.clearLog()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onPreBind(binding: FragmentLogMd2Binding) = Unit

    override fun onBackPressed(): Boolean {
        if (binding.logFilter.isVisible) {
            isLiorsmagicLogVisible = false
            return true
        }
        return super.onBackPressed()
    }

}
