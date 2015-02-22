package com.example.jegerima.SIDWeb;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.jegerima.SIDWeb.adapters.SWEffects;
import com.example.jegerima.SIDWeb.adapters.SWNewsAdapter;
import com.example.jegerima.SIDWeb.database.DataBaseManagerAnnouncements;
import com.example.jegerima.SIDWeb.database.DataBaseManagerPlanning;
import com.example.jegerima.SIDWeb.widget.AnimatedExpandableListView;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;

import java.util.ArrayList;
import java.util.List;


public class PlanningFragment extends android.support.v4.app.Fragment {
    SWNewsAdapter dataAdapter;
    String id_course=null;
    AnimatedExpandableListView listView;
    ExampleAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bd = getArguments();
        if(bd!=null)
            id_course=bd.getString("course_id");
        View V = inflater.inflate(R.layout.fragment_planning, container, false);
        //final ExpandableListView ll=(ExpandableListView)V.findViewById(R.id.list_news);
        //ArrayList<NewsView> li|st=new ArrayList<NewsView>();
        //ArrayList<String[]> list=new ArrayList<String[]>();

        List<GroupItem> items = new ArrayList<GroupItem>();
        DataBaseManagerPlanning dbPlan=null;
        try {

            dbPlan = new DataBaseManagerPlanning(this.getActivity());
            Cursor datos_grupos = dbPlan.consultar_grupos(id_course);
            Cursor datos_items = null;
            if (datos_grupos.moveToFirst()) {

                //Recorremos el cursor hasta que no haya más registros
                do {
                    GroupItem uno = new GroupItem();
                    uno.title=datos_grupos.getString(1);

                    datos_items=dbPlan.consultar_items(id_course,datos_grupos.getString(0));
                    if(datos_items.moveToFirst()){
                        do{

                            ChildItem child = new ChildItem();
                            child.title = datos_items.getString(1);
                            child.hint = datos_items.getString(0);
                            uno.items.add(child);

                        }while (datos_items.moveToNext());
                    }

                    items.add(uno);

                } while(datos_grupos.moveToNext());
            }

        }catch (Exception e){
            System.out.println(e.toString());
        }finally {
            if(dbPlan!=null)
                dbPlan.close();

        }



        adapter = new ExampleAdapter(this.getActivity());
        adapter.setData(items);

        listView = (AnimatedExpandableListView) V.findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // In order to show animations, we need to use a custom click handler
        // for our ExpandableListView.
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.
                if (listView.isGroupExpanded(groupPosition)) {
                    listView.collapseGroupWithAnimation(groupPosition);
                } else {
                    listView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }

        });






        return V;
    }


    private static class GroupItem {
        String title;
        List<ChildItem> items = new ArrayList<ChildItem>();
    }

    private static class ChildItem {
        String title;
        String hint;
    }

    private static class ChildHolder {
        TextView title;
        TextView hint;
    }

    private static class GroupHolder {
        TextView title;
    }

    /**
     * Adapter for our list of {@link GroupItem}s.
     */
    private class ExampleAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
        private LayoutInflater inflater;

        private List<GroupItem> items;

        public ExampleAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<GroupItem> items) {
            this.items = items;
        }

        @Override
        public ChildItem getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).items.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder holder;
            ChildItem item = getChild(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = inflater.inflate(R.layout.list_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.textTitle);
                holder.hint = (TextView) convertView.findViewById(R.id.textHint);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }

            holder.title.setText(item.title);
            holder.hint.setText(item.hint);

            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return items.get(groupPosition).items.size();
        }

        @Override
        public GroupItem getGroup(int groupPosition) {
            return items.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHolder holder;
            GroupItem item = getGroup(groupPosition);
            if (convertView == null) {
                holder = new GroupHolder();
                convertView = inflater.inflate(R.layout.group_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.textTitle);
                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }

            holder.title.setText(item.title);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }

    }


}