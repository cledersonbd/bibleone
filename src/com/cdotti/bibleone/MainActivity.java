package com.cdotti.bibleone;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MainActivity extends BaseActivity 
			implements	OnItemClickListener, 
						BookFragment.OnBookSelectedListener,
						ChapterFragment.OnChapterSelectedListener {
	
	private Boolean isDualPane = false;
	
	public static final String FONT_EXO_PATH =  "fonts/exo200.ttf";
	public static final String FONT_ROBOTO_100_PATH =  "fonts/roboto100.ttf";
	public static final String FONT_MAIN_FONT_TITLE =  "fonts/roboto300.ttf";
	public static final String FONT_MAIN_FONT =  "fonts/dosis_regular.ttf";
	
	private String[] mMenuList;
	private ListView mDrawerList;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	
	private final static String TAG_BOOK_FRAG = "bookFrag";
	private final static String TAG_CHAPTER_FRAG = "chapterFrag";
	
	private final static Integer MATCH_PARENT = android.support.v4.widget.DrawerLayout.LayoutParams.MATCH_PARENT;
	
	private ChapterFragment chapterFrag = new ChapterFragment();
	private BookFragment bookFragment = new BookFragment();
	
	private FragmentManager mFragmentManager;
	
	private FrameLayout mBookFrameLayout;
	private FrameLayout mChapterFrameLayout;
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_frag);
		
		PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.preferences, false);
		
		// Se o layout de mainFrame esta existe == dual pane
		isDualPane = findViewById(R.id.mainFrameLayout) != null;
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mMenuList = getResources().getStringArray(R.array.drawerArray);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_main_item, mMenuList));
		mDrawerList.setOnItemClickListener(this);
		
		mDrawerToggle = new ActionBarDrawerToggle(
				this, 
				mDrawerLayout, 
				R.drawable.ic_drawer, 
				R.string.drawer_open, 
				R.string.drawer_close){

            /** Called when a drawer has settled in a completely closed state. */
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
			public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                	getActionBar().setTitle(getResources().getString(R.string.app_name));
                	invalidateOptionsMenu();
                }
            }

            /** Called when a drawer has settled in a completely open state. */
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
			public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                	getActionBar().setTitle(getResources().getString(R.string.strMenu));
                	invalidateOptionsMenu();
                }
            }
        };
		
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			getActionBar().setDisplayHomeAsUpEnabled(true);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
			getActionBar().setHomeButtonEnabled(true);
		
		mBookFrameLayout = (FrameLayout) findViewById(R.id.mainFrameLayout);
		mChapterFrameLayout = (FrameLayout) findViewById(R.id.chapterFrameLayout);
		
		mFragmentManager = getSupportFragmentManager();
		FragmentTransaction fragTrans = mFragmentManager.beginTransaction();
		fragTrans.setCustomAnimations(R.anim.activity_slide_in_from_right, R.anim.activity_slide_out_to_left,
				R.anim.activity_slide_in_from_left, R.anim.activity_slide_out_to_right);
		
		// Se estivermos no dual-pane layout
		if (isDualPane) {
			fragTrans.add(R.id.mainFrameLayout, bookFragment, TAG_BOOK_FRAG);
			mFragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
				
				@Override
				public void onBackStackChanged() {
					setLayout();				
				}
			});
		} else {
			fragTrans.replace(R.id.content_frame, bookFragment);
		}
		
		fragTrans.commit();
		
	}
	
    protected void setLayout() {
		
		if (!chapterFrag.isAdded()) {
			mBookFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
			mChapterFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0, MATCH_PARENT));
		} else {
			mBookFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0, MATCH_PARENT, 1f));
			mChapterFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0, MATCH_PARENT, 2f));
		}
	}

	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
          return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
	
	/* Called whenever we call invalidateOptionsMenu() */
	/*
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
	*/
	
	/**
	 * Funcao para o menu lateral - DrawerMenu
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Context mContext = getApplicationContext();
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft;
		String aboutTag = "about";
		
		/*
		 * 0 Favoritos
		 * 1 Configuracoes
		 * 2 Sobre
		 */
		// Favoritos
		if (position == 0) {
			startActivity(new Intent(getApplicationContext(), VerseFavActivity.class));
		}
		// COnfiguracoes
		else if (position == 1){
			/*
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {				
				ft = fm.beginTransaction();
				ft.setCustomAnimations(R.anim.activity_slide_in_from_right, R.anim.activity_slide_out_to_left,
						R.anim.activity_slide_in_from_left, R.anim.activity_slide_out_to_right);
				ft.replace(R.id.content_frame, new SettingsFragment());
				ft.commit();
			}
			*/
			Intent it = new Intent(getApplicationContext(), SettingsActivity.class);
			startActivity(it);
				
			
		}
		else if (position == 2 && fm.findFragmentByTag(aboutTag) == null) {
			String versionName;
			Fragment aboutfrag = new AboutFragment();
			Bundle args = new Bundle();
			
			try {
				versionName = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
			} catch (NameNotFoundException e) {
				versionName = "";
			}
			
			args.putString("version_number", versionName);
			aboutfrag.setArguments(args);
			
			ft = fm.beginTransaction();
			ft.setCustomAnimations(R.anim.activity_slide_in_from_right, R.anim.activity_slide_out_to_left,
					R.anim.activity_slide_in_from_left, R.anim.activity_slide_out_to_right);
			ft.replace(R.id.content_frame, aboutfrag, aboutTag).addToBackStack("chapter_verse");
			// Executa as alteracoes
			ft.commit();
			
			// Fecha a gaveta
			mDrawerLayout.closeDrawer(Gravity.START);
			
		}
		
	}

	/** This is a callback that the list fragment (Fragment A) calls
    when a list item is selected */
	@Override
	public void onBookSelected(Integer bookID, String bookName) {
		FragmentManager fm;
		FragmentTransaction ft;
		
		// single-pane layout
		if (!isDualPane) {
			Intent intent = new Intent(getApplicationContext(), ChapterActivity.class);
			
			intent.putExtra("bookID", bookID);
			intent.putExtra("bookName", bookName);
			
			startActivity(intent);
		// dual-pane layout
		} else  {
			if (!chapterFrag.isAdded()) {
				fm = getSupportFragmentManager();
				ft = fm.beginTransaction();
				Bundle args = new Bundle();
				args.putInt("bookID", bookID);
				args.putString(bookName, bookName);			
				chapterFrag.setArguments(args);
				ft.add(R.id.chapterFrameLayout, chapterFrag, TAG_CHAPTER_FRAG);
				ft.addToBackStack(null);
				ft.commit();
				fm.executePendingTransactions();
			} else {
				chapterFrag.updateContent(bookID);	
			}
		}
	}

	@Override
	public void onChapterSelected(Integer bookid, Integer chapterid) {
		
		
	}
}
