package com.topjohnwu.liorsmagic.ui.install

import com.topjohnwu.liorsmagic.R
import com.topjohnwu.liorsmagic.arch.BaseFragment
import com.topjohnwu.liorsmagic.arch.viewModel
import com.topjohnwu.liorsmagic.databinding.FragmentInstallMd2Binding

class InstallFragment : BaseFragment<FragmentInstallMd2Binding>() {

    override val layoutRes = R.layout.fragment_install_md2
    override val viewModel by viewModel<InstallViewModel>()

    override fun onStart() {
        super.onStart()
        requireActivity().setTitle(R.string.install)
    }
}
