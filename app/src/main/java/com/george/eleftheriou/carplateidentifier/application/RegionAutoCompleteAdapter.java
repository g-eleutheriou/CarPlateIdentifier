package com.george.eleftheriou.carplateidentifier.application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.george.eleftheriou.carplateidentifier.Controllers.RegionController;
import com.george.eleftheriou.carplateidentifier.Models.RegionModel;
import com.george.eleftheriou.carplateidentifier.R;

import java.util.ArrayList;
import java.util.List;

public class RegionAutoCompleteAdapter extends BaseAdapter implements Filterable {

    private final Context context;
    private List<RegionModel> data = new ArrayList<>();

    public RegionAutoCompleteAdapter(final Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.region_auto_complete_list_item_layout, viewGroup, false);
        }

        final RegionModel model = (RegionModel) getItem(i);

        if (model != null) {
            ((TextView) view.findViewById(R.id.regionName)).setText(model.getName());
            ((TextView) view.findViewById(R.id.regionCode)).setText(model.getCode());
        }

        return view;
    }

    @Override
    public Filter getFilter() {
        final Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                final FilterResults rs = new FilterResults();
                final List<RegionModel> models = RegionController.getModelsByCode(String.valueOf(charSequence));

                if (models != null) {
                    rs.count = models.size();
                    rs.values = models;
                }

                return rs;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults != null && filterResults.count > 0) {
                    data.clear();
                    data = (List<RegionModel>) filterResults.values;

                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };

        return filter;
    }

}
