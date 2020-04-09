/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.example.aplikasiuntukuts;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplikasiuntukuts.data.Cheese;
import com.example.aplikasiuntukuts.data.CheeseViewModel;
import com.example.aplikasiuntukuts.provider.SampleContentProvider;

import java.util.List;


/**
 * Not very relevant to Room. This just shows data from {@link SampleContentProvider}.
 *
 * <p>Since the data is exposed through the ContentProvider, other apps can read and write the
 * content in a similar manner to this.</p>
 */
public class MainActivity extends AppCompatActivity {

    private static final int LOADER_CHEESES = 1;
    private RecyclerView list;
   // private CheeseListAdapter mCheeseAdapter;
    private CheeseAdapter mCheeseAdapter;
    private CheeseViewModel mCheeseViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list=(RecyclerView) findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(list.getContext()));
        //mCheeseAdapter = new CheeseListAdapter(this);
        mCheeseAdapter=new CheeseAdapter();
        list.setAdapter(mCheeseAdapter);
        /*mCheeseViewModel=new ViewModelProvider(this).get(CheeseViewModel.class);
        mCheeseViewModel.getAllCheese().observe(this, new Observer<List<Cheese>>() {
            @Override
            public void onChanged(@Nullable final List<Cheese> Cheeses) {
                // Update the cached copy of the words in the adapter.
                mCheeseAdapter.setCheese(Cheeses);
            }
        });*/
        LoaderManager.getInstance(this).initLoader(LOADER_CHEESES, null, mLoaderCallbacks);
    }

   private final LoaderManager.LoaderCallbacks<Cursor> mLoaderCallbacks =
            new LoaderManager.LoaderCallbacks<Cursor>() {

                @Override
                @NonNull
                public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
                    return new CursorLoader(getApplicationContext(),
                            SampleContentProvider.URI_CHEESE,
                            new String[]{Cheese.COLUMN_NAME},
                            null, null, null);
                }

                @Override
                public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
                    mCheeseAdapter.setCheeses(data);
                }

                @Override
                public void onLoaderReset(@NonNull Loader<Cursor> loader) {
                    mCheeseAdapter.setCheeses(null);
                }

            };

   private static class CheeseListAdapter extends RecyclerView.Adapter<CheeseListAdapter.CheeseViewHolder>{
       class CheeseViewHolder extends RecyclerView.ViewHolder {
           private final TextView CheeseItemView;

           private CheeseViewHolder(View itemView) {
               super(itemView);
               CheeseItemView = itemView.findViewById(android.R.id.text1);

           }
       }

       private final LayoutInflater mInflater;
       private List<Cheese> mCheese; // Cached copy of words

       CheeseListAdapter(Context context) {
           mInflater = LayoutInflater.from(context);
       }

       @Override
       public CheeseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
           View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
           return new CheeseViewHolder(itemView);
       }

       @Override
       public void onBindViewHolder(CheeseViewHolder holder, int position) {
           if (mCheese != null) {
               Cheese current = mCheese.get(position);
               holder.CheeseItemView.setText(current.name);
           } else {
               // Covers the case of data not being ready yet.
               holder.CheeseItemView.setText("No Word");
           }
       }

       void setCheese(List<Cheese> cheeses) {
           mCheese = cheeses;
           notifyDataSetChanged();
       }

       // getItemCount() is called many times, and when it is first called,
       // mWords has not been updated (means initially, it's null, and we can't return null).
       @Override
       public int getItemCount() {
           if (mCheese != null)
               return mCheese.size();
           else return 0;
       }
   }

    private static class CheeseAdapter extends RecyclerView.Adapter<CheeseAdapter.ViewHolder> {

        private Cursor mCursor;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (mCursor.moveToPosition(position)) {
                holder.mText.setText(mCursor.getString(
                        mCursor.getColumnIndexOrThrow(Cheese.COLUMN_NAME)));
            }
        }

        @Override
        public int getItemCount() {
            return mCursor == null ? 0 : mCursor.getCount();
        }

        void setCheeses(Cursor cursor) {
            mCursor = cursor;
            notifyDataSetChanged();
        }


        static class ViewHolder extends RecyclerView.ViewHolder {

            TextView mText;

            ViewHolder(ViewGroup parent) {
                super(LayoutInflater.from(parent.getContext()).inflate(
                        android.R.layout.simple_list_item_1, parent, false));
                mText = (TextView) itemView.findViewById(android.R.id.text1);
            }

        }

    }

}
