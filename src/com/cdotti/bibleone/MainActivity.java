package com.cdotti.bibleone;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity implements OnItemClickListener {
	private GridView gridNewTest;
	private GridView gridOldTest;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.
        // Binding utilizado nas paradas
        gridOldTest = (GridView) findViewById(R.id.gridOldTest);
        gridOldTest.setAdapter(new BibleBookAdapter(getApplicationContext(), "1"));

        // Binding utilizado nas paradas
        gridNewTest = (GridView) findViewById(R.id.gridNewTest);
        gridNewTest.setAdapter(new BibleBookAdapter(getApplicationContext(), "2"));
		
		// Configura o ItemClick function (nesta mesma classe)
		gridOldTest.setOnItemClickListener(this);
		gridNewTest.setOnItemClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // Listener para os cliques nos livros da biblia (Velho/Novo Testamento)
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		Intent intent = new Intent(getApplicationContext(), ChapterActivity.class);
		Integer bookID = 0;
		String bookName = "";
		
		if (parent.getId() == R.id.gridOldTest) {
			bookID = ((BibleBookAdapter) gridOldTest.getAdapter()).getItem(position).getId();
			bookName = ((BibleBookAdapter) gridOldTest.getAdapter()).getItem(position).getName();
		}
		else if (parent.getId() == R.id.gridNewTest) {
			bookID = ((BibleBookAdapter) gridNewTest.getAdapter()).getItem(position).getId();
			bookName = ((BibleBookAdapter) gridNewTest.getAdapter()).getItem(position).getName();
		}
		
		intent.putExtra("bookID", bookID);
		intent.putExtra("bookName", bookName);
		
		startActivity(intent);
	}
    
}
