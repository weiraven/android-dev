package edu.uncc.assessment03.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import edu.uncc.assessment03.R;
import edu.uncc.assessment03.databinding.FragmentFilterBinding;
import edu.uncc.assessment03.models.CreditCategory;


public class FilterFragment extends Fragment {

    public FilterFragment() {
        // Required empty public constructor
    }

    FragmentFilterBinding binding;
    CreditCategory[] creditCategories = {
            new CreditCategory("Excellent", R.drawable.excellent, 800,850),
            new CreditCategory("Very Good", R.drawable.very_good, 740, 799),
            new CreditCategory("Good", R.drawable.good, 670, 739),
            new CreditCategory("Fair", R.drawable.fair, 580, 669),
            new CreditCategory("Poor", R.drawable.poor, 300, 579)};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFilterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    CreditArrayAdapter adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Select Filter");
        List<CreditCategory> creditCategoryList = new ArrayList<>(List.of(creditCategories));
        adapter = new CreditArrayAdapter(getContext(), creditCategoryList);
        binding.listView.setAdapter(adapter);

        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CreditCategory creditCategory = creditCategoryList.get(position);
                mListener.onFilterSelected(creditCategory);
            }
        });

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.cancelAddOrSelection();
            }
        });

        binding.buttonNoFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFilterSelected(null);
            }
        });
    }

    class CreditArrayAdapter extends ArrayAdapter<CreditCategory> {

        public CreditArrayAdapter(@NonNull Context context, @NonNull List<CreditCategory> objects) {
            super(context, R.layout.credit_category_item, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.credit_category_item, parent, false);
            }

            CreditCategory creditCategory = getItem(position);

            TextView textViewCreditCategory = convertView.findViewById(R.id.textViewCreditCategory);
            ImageView imageViewCreditImage = convertView.findViewById(R.id.imageViewCreditImage);

            textViewCreditCategory.setText(creditCategory.getName());
            imageViewCreditImage.setImageResource(creditCategory.getImageResourceId());

            return convertView;
        }
    }
    FilterListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //surround by try catch block
        try {
            mListener = (FilterListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement FilterListener");
        }
    }

    public interface FilterListener{
        void onFilterSelected(CreditCategory creditCategory);
        void cancelAddOrSelection();
    }
}