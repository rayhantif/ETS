package com.example.aplikasiuntukuts.data;

import android.app.Application;
import android.database.Cursor;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

class CheeseRepository {
    private CheeseDao mCheeseDao;
    private  LiveData<List<Cheese>> mAllCheese;

    CheeseRepository(Application application){
        SampleDatabase db =SampleDatabase.getInstance(application);
        mCheeseDao=db.cheese();
        mAllCheese=mCheeseDao.getAlphabetizedCheese();
    }

    LiveData<List<Cheese>> getAllCheese(){return mAllCheese;}
    public void Insert (Cheese cheeses) {
        new InsertAsync((CheeseDao) mAllCheese).execute(cheeses);
    }
    private static class InsertAsync extends AsyncTask<Cheese, Void, Void> {

        private CheeseDao mAsyncTaskDao;

        InsertAsync(CheeseDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Cheese... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

}
