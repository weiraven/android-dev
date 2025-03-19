package edu.uncc.assessment03.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

import edu.uncc.assessment03.R;
import edu.uncc.assessment03.databinding.FragmentUsersBinding;
import edu.uncc.assessment03.databinding.UserRowItemBinding;
import edu.uncc.assessment03.models.CreditCategory;
import edu.uncc.assessment03.models.User;

public class UsersFragment extends Fragment {
    public UsersFragment() {
        // Required empty public constructor
    }

    FragmentUsersBinding binding;
    String selectedSort;
    String sortDirection = "ASC";
    CreditCategory selectedFilterCategory;
    ArrayList<User> mUsers = new ArrayList<>();
    ArrayList<User> filteredUsers = new ArrayList<>();
    UsersAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUsersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Users");
        mUsers = mListener.getAllUsers();


        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.add_new_user_action){
                    mListener.gotoAddUser();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        binding.imageViewSort.setOnClickListener(view1 -> mListener.gotoSelectSort());

        binding.imageViewFilter.setOnClickListener(view1 -> mListener.gotoSelectFilter());

        binding.imageViewSortAsc.setOnClickListener(view1 -> {
            sortDirection = "ASC";
            sortUsers();
        });

        binding.imageViewSortDesc.setOnClickListener(view1 -> {
            sortDirection = "DESC";
            sortUsers();
        });

        if(selectedFilterCategory == null){
            binding.textViewFilter.setText("N/A");
        } else {
            binding.textViewFilter.setText(selectedFilterCategory.getName() + " or Higher" );
        }

        if(selectedSort == null){
            selectedSort = "Name";
            sortDirection = "ASC";
        }

        binding.textViewSort.setText(selectedSort + " (" + sortDirection + ")");

        filterUsers();
        adapter = new UsersAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        sortUsers();

    }

    private void sortUsers() {
        if (selectedSort != null) {
            Comparator<User> comparator = null;
            switch (selectedSort) {
                case "Name":
                    comparator = Comparator.comparing(User::getName);
                    break;
                case "Age":
                    comparator = Comparator.comparingInt(User::getAge);
                    break;
                case "Credit Score":
                    comparator = Comparator.comparingInt(User::getCreditScore);
                    break;
                case "State":
                    comparator = Comparator.comparing(user -> user.getState().getName());
                    break;
            }

            if (comparator != null) {
                if (sortDirection.equals("DESC")) {
                    comparator = comparator.reversed();
                }
                Collections.sort(filteredUsers, comparator);
                adapter.notifyDataSetChanged();
                binding.textViewSort.setText(selectedSort + " (" + sortDirection + ")");
            }
        }
    }

    private void filterUsers() {
        mUsers = mListener.getAllUsers();
        filteredUsers.clear();

        if (selectedFilterCategory != null) {

            for (User user : mUsers) {
                if (user.getCreditScore() >= selectedFilterCategory.getMin()) {
                    filteredUsers.add(user);
                }
            }

        } else {
            filteredUsers = new ArrayList<>(mListener.getAllUsers());
        }
    }
    class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {
        @NonNull
        @Override
        public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            UserRowItemBinding itemBinding = UserRowItemBinding.inflate(getLayoutInflater(), parent, false);
            return new UserViewHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
            User user = filteredUsers.get(position);
            holder.bind(user);
        }

        @Override
        public int getItemCount() {
            return filteredUsers.size();
        }

        class UserViewHolder extends RecyclerView.ViewHolder {
            UserRowItemBinding itemBinding;
            User mUser;

            public UserViewHolder(@NonNull UserRowItemBinding itemBinding) {
                super(itemBinding.getRoot());
                this.itemBinding = itemBinding;
            }

            public void bind(User user){
                this.mUser = user;
                itemBinding.textViewUserName.setText(user.getName());
                itemBinding.textViewUserAge.setText(String.valueOf(user.getAge()));
                itemBinding.textViewUserState.setText(user.getState().getName());

                if(user.getCreditScore() >= 300 && user.getCreditScore() <= 579) {
                    itemBinding.imageViewCredit.setImageResource(R.drawable.poor);
                } else if(user.getCreditScore() >= 580 && user.getCreditScore() <= 669) {
                    itemBinding.imageViewCredit.setImageResource(R.drawable.fair);
                } else if(user.getCreditScore() >= 670 && user.getCreditScore() <= 739) {
                    itemBinding.imageViewCredit.setImageResource(R.drawable.good);
                } else if(user.getCreditScore() >= 740 && user.getCreditScore() <= 799) {
                    itemBinding.imageViewCredit.setImageResource(R.drawable.very_good);
                } else {
                    itemBinding.imageViewCredit.setImageResource(R.drawable.excellent);
                }

                itemBinding.textViewCreditScore.setText(String.valueOf(user.getCreditScore()));

                itemBinding.imageViewTrashIcon.setOnClickListener(v -> {
                    mUsers.remove(mUser);
                    filterUsers();
                });
            }
        }
    }

    UsersListener mListener;
    public void setSelectedSort(String selectedSort) {
        this.selectedSort = selectedSort;
    }

    public void setSelectedFilterCategory(CreditCategory selectedFilterCategory) {
        this.selectedFilterCategory = selectedFilterCategory;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof UsersListener) {
            mListener = (UsersListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement UsersListener");
        }
    }

    public interface UsersListener {
        void gotoAddUser();
        void gotoSelectFilter();
        void gotoSelectSort();
        ArrayList<User> getAllUsers();
    }
}