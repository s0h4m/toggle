package cc.soham.togglesample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * App home page, shows a list of all the available samples
 */
public class SampleActivity extends AppCompatActivity {
    @BindView(R.id.activity_sample_samples)
    ListView samplesListView;

    SampleArrayAdapter sampleArrayAdapter;

    List<Sample> samples = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sample);

        ButterKnife.bind(this);
        init();
    }

    /**
     * Adds Sample to the sample list and initializes and sets the adapter
     */
    private void init() {
        samples.add(new Sample(getString(R.string.label_network), SampleNetworkActivity.class));
        samples.add(new Sample(getString(R.string.label_retrofit), SampleRetrofitActivity.class));
        samples.add(new Sample(getString(R.string.label_string), SampleStringActivity.class));
        samples.add(new Sample(getString(R.string.label_object), SampleConfigActivity.class));

        sampleArrayAdapter = new SampleArrayAdapter(samples);
        samplesListView.setAdapter(sampleArrayAdapter);
    }

    @OnItemClick(R.id.activity_sample_samples)
    public void samplesListView_onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(this, samples.get(position).aClass));
    }

    private class Sample {
        String name;
        Class aClass;

        public Sample(String name, Class<?> aClass) {
            this.name = name;
            this.aClass = aClass;
        }
    }

    public class SampleArrayAdapter extends BaseAdapter {
        List<Sample> samples;

        public SampleArrayAdapter(List<Sample> samples) {
            this.samples = samples;
        }

        @Override
        public int getCount() {
            return samples.size();
        }

        @Override
        public Sample getItem(int position) {
            return samples.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(SampleActivity.this).inflate(R.layout.activity_sample_item, parent, false);
                ViewHolder viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }

            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.titleTextView.setText(getItem(position).name);

            return convertView;
        }

        public class ViewHolder {
            @BindView(R.id.activity_sample_item_title)
            TextView titleTextView;

            public ViewHolder(View convertView) {
                ButterKnife.bind(this, convertView);
            }
        }
    }
}
