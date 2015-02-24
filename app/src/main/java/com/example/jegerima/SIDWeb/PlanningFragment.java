package com.example.jegerima.SIDWeb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PlanningFragment extends android.support.v4.app.Fragment {
    SWNewsAdapter dataAdapter;
    String id_course=null;
    AnimatedExpandableListView listView;
    ExampleAdapter adapter;
    HashMap<String,String> plan_type=new HashMap<String, String>();

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

        plan_type.put("Attachment","Adjunto");
        plan_type.put("ExternalUrl","Link Externo");
        plan_type.put("WikiPage","Página de Wiki");
        plan_type.put("ContextModuleSubHeader","ContextModuleSubHeader");
        plan_type.put("Assignment","Calificacion");

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
                            child.url = datos_items.getString(5);
                            child.hint = plan_type.get(datos_items.getString(4));
                            if(child.hint.equalsIgnoreCase("Adjunto"))
                                child.url = "https://www.sidweb.espol.edu.ec/courses/"+datos_items.getString(2)+"/files/"+datos_items.getString(6)+"/download";
                            else
                                child.url = datos_items.getString(5);
                            child.id = datos_items.getString(0);
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
        String url;
        String id;
    }

    private static class ChildHolder {
        TextView title;
        TextView hint;
        TextView url;
        TextView id;
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
                convertView = inflater.inflate(R.layout.plan_list_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.textTitle);
                holder.hint = (TextView) convertView.findViewById(R.id.textHint);
                holder.url = (TextView) convertView.findViewById(R.id.hideTextUrl);
                holder.id = (TextView) convertView.findViewById(R.id.hideTextId);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }

            holder.title.setText(item.title);
            holder.hint.setText(item.hint);
            holder.url.setText(item.url);
            holder.id.setText(item.id);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(((TextView) v.findViewById(R.id.hideTextUrl)).getText().toString());
                    try {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(((TextView) v.findViewById(R.id.hideTextUrl)).getText().toString()));
                        startActivity(myIntent);
                        //descarga("http://cdns2.freepik.com/foto-gratis/pequena-casa-con-jardin_417887.jpg");
                    } catch (Exception e) {
                        System.out.println("errpr");

                    }
                }
            });

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
                convertView = inflater.inflate(R.layout.plan_group_item, parent, false);
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


    public static void descarga(String Url) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            System.out.println("inicio");
            URL url = new URL(Url);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the filevvv
            System.out.println("sss");
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return ;
            }
            System.out.println("ddd");
            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();

            File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/SidWeb/Descargas");
            directory.mkdirs();
            output = new FileOutputStream("/sdcard/cassa.jpg");
            System.out.println("asdasdasd");
            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {


                output.write(data, 0, count);
            }
        } catch (Exception e) {
            System.out.println("no abrio");
            System.out.println(e.toString());
            return ;
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (Exception ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        return ;
    }


}