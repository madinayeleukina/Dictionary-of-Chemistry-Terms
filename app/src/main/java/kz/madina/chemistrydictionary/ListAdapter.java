package kz.madina.chemistrydictionary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

    private ArrayList<Term> list;
    private LayoutInflater inflater;
    private DatabaseRepository databaseRepository;
    private Context context;

    public ListAdapter(Context context, DatabaseRepository databaseRepository, ArrayList<Term> data) {
        list = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.databaseRepository = databaseRepository;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row, null);

        TextView title = (TextView)vi.findViewById(R.id.title);
        TextView subtitle = (TextView)vi.findViewById(R.id.subtitle);
        final ToggleButton bookmark = (ToggleButton) vi.findViewById(R.id.bookmark);

        final Term term = list.get(position);
        title.setText(term.getFrst().getWord());
        subtitle.setText(term.getScnd().getWord());
        bookmark.setChecked(term.getBookmarked() == 1);

        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookmark.isChecked()) {
                    databaseRepository.addBookmark(term.getId());
                    list.get(position).setBookmarked(1);
                    Toast.makeText(context, "Bookmark added", Toast.LENGTH_SHORT).show();
                } else {
                    databaseRepository.deleteBookmark(term.getId());
                    list.get(position).setBookmarked(0);
                    Toast.makeText(context, "Bookmark deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return vi;
    }

    public void updateList(ArrayList<Term> list) {
        if (list != null) {
            this.list = list;
            notifyDataSetChanged();
        }
    }
}
