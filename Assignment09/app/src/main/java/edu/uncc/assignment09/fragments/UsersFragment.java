package edu.uncc.assignment09.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.uncc.assignment09.R;
import edu.uncc.assignment09.databinding.FragmentUsersBinding;
import edu.uncc.assignment09.databinding.RowItemUserBinding;
import edu.uncc.assignment09.models.AppDatabase;
import edu.uncc.assignment09.models.CreditCategory;
import edu.uncc.assignment09.models.User;

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
    UsersAdapter adapter = new UsersAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUsersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Users");

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

        binding.imageViewSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoSelectSort();
            }
        });

        binding.imageViewFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoSelectFilter();
            }
        });

        binding.imageViewSortAsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortDirection = "ASC";
                sortAndFilter();
            }
        });

        binding.imageViewSortDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortDirection = "DESC";
                sortAndFilter();
            }
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


        adapter = new UsersAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        sortAndFilter();
    }

    private void sortAndFilter(){
        String sortBy = "name";
        String sortDir = "ASC";
        if(selectedSort != null){
            sortDir = this.sortDirection;
            switch (selectedSort) {
                case "Name":
                    sortBy = "name";
                    break;
                case "Age":
                    sortBy = "age";
                    break;
                case "Credit Score":
                    sortBy = "credit_score";
                    break;
                case "State":
                    sortBy = "state";
                    break;
            }
        }

        int threshold = 0;
        if(selectedFilterCategory != null){
            threshold = selectedFilterCategory.getMin();
        }

        filteredUsers.clear();
        filteredUsers.addAll(mListener.getFilteredAndSortedUsers(threshold, sortBy, sortDir));
        adapter.notifyDataSetChanged();
    }

    class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder>{
        @NonNull
        @Override
        public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RowItemUserBinding itemBinding = RowItemUserBinding.inflate(getLayoutInflater(), parent, false);
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
            RowItemUserBinding itemBinding;
            User mUser;
            public UserViewHolder(RowItemUserBinding itemBinding) {
                super(itemBinding.getRoot());
                this.itemBinding = itemBinding;
            }

            public void bind(User user) {
                this.mUser = user;
                itemBinding.textViewName.setText(user.getName());
                itemBinding.textViewAge.setText(user.getAge() + " years old");
                itemBinding.textViewState.setText(user.getState());

                //Poor (300-579), Fair (580-669), Good (670-739), Very Good (740-799), Excellent (800-850).
                itemBinding.textViewCreditScore.setText(String.valueOf(user.getCreditScore()));
                if(user.getCreditScore() <= 579){
                    itemBinding.imageViewCreditScore.setImageResource(R.drawable.poor);
                } else if(user.getCreditScore() <= 669){
                    itemBinding.imageViewCreditScore.setImageResource(R.drawable.fair);
                } else if(user.getCreditScore() <= 739){
                    itemBinding.imageViewCreditScore.setImageResource(R.drawable.good);
                } else if(user.getCreditScore() <= 799){
                    itemBinding.imageViewCreditScore.setImageResource(R.drawable.very_good);
                } else {
                    itemBinding.imageViewCreditScore.setImageResource(R.drawable.excellent);
                }

                itemBinding.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.deleteUser(mUser);
                        sortAndFilter();
                    }
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
        void deleteUser(User user);
        List<User> getFilteredAndSortedUsers(int threshold, String sortBy, String sortDirection);
    }
}