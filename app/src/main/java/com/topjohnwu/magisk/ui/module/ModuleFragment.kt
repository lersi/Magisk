package com.topjohnwu.liorsmagic.ui.module

import android.os.Bundle
import android.view.View
import com.topjohnwu.liorsmagic.R
import com.topjohnwu.liorsmagic.arch.BaseFragment
import com.topjohnwu.liorsmagic.arch.viewModel
import com.topjohnwu.liorsmagic.core.utils.MediaStoreUtils.displayName
import com.topjohnwu.liorsmagic.databinding.FragmentModuleMd2Binding
import rikka.recyclerview.addEdgeSpacing
import rikka.recyclerview.addInvalidateItemDecorationsObserver
import rikka.recyclerview.addItemSpacing
import rikka.recyclerview.fixEdgeEffect

class ModuleFragment : BaseFragment<FragmentModuleMd2Binding>() {

    override val layoutRes = R.layout.fragment_module_md2
    override val viewModel by viewModel<ModuleViewModel>()

    override fun onStart() {
        super.onStart()
        activity?.title = resources.getString(R.string.modules)
        viewModel.data.observe(this) {
            it ?: return@observe
            val displayName = runCatching { it.displayName }.getOrNull() ?: return@observe
            viewModel.requestInstallLocalModule(it, displayName)
            viewModel.data.value = null
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.moduleList.apply {
            addEdgeSpacing(top = R.dimen.l_50, bottom = R.dimen.l1)
            addItemSpacing(R.dimen.l1, R.dimen.l_50, R.dimen.l1)
            fixEdgeEffect()
            post { addInvalidateItemDecorationsObserver() }
        }
    }

    override fun onPreBind(binding: FragmentModuleMd2Binding) = Unit

}
