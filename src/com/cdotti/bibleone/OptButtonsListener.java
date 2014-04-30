package com.cdotti.bibleone;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class OptButtonsListener implements OnClickListener {

	private Context mContext;

	public OptButtonsListener(Context c) {
		super();
		this.mContext = c;
	}
	
	@Override
	public void onClick(View v) {		
		if (v.getId() == R.id.btnOptFav) {
			FavVerseDAO favDAO = new FavVerseDAO(mContext);
			// Se inseriu corretamente
			if ( favDAO.insertF(new VerseFav((BibleVerse) v.getTag())) )
				Toast.makeText(mContext, "Favorito salvo", Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(mContext, "Erro ao salvar favorito", Toast.LENGTH_SHORT).show();
		}
	}

}
