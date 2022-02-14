package com.pchpsky.swivltesttask.feature_users.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pchpsky.swivltesttask.R
import com.pchpsky.swivltesttask.databinding.FragmentUserListBinding
import com.pchpsky.swivltesttask.feature_users.presentation.list_adapter.UserListAdapter
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserListFragment : Fragment(R.layout.fragment_user_list) {

    companion object {
        fun newInstance() = UserListFragment().apply {
            arguments = Bundle().apply {}
        }
    }

    private val viewModel: UsersViewModelImpl by viewModel()
    private var binding: FragmentUserListBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserListBinding.inflate(inflater, container, false)
        binding?.root

        return binding?.root!!
    }

    @OptIn(InternalCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.users.collect {
                binding?.userList?.layoutManager = LinearLayoutManager(requireContext())
                binding?.userList?.adapter = UserListAdapter(it)
                binding?.swipeRefresh?.isRefreshing = false
            }
        }

        binding?.swipeRefresh?.setOnRefreshListener {
            viewModel.onEvent(UsersEvent.Refresh)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}